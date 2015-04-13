package org.apache.tools.ant.util;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.*;
import java.text.*;
import org.apache.tools.ant.launch.*;
import java.nio.channels.*;
import java.net.*;
import java.util.jar.*;
import java.util.*;
import java.io.*;
import org.apache.tools.ant.taskdefs.condition.*;

public class FileUtils
{
    private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
    private static final int EXPAND_SPACE = 50;
    private static final FileUtils PRIMARY_INSTANCE;
    private static Random rand;
    private static final boolean ON_NETWARE;
    private static final boolean ON_DOS;
    private static final boolean ON_WIN9X;
    private static final boolean ON_WINDOWS;
    static final int BUF_SIZE = 8192;
    public static final long FAT_FILE_TIMESTAMP_GRANULARITY = 2000L;
    public static final long UNIX_FILE_TIMESTAMP_GRANULARITY = 1000L;
    public static final long NTFS_FILE_TIMESTAMP_GRANULARITY = 1L;
    private Object cacheFromUriLock;
    private String cacheFromUriRequest;
    private String cacheFromUriResponse;
    private static final String NULL_PLACEHOLDER = "null";
    
    public static FileUtils newFileUtils() {
        return new FileUtils();
    }
    
    public static FileUtils getFileUtils() {
        return FileUtils.PRIMARY_INSTANCE;
    }
    
    protected FileUtils() {
        super();
        this.cacheFromUriLock = new Object();
        this.cacheFromUriRequest = null;
        this.cacheFromUriResponse = null;
    }
    
    public URL getFileURL(final File file) throws MalformedURLException {
        return new URL(file.toURI().toASCIIString());
    }
    
    public void copyFile(final String sourceFile, final String destFile) throws IOException {
        this.copyFile(new File(sourceFile), new File(destFile), null, false, false);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final FilterSetCollection filters) throws IOException {
        this.copyFile(new File(sourceFile), new File(destFile), filters, false, false);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final FilterSetCollection filters, final boolean overwrite) throws IOException {
        this.copyFile(new File(sourceFile), new File(destFile), filters, overwrite, false);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final FilterSetCollection filters, final boolean overwrite, final boolean preserveLastModified) throws IOException {
        this.copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final FilterSetCollection filters, final boolean overwrite, final boolean preserveLastModified, final String encoding) throws IOException {
        this.copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified, encoding);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final String encoding, final Project project) throws IOException {
        this.copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, encoding, project);
    }
    
    public void copyFile(final String sourceFile, final String destFile, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final String inputEncoding, final String outputEncoding, final Project project) throws IOException {
        this.copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, inputEncoding, outputEncoding, project);
    }
    
    public void copyFile(final File sourceFile, final File destFile) throws IOException {
        this.copyFile(sourceFile, destFile, null, false, false);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters) throws IOException {
        this.copyFile(sourceFile, destFile, filters, false, false);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters, final boolean overwrite) throws IOException {
        this.copyFile(sourceFile, destFile, filters, overwrite, false);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters, final boolean overwrite, final boolean preserveLastModified) throws IOException {
        this.copyFile(sourceFile, destFile, filters, overwrite, preserveLastModified, null);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters, final boolean overwrite, final boolean preserveLastModified, final String encoding) throws IOException {
        this.copyFile(sourceFile, destFile, filters, null, overwrite, preserveLastModified, encoding, null);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final String encoding, final Project project) throws IOException {
        this.copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, encoding, encoding, project);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final String inputEncoding, final String outputEncoding, final Project project) throws IOException {
        this.copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, false, inputEncoding, outputEncoding, project);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final boolean append, final String inputEncoding, final String outputEncoding, final Project project) throws IOException {
        this.copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, false);
    }
    
    public void copyFile(final File sourceFile, final File destFile, final FilterSetCollection filters, final Vector filterChains, final boolean overwrite, final boolean preserveLastModified, final boolean append, final String inputEncoding, final String outputEncoding, final Project project, final boolean force) throws IOException {
        ResourceUtils.copyResource(new FileResource(sourceFile), new FileResource(destFile), filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, force);
    }
    
    public void setFileLastModified(final File file, final long time) {
        ResourceUtils.setLastModified(new FileResource(file), time);
    }
    
    public File resolveFile(File file, String filename) {
        if (!isAbsolutePath(filename)) {
            final char sep = File.separatorChar;
            filename = filename.replace('/', sep).replace('\\', sep);
            if (isContextRelativePath(filename)) {
                file = null;
                final String udir = System.getProperty("user.dir");
                if (filename.charAt(0) == sep && udir.charAt(0) == sep) {
                    filename = this.dissect(udir)[0] + filename.substring(1);
                }
            }
            filename = new File(file, filename).getAbsolutePath();
        }
        return this.normalize(filename);
    }
    
    public static boolean isContextRelativePath(String filename) {
        if ((!FileUtils.ON_DOS && !FileUtils.ON_NETWARE) || filename.length() == 0) {
            return false;
        }
        final char sep = File.separatorChar;
        filename = filename.replace('/', sep).replace('\\', sep);
        final char c = filename.charAt(0);
        final int len = filename.length();
        return (c == sep && (len == 1 || filename.charAt(1) != sep)) || (Character.isLetter(c) && len > 1 && filename.indexOf(58) == 1 && (len == 2 || filename.charAt(2) != sep));
    }
    
    public static boolean isAbsolutePath(String filename) {
        final int len = filename.length();
        if (len == 0) {
            return false;
        }
        final char sep = File.separatorChar;
        filename = filename.replace('/', sep).replace('\\', sep);
        final char c = filename.charAt(0);
        if (!FileUtils.ON_DOS && !FileUtils.ON_NETWARE) {
            return c == sep;
        }
        if (c != sep) {
            final int colon = filename.indexOf(58);
            return (Character.isLetter(c) && colon == 1 && filename.length() > 2 && filename.charAt(2) == sep) || (FileUtils.ON_NETWARE && colon > 0);
        }
        if (!FileUtils.ON_DOS || len <= 4 || filename.charAt(1) != sep) {
            return false;
        }
        final int nextsep = filename.indexOf(sep, 2);
        return nextsep > 2 && nextsep + 1 < len;
    }
    
    public static String translatePath(final String toProcess) {
        if (toProcess == null || toProcess.length() == 0) {
            return "";
        }
        final StringBuffer path = new StringBuffer(toProcess.length() + 50);
        final PathTokenizer tokenizer = new PathTokenizer(toProcess);
        while (tokenizer.hasMoreTokens()) {
            String pathComponent = tokenizer.nextToken();
            pathComponent = pathComponent.replace('/', File.separatorChar);
            pathComponent = pathComponent.replace('\\', File.separatorChar);
            if (path.length() != 0) {
                path.append(File.pathSeparatorChar);
            }
            path.append(pathComponent);
        }
        return path.toString();
    }
    
    public File normalize(final String path) {
        final Stack s = new Stack();
        final String[] dissect = this.dissect(path);
        s.push(dissect[0]);
        final StringTokenizer tok = new StringTokenizer(dissect[1], File.separator);
        while (tok.hasMoreTokens()) {
            final String thisToken = tok.nextToken();
            if (".".equals(thisToken)) {
                continue;
            }
            if ("..".equals(thisToken)) {
                if (s.size() < 2) {
                    return new File(path);
                }
                s.pop();
            }
            else {
                s.push(thisToken);
            }
        }
        final StringBuffer sb = new StringBuffer();
        for (int size = s.size(), i = 0; i < size; ++i) {
            if (i > 1) {
                sb.append(File.separatorChar);
            }
            sb.append(s.elementAt(i));
        }
        return new File(sb.toString());
    }
    
    public String[] dissect(String path) {
        final char sep = File.separatorChar;
        path = path.replace('/', sep).replace('\\', sep);
        if (!isAbsolutePath(path)) {
            throw new BuildException(path + " is not an absolute path");
        }
        String root = null;
        final int colon = path.indexOf(58);
        if (colon > 0 && (FileUtils.ON_DOS || FileUtils.ON_NETWARE)) {
            int next = colon + 1;
            root = path.substring(0, next);
            final char[] ca = path.toCharArray();
            root += sep;
            next = ((ca[next] == sep) ? (next + 1) : next);
            final StringBuffer sbPath = new StringBuffer();
            for (int i = next; i < ca.length; ++i) {
                if (ca[i] != sep || ca[i - 1] != sep) {
                    sbPath.append(ca[i]);
                }
            }
            path = sbPath.toString();
        }
        else if (path.length() > 1 && path.charAt(1) == sep) {
            int nextsep = path.indexOf(sep, 2);
            nextsep = path.indexOf(sep, nextsep + 1);
            root = ((nextsep > 2) ? path.substring(0, nextsep + 1) : path);
            path = path.substring(root.length());
        }
        else {
            root = File.separator;
            path = path.substring(1);
        }
        return new String[] { root, path };
    }
    
    public String toVMSPath(final File f) {
        final String path = this.normalize(f.getAbsolutePath()).getPath();
        final String name = f.getName();
        final boolean isAbsolute = path.charAt(0) == File.separatorChar;
        final boolean isDirectory = f.isDirectory() && !name.regionMatches(true, name.length() - 4, ".DIR", 0, 4);
        String device = null;
        StringBuffer directory = null;
        String file = null;
        int index = 0;
        if (isAbsolute) {
            index = path.indexOf(File.separatorChar, 1);
            if (index == -1) {
                return path.substring(1) + ":[000000]";
            }
            device = path.substring(1, index++);
        }
        if (isDirectory) {
            directory = new StringBuffer(path.substring(index).replace(File.separatorChar, '.'));
        }
        else {
            final int dirEnd = path.lastIndexOf(File.separatorChar, path.length());
            if (dirEnd == -1 || dirEnd < index) {
                file = path.substring(index);
            }
            else {
                directory = new StringBuffer(path.substring(index, dirEnd).replace(File.separatorChar, '.'));
                index = dirEnd + 1;
                if (path.length() > index) {
                    file = path.substring(index);
                }
            }
        }
        if (!isAbsolute && directory != null) {
            directory.insert(0, '.');
        }
        final String osPath = ((device != null) ? (device + ":") : "") + ((directory != null) ? ("[" + (Object)directory + "]") : "") + ((file != null) ? file : "");
        return osPath;
    }
    
    public File createTempFile(final String prefix, final String suffix, final File parentDir) {
        return this.createTempFile(prefix, suffix, parentDir, false, false);
    }
    
    public File createTempFile(String prefix, String suffix, final File parentDir, final boolean deleteOnExit, final boolean createFile) {
        File result = null;
        final String parent = (parentDir == null) ? System.getProperty("java.io.tmpdir") : parentDir.getPath();
        if (prefix == null) {
            prefix = "null";
        }
        if (suffix == null) {
            suffix = "null";
        }
        Label_0177: {
            if (createFile) {
                try {
                    result = File.createTempFile(prefix, suffix, new File(parent));
                    break Label_0177;
                }
                catch (IOException e) {
                    throw new BuildException("Could not create tempfile in " + parent, e);
                }
            }
            final DecimalFormat fmt = new DecimalFormat("#####");
            synchronized (FileUtils.rand) {
                do {
                    result = new File(parent, prefix + fmt.format(FileUtils.rand.nextInt(Integer.MAX_VALUE)) + suffix);
                } while (result.exists());
            }
        }
        if (deleteOnExit) {
            result.deleteOnExit();
        }
        return result;
    }
    
    public File createTempFile(final String prefix, final String suffix, final File parentDir, final boolean deleteOnExit) {
        return this.createTempFile(prefix, suffix, parentDir, deleteOnExit, false);
    }
    
    public boolean contentEquals(final File f1, final File f2) throws IOException {
        return this.contentEquals(f1, f2, false);
    }
    
    public boolean contentEquals(final File f1, final File f2, final boolean textfile) throws IOException {
        return ResourceUtils.contentEquals(new FileResource(f1), new FileResource(f2), textfile);
    }
    
    public File getParentFile(final File f) {
        return (f == null) ? null : f.getParentFile();
    }
    
    public static String readFully(final Reader rdr) throws IOException {
        return readFully(rdr, 8192);
    }
    
    public static String readFully(final Reader rdr, final int bufferSize) throws IOException {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0");
        }
        final char[] buffer = new char[bufferSize];
        int bufferLength = 0;
        StringBuffer textBuffer = null;
        while (bufferLength != -1) {
            bufferLength = rdr.read(buffer);
            if (bufferLength > 0) {
                textBuffer = ((textBuffer == null) ? new StringBuffer() : textBuffer);
                textBuffer.append(new String(buffer, 0, bufferLength));
            }
        }
        return (textBuffer == null) ? null : textBuffer.toString();
    }
    
    public static String safeReadFully(final Reader reader) throws IOException {
        final String ret = readFully(reader);
        return (ret == null) ? "" : ret;
    }
    
    public boolean createNewFile(final File f) throws IOException {
        return f.createNewFile();
    }
    
    public boolean createNewFile(final File f, final boolean mkdirs) throws IOException {
        final File parent = f.getParentFile();
        if (mkdirs && !parent.exists()) {
            parent.mkdirs();
        }
        return f.createNewFile();
    }
    
    public boolean isSymbolicLink(final File parent, final String name) throws IOException {
        final SymbolicLinkUtils u = SymbolicLinkUtils.getSymbolicLinkUtils();
        if (parent == null) {
            return u.isSymbolicLink(name);
        }
        return u.isSymbolicLink(parent, name);
    }
    
    public String removeLeadingPath(final File leading, final File path) {
        String l = this.normalize(leading.getAbsolutePath()).getAbsolutePath();
        final String p = this.normalize(path.getAbsolutePath()).getAbsolutePath();
        if (l.equals(p)) {
            return "";
        }
        if (!l.endsWith(File.separator)) {
            l += File.separator;
        }
        return p.startsWith(l) ? p.substring(l.length()) : p;
    }
    
    public boolean isLeadingPath(final File leading, final File path) {
        String l = this.normalize(leading.getAbsolutePath()).getAbsolutePath();
        final String p = this.normalize(path.getAbsolutePath()).getAbsolutePath();
        if (l.equals(p)) {
            return true;
        }
        if (!l.endsWith(File.separator)) {
            l += File.separator;
        }
        return p.startsWith(l);
    }
    
    public String toURI(final String path) {
        return new File(path).toURI().toASCIIString();
    }
    
    public String fromURI(final String uri) {
        synchronized (this.cacheFromUriLock) {
            if (uri.equals(this.cacheFromUriRequest)) {
                return this.cacheFromUriResponse;
            }
            final String path = Locator.fromURI(uri);
            final String ret = isAbsolutePath(path) ? this.normalize(path).getAbsolutePath() : path;
            this.cacheFromUriRequest = uri;
            return this.cacheFromUriResponse = ret;
        }
    }
    
    public boolean fileNameEquals(final File f1, final File f2) {
        return this.normalize(f1.getAbsolutePath()).getAbsolutePath().equals(this.normalize(f2.getAbsolutePath()).getAbsolutePath());
    }
    
    public boolean areSame(final File f1, final File f2) throws IOException {
        if (f1 == null && f2 == null) {
            return true;
        }
        if (f1 == null || f2 == null) {
            return false;
        }
        final File f1Normalized = this.normalize(f1.getAbsolutePath());
        final File f2Normalized = this.normalize(f2.getAbsolutePath());
        return f1Normalized.equals(f2Normalized) || f1Normalized.getCanonicalFile().equals(f2Normalized.getCanonicalFile());
    }
    
    public void rename(File from, File to) throws IOException {
        from = this.normalize(from.getAbsolutePath()).getCanonicalFile();
        to = this.normalize(to.getAbsolutePath());
        if (!from.exists()) {
            System.err.println("Cannot rename nonexistent file " + from);
            return;
        }
        if (from.getAbsolutePath().equals(to.getAbsolutePath())) {
            System.err.println("Rename of " + from + " to " + to + " is a no-op.");
            return;
        }
        if (to.exists() && !this.areSame(from, to) && !this.tryHardToDelete(to)) {
            throw new IOException("Failed to delete " + to + " while trying to rename " + from);
        }
        final File parent = to.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent + " while trying to rename " + from);
        }
        if (!from.renameTo(to)) {
            this.copyFile(from, to);
            if (!this.tryHardToDelete(from)) {
                throw new IOException("Failed to delete " + from + " while trying to rename it.");
            }
        }
    }
    
    public long getFileTimestampGranularity() {
        if (FileUtils.ON_WIN9X) {
            return 2000L;
        }
        if (FileUtils.ON_WINDOWS) {
            return 1L;
        }
        if (FileUtils.ON_DOS) {
            return 2000L;
        }
        return 1000L;
    }
    
    public boolean hasErrorInCase(File localFile) {
        localFile = this.normalize(localFile.getAbsolutePath());
        if (!localFile.exists()) {
            return false;
        }
        final String localFileName = localFile.getName();
        final FilenameFilter ff = new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                return name.equalsIgnoreCase(localFileName) && !name.equals(localFileName);
            }
        };
        final String[] names = localFile.getParentFile().list(ff);
        return names != null && names.length == 1;
    }
    
    public boolean isUpToDate(final File source, final File dest, final long granularity) {
        if (!dest.exists()) {
            return false;
        }
        final long sourceTime = source.lastModified();
        final long destTime = dest.lastModified();
        return this.isUpToDate(sourceTime, destTime, granularity);
    }
    
    public boolean isUpToDate(final File source, final File dest) {
        return this.isUpToDate(source, dest, this.getFileTimestampGranularity());
    }
    
    public boolean isUpToDate(final long sourceTime, final long destTime, final long granularity) {
        return destTime != -1L && destTime >= sourceTime + granularity;
    }
    
    public boolean isUpToDate(final long sourceTime, final long destTime) {
        return this.isUpToDate(sourceTime, destTime, this.getFileTimestampGranularity());
    }
    
    public static void close(final Writer device) {
        if (null != device) {
            try {
                device.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public static void close(final Reader device) {
        if (null != device) {
            try {
                device.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public static void close(final OutputStream device) {
        if (null != device) {
            try {
                device.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public static void close(final InputStream device) {
        if (null != device) {
            try {
                device.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public static void close(final Channel device) {
        if (null != device) {
            try {
                device.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public static void close(final URLConnection conn) {
        if (conn != null) {
            try {
                if (conn instanceof JarURLConnection) {
                    final JarURLConnection juc = (JarURLConnection)conn;
                    JarFile jf = juc.getJarFile();
                    jf.close();
                    jf = null;
                }
                else if (conn instanceof HttpURLConnection) {
                    ((HttpURLConnection)conn).disconnect();
                }
            }
            catch (IOException ex) {}
        }
    }
    
    public static void delete(final File file) {
        if (file != null) {
            file.delete();
        }
    }
    
    public boolean tryHardToDelete(final File f) {
        return this.tryHardToDelete(f, FileUtils.ON_WINDOWS);
    }
    
    public boolean tryHardToDelete(final File f, final boolean runGC) {
        if (!f.delete()) {
            if (runGC) {
                System.gc();
            }
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException ex) {}
            return f.delete();
        }
        return true;
    }
    
    public static String getRelativePath(final File fromFile, final File toFile) throws Exception {
        final String fromPath = fromFile.getCanonicalPath();
        final String toPath = toFile.getCanonicalPath();
        final String[] fromPathStack = getPathStack(fromPath);
        final String[] toPathStack = getPathStack(toPath);
        if (0 >= toPathStack.length || 0 >= fromPathStack.length) {
            return getPath(Arrays.asList(toPathStack));
        }
        if (!fromPathStack[0].equals(toPathStack[0])) {
            return getPath(Arrays.asList(toPathStack));
        }
        int minLength;
        int same;
        for (minLength = Math.min(fromPathStack.length, toPathStack.length), same = 1; same < minLength && fromPathStack[same].equals(toPathStack[same]); ++same) {}
        final List relativePathStack = new ArrayList();
        for (int i = same; i < fromPathStack.length; ++i) {
            relativePathStack.add("..");
        }
        for (int i = same; i < toPathStack.length; ++i) {
            relativePathStack.add(toPathStack[i]);
        }
        return getPath(relativePathStack);
    }
    
    public static String[] getPathStack(final String path) {
        final String normalizedPath = path.replace(File.separatorChar, '/');
        return normalizedPath.split("/");
    }
    
    public static String getPath(final List pathStack) {
        return getPath(pathStack, '/');
    }
    
    public static String getPath(final List pathStack, final char separatorChar) {
        final StringBuffer buffer = new StringBuffer();
        final Iterator iter = pathStack.iterator();
        if (iter.hasNext()) {
            buffer.append(iter.next());
        }
        while (iter.hasNext()) {
            buffer.append(separatorChar);
            buffer.append(iter.next());
        }
        return buffer.toString();
    }
    
    public String getDefaultEncoding() {
        final InputStreamReader is = new InputStreamReader(new InputStream() {
            public int read() {
                return -1;
            }
        });
        try {
            return is.getEncoding();
        }
        finally {
            close(is);
        }
    }
    
    static {
        PRIMARY_INSTANCE = new FileUtils();
        FileUtils.rand = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
        ON_NETWARE = Os.isFamily("netware");
        ON_DOS = Os.isFamily("dos");
        ON_WIN9X = Os.isFamily("win9x");
        ON_WINDOWS = Os.isFamily("windows");
    }
}
