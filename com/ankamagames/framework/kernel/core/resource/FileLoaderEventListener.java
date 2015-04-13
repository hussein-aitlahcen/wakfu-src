package com.ankamagames.framework.kernel.core.resource;

public interface FileLoaderEventListener
{
    void onLoadStart(String p0);
    
    void onLoadComplete(String p0);
    
    void onLoadError(String p0, String p1);
}
