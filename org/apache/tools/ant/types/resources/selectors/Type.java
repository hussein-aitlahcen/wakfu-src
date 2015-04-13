package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

public class Type implements ResourceSelector
{
    private static final String FILE_ATTR = "file";
    private static final String DIR_ATTR = "dir";
    private static final String ANY_ATTR = "any";
    public static final Type FILE;
    public static final Type DIR;
    public static final Type ANY;
    private FileDir type;
    
    public Type() {
        super();
        this.type = null;
    }
    
    public Type(final FileDir fd) {
        super();
        this.type = null;
        this.setType(fd);
    }
    
    public void setType(final FileDir fd) {
        this.type = fd;
    }
    
    public boolean isSelected(final Resource r) {
        if (this.type == null) {
            throw new BuildException("The type attribute is required.");
        }
        final int i = this.type.getIndex();
        return i == 2 || (r.isDirectory() ? (i == 1) : (i == 0));
    }
    
    static {
        FILE = new Type(new FileDir("file"));
        DIR = new Type(new FileDir("dir"));
        ANY = new Type(new FileDir("any"));
    }
    
    public static class FileDir extends EnumeratedAttribute
    {
        private static final String[] VALUES;
        
        public FileDir() {
            super();
        }
        
        public FileDir(final String value) {
            super();
            this.setValue(value);
        }
        
        public String[] getValues() {
            return FileDir.VALUES;
        }
        
        static {
            VALUES = new String[] { "file", "dir", "any" };
        }
    }
}
