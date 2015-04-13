package com.ankamagames.framework.kernel.core.resource;

public interface FileLoader
{
    void addFileLoaderEventListener(FileLoaderEventListener p0);
    
    void removeFileLoaderEventLstener(FileLoaderEventListener p0);
    
    void fireOnLoadStartEvent(String p0);
    
    void fireOnLoadCompleteEvent(String p0);
    
    void fireOnLoadErrorEvent(String p0, String p1);
}
