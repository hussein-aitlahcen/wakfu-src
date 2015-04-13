package org.apache.tools.ant.types.selectors;

public interface SelectorScanner
{
    void setSelectors(FileSelector[] p0);
    
    String[] getDeselectedDirectories();
    
    String[] getDeselectedFiles();
}
