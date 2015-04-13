package org.apache.tools.ant;

import java.io.*;

public interface FileScanner
{
    void addDefaultExcludes();
    
    File getBasedir();
    
    String[] getExcludedDirectories();
    
    String[] getExcludedFiles();
    
    String[] getIncludedDirectories();
    
    String[] getIncludedFiles();
    
    String[] getNotIncludedDirectories();
    
    String[] getNotIncludedFiles();
    
    void scan() throws IllegalStateException;
    
    void setBasedir(String p0);
    
    void setBasedir(File p0);
    
    void setExcludes(String[] p0);
    
    void setIncludes(String[] p0);
    
    void setCaseSensitive(boolean p0);
}
