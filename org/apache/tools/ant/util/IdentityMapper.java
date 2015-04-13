package org.apache.tools.ant.util;

public class IdentityMapper implements FileNameMapper
{
    public void setFrom(final String from) {
    }
    
    public void setTo(final String to) {
    }
    
    public String[] mapFileName(final String sourceFileName) {
        return new String[] { sourceFileName };
    }
}
