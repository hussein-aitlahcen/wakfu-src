package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.*;
import java.io.*;
import org.apache.tools.ant.*;

public class FileResource extends Resource implements Touchable, FileProvider, ResourceFactory, Appendable
{
    private static final FileUtils FILE_UTILS;
    private static final int NULL_FILE;
    private File file;
    private File baseDir;
    
    public FileResource() {
        super();
    }
    
    public FileResource(final File b, final String name) {
        super();
        this.baseDir = b;
        this.file = FileResource.FILE_UTILS.resolveFile(b, name);
    }
    
    public FileResource(final File f) {
        super();
        this.setFile(f);
    }
    
    public FileResource(final Project p, final File f) {
        this(f);
        this.setProject(p);
    }
    
    public FileResource(final Project p, final String s) {
        this(p, p.resolveFile(s));
    }
    
    public void setFile(final File f) {
        this.checkAttributesAllowed();
        this.file = f;
        if (f != null && (this.getBaseDir() == null || !FileResource.FILE_UTILS.isLeadingPath(this.getBaseDir(), f))) {
            this.setBaseDir(f.getParentFile());
        }
    }
    
    public File getFile() {
        if (this.isReference()) {
            return ((FileResource)this.getCheckedRef()).getFile();
        }
        this.dieOnCircularReference();
        synchronized (this) {
            if (this.file == null) {
                final File d = this.getBaseDir();
                final String n = super.getName();
                if (n != null) {
                    this.setFile(FileResource.FILE_UTILS.resolveFile(d, n));
                }
            }
        }
        return this.file;
    }
    
    public void setBaseDir(final File b) {
        this.checkAttributesAllowed();
        this.baseDir = b;
    }
    
    public File getBaseDir() {
        if (this.isReference()) {
            return ((FileResource)this.getCheckedRef()).getBaseDir();
        }
        this.dieOnCircularReference();
        return this.baseDir;
    }
    
    public void setRefid(final Reference r) {
        if (this.file != null || this.baseDir != null) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    public String getName() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getName();
        }
        final File b = this.getBaseDir();
        return (b == null) ? this.getNotNullFile().getName() : FileResource.FILE_UTILS.removeLeadingPath(b, this.getNotNullFile());
    }
    
    public boolean isExists() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).isExists() : this.getNotNullFile().exists();
    }
    
    public long getLastModified() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).getLastModified() : this.getNotNullFile().lastModified();
    }
    
    public boolean isDirectory() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).isDirectory() : this.getNotNullFile().isDirectory();
    }
    
    public long getSize() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).getSize() : this.getNotNullFile().length();
    }
    
    public InputStream getInputStream() throws IOException {
        return this.isReference() ? ((Resource)this.getCheckedRef()).getInputStream() : new FileInputStream(this.getNotNullFile());
    }
    
    public OutputStream getOutputStream() throws IOException {
        if (this.isReference()) {
            return ((FileResource)this.getCheckedRef()).getOutputStream();
        }
        return this.getOutputStream(false);
    }
    
    public OutputStream getAppendOutputStream() throws IOException {
        if (this.isReference()) {
            return ((FileResource)this.getCheckedRef()).getAppendOutputStream();
        }
        return this.getOutputStream(true);
    }
    
    private OutputStream getOutputStream(final boolean append) throws IOException {
        final File f = this.getNotNullFile();
        if (f.exists()) {
            if (f.isFile() && !append) {
                f.delete();
            }
        }
        else {
            final File p = f.getParentFile();
            if (p != null && !p.exists()) {
                p.mkdirs();
            }
        }
        return append ? new FileOutputStream(f.getAbsolutePath(), true) : new FileOutputStream(f);
    }
    
    public int compareTo(final Resource another) {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).compareTo(another);
        }
        if (this.equals(another)) {
            return 0;
        }
        final FileProvider otherFP = another.as(FileProvider.class);
        if (otherFP == null) {
            return super.compareTo(another);
        }
        final File f = this.getFile();
        if (f == null) {
            return -1;
        }
        final File of = otherFP.getFile();
        if (of == null) {
            return 1;
        }
        return f.compareTo(of);
    }
    
    public boolean equals(final Object another) {
        if (this == another) {
            return true;
        }
        if (this.isReference()) {
            return this.getCheckedRef().equals(another);
        }
        if (!another.getClass().equals(this.getClass())) {
            return false;
        }
        final FileResource otherfr = (FileResource)another;
        return (this.getFile() == null) ? (otherfr.getFile() == null) : this.getFile().equals(otherfr.getFile());
    }
    
    public int hashCode() {
        if (this.isReference()) {
            return this.getCheckedRef().hashCode();
        }
        return FileResource.MAGIC * ((this.getFile() == null) ? FileResource.NULL_FILE : this.getFile().hashCode());
    }
    
    public String toString() {
        if (this.isReference()) {
            return this.getCheckedRef().toString();
        }
        if (this.file == null) {
            return "(unbound file resource)";
        }
        final String absolutePath = this.file.getAbsolutePath();
        return FileResource.FILE_UTILS.normalize(absolutePath).getAbsolutePath();
    }
    
    public boolean isFilesystemOnly() {
        if (this.isReference()) {
            return ((FileResource)this.getCheckedRef()).isFilesystemOnly();
        }
        this.dieOnCircularReference();
        return true;
    }
    
    public void touch(final long modTime) {
        if (this.isReference()) {
            ((FileResource)this.getCheckedRef()).touch(modTime);
            return;
        }
        if (!this.getNotNullFile().setLastModified(modTime)) {
            this.log("Failed to change file modification time", 1);
        }
    }
    
    protected File getNotNullFile() {
        if (this.getFile() == null) {
            throw new BuildException("file attribute is null!");
        }
        this.dieOnCircularReference();
        return this.getFile();
    }
    
    public Resource getResource(final String path) {
        final File newfile = FileResource.FILE_UTILS.resolveFile(this.getFile(), path);
        final FileResource fileResource = new FileResource(newfile);
        if (FileResource.FILE_UTILS.isLeadingPath(this.getBaseDir(), newfile)) {
            fileResource.setBaseDir(this.getBaseDir());
        }
        return fileResource;
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
        NULL_FILE = Resource.getMagicNumber("null file".getBytes());
    }
}
