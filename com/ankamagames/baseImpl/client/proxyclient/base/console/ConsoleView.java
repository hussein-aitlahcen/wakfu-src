package com.ankamagames.baseImpl.client.proxyclient.base.console;

public interface ConsoleView
{
    void log(String p0);
    
    void customStyle(String p0);
    
    void trace(String p0);
    
    void err(String p0);
    
    void customTrace(String p0, int p1);
    
    void setPrompt(String p0);
}
