package org.apache.tools.ant.util;

public interface FileNameMapper
{
    void setFrom(String p0);
    
    void setTo(String p0);
    
    String[] mapFileName(String p0);
}
