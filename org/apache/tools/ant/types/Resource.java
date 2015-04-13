package org.apache.tools.ant.types;

import java.math.*;
import java.io.*;
import java.util.*;
import org.apache.tools.ant.types.resources.*;

public class Resource extends DataType implements Comparable<Resource>, ResourceCollection
{
    public static final long UNKNOWN_SIZE = -1L;
    public static final long UNKNOWN_DATETIME = 0L;
    protected static final int MAGIC;
    private static final int NULL_NAME;
    private String name;
    private Boolean exists;
    private Long lastmodified;
    private Boolean directory;
    private Long size;
    
    protected static int getMagicNumber(final byte[] seed) {
        return new BigInteger(seed).intValue();
    }
    
    public Resource() {
        super();
        this.name = null;
        this.exists = null;
        this.lastmodified = null;
        this.directory = null;
        this.size = null;
    }
    
    public Resource(final String name) {
        this(name, false, 0L, false);
    }
    
    public Resource(final String name, final boolean exists, final long lastmodified) {
        this(name, exists, lastmodified, false);
    }
    
    public Resource(final String name, final boolean exists, final long lastmodified, final boolean directory) {
        this(name, exists, lastmodified, directory, -1L);
    }
    
    public Resource(final String name, final boolean exists, final long lastmodified, final boolean directory, final long size) {
        super();
        this.name = null;
        this.exists = null;
        this.lastmodified = null;
        this.directory = null;
        this.size = null;
        this.setName(this.name = name);
        this.setExists(exists);
        this.setLastModified(lastmodified);
        this.setDirectory(directory);
        this.setSize(size);
    }
    
    public String getName() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).getName() : this.name;
    }
    
    public void setName(final String name) {
        this.checkAttributesAllowed();
        this.name = name;
    }
    
    public boolean isExists() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).isExists();
        }
        return this.exists == null || this.exists;
    }
    
    public void setExists(final boolean exists) {
        this.checkAttributesAllowed();
        this.exists = (exists ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public long getLastModified() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getLastModified();
        }
        if (!this.isExists() || this.lastmodified == null) {
            return 0L;
        }
        final long result = this.lastmodified;
        return (result < 0L) ? 0L : result;
    }
    
    public void setLastModified(final long lastmodified) {
        this.checkAttributesAllowed();
        this.lastmodified = new Long(lastmodified);
    }
    
    public boolean isDirectory() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).isDirectory();
        }
        return this.directory != null && this.directory;
    }
    
    public void setDirectory(final boolean directory) {
        this.checkAttributesAllowed();
        this.directory = (directory ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public void setSize(final long size) {
        this.checkAttributesAllowed();
        this.size = new Long((size > -1L) ? size : -1L);
    }
    
    public long getSize() {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getSize();
        }
        return this.isExists() ? ((this.size != null) ? this.size : -1L) : 0L;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("CloneNotSupportedException for a Resource caught. Derived classes must support cloning.");
        }
    }
    
    public int compareTo(final Resource other) {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).compareTo(other);
        }
        return this.toString().compareTo(other.toString());
    }
    
    public boolean equals(final Object other) {
        if (this.isReference()) {
            return this.getCheckedRef().equals(other);
        }
        return other.getClass().equals(this.getClass()) && this.compareTo((Resource)other) == 0;
    }
    
    public int hashCode() {
        if (this.isReference()) {
            return this.getCheckedRef().hashCode();
        }
        final String name = this.getName();
        return Resource.MAGIC * ((name == null) ? Resource.NULL_NAME : name.hashCode());
    }
    
    public InputStream getInputStream() throws IOException {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getInputStream();
        }
        throw new UnsupportedOperationException();
    }
    
    public OutputStream getOutputStream() throws IOException {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getOutputStream();
        }
        throw new UnsupportedOperationException();
    }
    
    public Iterator<Resource> iterator() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).iterator() : new Iterator<Resource>() {
            private boolean done = false;
            
            public boolean hasNext() {
                return !this.done;
            }
            
            public Resource next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return Resource.this;
            }
            
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public int size() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).size() : 1;
    }
    
    public boolean isFilesystemOnly() {
        return (this.isReference() && ((Resource)this.getCheckedRef()).isFilesystemOnly()) || this.as(FileProvider.class) != null;
    }
    
    public String toString() {
        if (this.isReference()) {
            return this.getCheckedRef().toString();
        }
        final String n = this.getName();
        return (n == null) ? "(anonymous)" : n;
    }
    
    public final String toLongString() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).toLongString() : (this.getDataTypeName() + " \"" + this.toString() + '\"');
    }
    
    public void setRefid(final Reference r) {
        if (this.name != null || this.exists != null || this.lastmodified != null || this.directory != null || this.size != null) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    public <T> T as(final Class<T> clazz) {
        return clazz.isAssignableFrom(this.getClass()) ? clazz.cast(this) : null;
    }
    
    static {
        MAGIC = getMagicNumber("Resource".getBytes());
        NULL_NAME = getMagicNumber("null name".getBytes());
    }
}
