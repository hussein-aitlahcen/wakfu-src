package com.ankamagames.framework.graphics.engine.fadeManager;

public interface FadeListener
{
    void onFadeInStart();
    
    void onFadeOutStart();
    
    void onFadeInEnd();
    
    void onFadeOutEnd();
}
