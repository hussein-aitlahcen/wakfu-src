package com.ankamagames.framework.video;

public interface VideoEventListener
{
    void onVideoEnd();
    
    void onVideoStopped();
    
    void onVideoOutput();
    
    void onBuffering(float p0);
}
