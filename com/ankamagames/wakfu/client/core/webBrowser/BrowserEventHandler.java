package com.ankamagames.wakfu.client.core.webBrowser;

public interface BrowserEventHandler
{
    void initialize();
    
    void clean();
    
    String getUrl();
    
    boolean invokeFunction(String p0, Object[] p1);
    
    void start(SWFBrowser p0);
    
    void onLoad();
}
