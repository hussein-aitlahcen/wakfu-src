package com.ankamagames.wakfu.client.core.auth;

public interface ICEAuthListener
{
    void onToken(String p0);
    
    void onError();
}
