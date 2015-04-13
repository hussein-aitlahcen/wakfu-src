package org.apache.tools.ant.util;

public class MergingMapper implements FileNameMapper
{
    protected String[] mergedFile;
    
    public MergingMapper() {
        super();
        this.mergedFile = null;
    }
    
    public MergingMapper(final String to) {
        super();
        this.mergedFile = null;
        this.setTo(to);
    }
    
    public void setFrom(final String from) {
    }
    
    public void setTo(final String to) {
        this.mergedFile = new String[] { to };
    }
    
    public String[] mapFileName(final String sourceFileName) {
        return this.mergedFile;
    }
}
