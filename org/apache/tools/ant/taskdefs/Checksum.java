package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.taskdefs.condition.*;
import org.apache.tools.ant.*;
import java.security.*;
import org.apache.tools.ant.util.*;
import java.util.*;
import java.io.*;
import java.text.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.types.resources.selectors.*;
import org.apache.tools.ant.types.*;

public class Checksum extends MatchingTask implements Condition
{
    private static final int NIBBLE = 4;
    private static final int WORD = 16;
    private static final int BUFFER_SIZE = 8192;
    private static final int BYTE_MASK = 255;
    private File file;
    private File todir;
    private String algorithm;
    private String provider;
    private String fileext;
    private String property;
    private Map<File, byte[]> allDigests;
    private Map<File, String> relativeFilePaths;
    private String totalproperty;
    private boolean forceOverwrite;
    private String verifyProperty;
    private FileUnion resources;
    private Hashtable<File, Object> includeFileMap;
    private MessageDigest messageDigest;
    private boolean isCondition;
    private int readBufferSize;
    private MessageFormat format;
    
    public Checksum() {
        super();
        this.file = null;
        this.algorithm = "MD5";
        this.provider = null;
        this.allDigests = new HashMap<File, byte[]>();
        this.relativeFilePaths = new HashMap<File, String>();
        this.resources = null;
        this.includeFileMap = new Hashtable<File, Object>();
        this.readBufferSize = 8192;
        this.format = FormatElement.getDefault().getFormat();
    }
    
    public void setFile(final File file) {
        this.file = file;
    }
    
    public void setTodir(final File todir) {
        this.todir = todir;
    }
    
    public void setAlgorithm(final String algorithm) {
        this.algorithm = algorithm;
    }
    
    public void setProvider(final String provider) {
        this.provider = provider;
    }
    
    public void setFileext(final String fileext) {
        this.fileext = fileext;
    }
    
    public void setProperty(final String property) {
        this.property = property;
    }
    
    public void setTotalproperty(final String totalproperty) {
        this.totalproperty = totalproperty;
    }
    
    public void setVerifyproperty(final String verifyProperty) {
        this.verifyProperty = verifyProperty;
    }
    
    public void setForceOverwrite(final boolean forceOverwrite) {
        this.forceOverwrite = forceOverwrite;
    }
    
    public void setReadBufferSize(final int size) {
        this.readBufferSize = size;
    }
    
    public void setFormat(final FormatElement e) {
        this.format = e.getFormat();
    }
    
    public void setPattern(final String p) {
        this.format = new MessageFormat(p);
    }
    
    public void addFileset(final FileSet set) {
        this.add(set);
    }
    
    public void add(final ResourceCollection rc) {
        if (rc == null) {
            return;
        }
        (this.resources = ((this.resources == null) ? new FileUnion() : this.resources)).add(rc);
    }
    
    public void execute() throws BuildException {
        this.isCondition = false;
        final boolean value = this.validateAndExecute();
        if (this.verifyProperty != null) {
            this.getProject().setNewProperty(this.verifyProperty, value ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
        }
    }
    
    public boolean eval() throws BuildException {
        this.isCondition = true;
        return this.validateAndExecute();
    }
    
    private boolean validateAndExecute() throws BuildException {
        final String savedFileExt = this.fileext;
        if (this.file == null && (this.resources == null || this.resources.size() == 0)) {
            throw new BuildException("Specify at least one source - a file or a resource collection.");
        }
        if (this.resources != null && !this.resources.isFilesystemOnly()) {
            throw new BuildException("Can only calculate checksums for file-based resources.");
        }
        if (this.file != null && this.file.exists() && this.file.isDirectory()) {
            throw new BuildException("Checksum cannot be generated for directories");
        }
        if (this.file != null && this.totalproperty != null) {
            throw new BuildException("File and Totalproperty cannot co-exist.");
        }
        if (this.property != null && this.fileext != null) {
            throw new BuildException("Property and FileExt cannot co-exist.");
        }
        if (this.property != null) {
            if (this.forceOverwrite) {
                throw new BuildException("ForceOverwrite cannot be used when Property is specified");
            }
            int ct = 0;
            if (this.resources != null) {
                ct += this.resources.size();
            }
            if (this.file != null) {
                ++ct;
            }
            if (ct > 1) {
                throw new BuildException("Multiple files cannot be used when Property is specified");
            }
        }
        if (this.verifyProperty != null) {
            this.isCondition = true;
        }
        if (this.verifyProperty != null && this.forceOverwrite) {
            throw new BuildException("VerifyProperty and ForceOverwrite cannot co-exist.");
        }
        if (this.isCondition && this.forceOverwrite) {
            throw new BuildException("ForceOverwrite cannot be used when conditions are being used.");
        }
        this.messageDigest = null;
        Label_0365: {
            if (this.provider != null) {
                try {
                    this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
                    break Label_0365;
                }
                catch (NoSuchAlgorithmException noalgo) {
                    throw new BuildException(noalgo, this.getLocation());
                }
                catch (NoSuchProviderException noprovider) {
                    throw new BuildException(noprovider, this.getLocation());
                }
            }
            try {
                this.messageDigest = MessageDigest.getInstance(this.algorithm);
            }
            catch (NoSuchAlgorithmException noalgo) {
                throw new BuildException(noalgo, this.getLocation());
            }
        }
        if (this.messageDigest == null) {
            throw new BuildException("Unable to create Message Digest", this.getLocation());
        }
        if (this.fileext == null) {
            this.fileext = "." + this.algorithm;
        }
        else if (this.fileext.trim().length() == 0) {
            throw new BuildException("File extension when specified must not be an empty string");
        }
        try {
            if (this.resources != null) {
                for (final Resource r : this.resources) {
                    final File src = r.as(FileProvider.class).getFile();
                    if (this.totalproperty != null || this.todir != null) {
                        this.relativeFilePaths.put(src, r.getName().replace(File.separatorChar, '/'));
                    }
                    this.addToIncludeFileMap(src);
                }
            }
            if (this.file != null) {
                if (this.totalproperty != null || this.todir != null) {
                    this.relativeFilePaths.put(this.file, this.file.getName().replace(File.separatorChar, '/'));
                }
                this.addToIncludeFileMap(this.file);
            }
            return this.generateChecksums();
        }
        finally {
            this.fileext = savedFileExt;
            this.includeFileMap.clear();
        }
    }
    
    private void addToIncludeFileMap(final File file) throws BuildException {
        if (file.exists()) {
            if (this.property == null) {
                final File checksumFile = this.getChecksumFile(file);
                if (this.forceOverwrite || this.isCondition || file.lastModified() > checksumFile.lastModified()) {
                    this.includeFileMap.put(file, checksumFile);
                }
                else {
                    this.log(file + " omitted as " + checksumFile + " is up to date.", 3);
                    if (this.totalproperty != null) {
                        final String checksum = this.readChecksum(checksumFile);
                        final byte[] digest = decodeHex(checksum.toCharArray());
                        this.allDigests.put(file, digest);
                    }
                }
            }
            else {
                this.includeFileMap.put(file, this.property);
            }
            return;
        }
        final String message = "Could not find file " + file.getAbsolutePath() + " to generate checksum for.";
        this.log(message);
        throw new BuildException(message, this.getLocation());
    }
    
    private File getChecksumFile(final File file) {
        File directory;
        if (this.todir != null) {
            final String path = this.getRelativeFilePath(file);
            directory = new File(this.todir, path).getParentFile();
            directory.mkdirs();
        }
        else {
            directory = file.getParentFile();
        }
        final File checksumFile = new File(directory, file.getName() + this.fileext);
        return checksumFile;
    }
    
    private boolean generateChecksums() throws BuildException {
        boolean checksumMatches = true;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        final byte[] buf = new byte[this.readBufferSize];
        try {
            for (final Map.Entry<File, Object> e : this.includeFileMap.entrySet()) {
                this.messageDigest.reset();
                final File src = e.getKey();
                if (!this.isCondition) {
                    this.log("Calculating " + this.algorithm + " checksum for " + src, 3);
                }
                fis = new FileInputStream(src);
                final DigestInputStream dis = new DigestInputStream(fis, this.messageDigest);
                while (dis.read(buf, 0, this.readBufferSize) != -1) {}
                dis.close();
                fis.close();
                fis = null;
                final byte[] fileDigest = this.messageDigest.digest();
                if (this.totalproperty != null) {
                    this.allDigests.put(src, fileDigest);
                }
                final String checksum = this.createDigestString(fileDigest);
                final Object destination = e.getValue();
                if (destination instanceof String) {
                    final String prop = (String)destination;
                    if (this.isCondition) {
                        checksumMatches = (checksumMatches && checksum.equals(this.property));
                    }
                    else {
                        this.getProject().setNewProperty(prop, checksum);
                    }
                }
                else {
                    if (!(destination instanceof File)) {
                        continue;
                    }
                    if (this.isCondition) {
                        final File existingFile = (File)destination;
                        if (existingFile.exists()) {
                            try {
                                final String suppliedChecksum = this.readChecksum(existingFile);
                                checksumMatches = (checksumMatches && checksum.equals(suppliedChecksum));
                            }
                            catch (BuildException be) {
                                checksumMatches = false;
                            }
                        }
                        else {
                            checksumMatches = false;
                        }
                    }
                    else {
                        final File dest = (File)destination;
                        fos = new FileOutputStream(dest);
                        fos.write(this.format.format(new Object[] { checksum, src.getName(), FileUtils.getRelativePath(dest.getParentFile(), src), FileUtils.getRelativePath(this.getProject().getBaseDir(), src), src.getAbsolutePath() }).getBytes());
                        fos.write(StringUtils.LINE_SEP.getBytes());
                        fos.close();
                        fos = null;
                    }
                }
            }
            if (this.totalproperty != null) {
                final File[] keyArray = this.allDigests.keySet().toArray(new File[this.allDigests.size()]);
                Arrays.sort(keyArray, new Comparator<File>() {
                    public int compare(final File f1, final File f2) {
                        return (f1 == null) ? ((f2 == null) ? 0 : -1) : ((f2 == null) ? 1 : Checksum.this.getRelativeFilePath(f1).compareTo(Checksum.this.getRelativeFilePath(f2)));
                    }
                });
                this.messageDigest.reset();
                for (final File src2 : keyArray) {
                    final byte[] digest = this.allDigests.get(src2);
                    this.messageDigest.update(digest);
                    final String fileName = this.getRelativeFilePath(src2);
                    this.messageDigest.update(fileName.getBytes());
                }
                final String totalChecksum = this.createDigestString(this.messageDigest.digest());
                this.getProject().setNewProperty(this.totalproperty, totalChecksum);
            }
        }
        catch (Exception e2) {
            throw new BuildException(e2, this.getLocation());
        }
        finally {
            FileUtils.close(fis);
            FileUtils.close(fos);
        }
        return checksumMatches;
    }
    
    private String createDigestString(final byte[] fileDigest) {
        final StringBuffer checksumSb = new StringBuffer();
        for (int i = 0; i < fileDigest.length; ++i) {
            final String hexStr = Integer.toHexString(0xFF & fileDigest[i]);
            if (hexStr.length() < 2) {
                checksumSb.append("0");
            }
            checksumSb.append(hexStr);
        }
        return checksumSb.toString();
    }
    
    public static byte[] decodeHex(final char[] data) throws BuildException {
        final int l = data.length;
        if ((l & 0x1) != 0x0) {
            throw new BuildException("odd number of characters.");
        }
        final byte[] out = new byte[l >> 1];
        int f;
        for (int i = 0, j = 0; j < l; f = Character.digit(data[j++], 16) << 4, f |= Character.digit(data[j++], 16), out[i] = (byte)(f & 0xFF), ++i) {}
        return out;
    }
    
    private String readChecksum(final File f) {
        BufferedReader diskChecksumReader = null;
        try {
            diskChecksumReader = new BufferedReader(new FileReader(f));
            final Object[] result = this.format.parse(diskChecksumReader.readLine());
            if (result == null || result.length == 0 || result[0] == null) {
                throw new BuildException("failed to find a checksum");
            }
            return (String)result[0];
        }
        catch (IOException e) {
            throw new BuildException("Couldn't read checksum file " + f, e);
        }
        catch (ParseException e2) {
            throw new BuildException("Couldn't read checksum file " + f, e2);
        }
        finally {
            FileUtils.close(diskChecksumReader);
        }
    }
    
    private String getRelativeFilePath(final File f) {
        final String path = this.relativeFilePaths.get(f);
        if (path == null) {
            throw new BuildException("Internal error: relativeFilePaths could not match file " + f + "\n" + "please file a bug report on this");
        }
        return path;
    }
    
    private static class FileUnion extends Restrict
    {
        private Union u;
        
        FileUnion() {
            super();
            super.add(this.u = new Union());
            super.add(Type.FILE);
        }
        
        public void add(final ResourceCollection rc) {
            this.u.add(rc);
        }
    }
    
    public static class FormatElement extends EnumeratedAttribute
    {
        private static HashMap<String, MessageFormat> formatMap;
        private static final String CHECKSUM = "CHECKSUM";
        private static final String MD5SUM = "MD5SUM";
        private static final String SVF = "SVF";
        
        public static FormatElement getDefault() {
            final FormatElement e = new FormatElement();
            e.setValue("CHECKSUM");
            return e;
        }
        
        public MessageFormat getFormat() {
            return FormatElement.formatMap.get(this.getValue());
        }
        
        public String[] getValues() {
            return new String[] { "CHECKSUM", "MD5SUM", "SVF" };
        }
        
        static {
            (FormatElement.formatMap = new HashMap<String, MessageFormat>()).put("CHECKSUM", new MessageFormat("{0}"));
            FormatElement.formatMap.put("MD5SUM", new MessageFormat("{0} *{1}"));
            FormatElement.formatMap.put("SVF", new MessageFormat("MD5 ({1}) = {0}"));
        }
    }
}
