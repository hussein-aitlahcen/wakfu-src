package com.ankamagames.baseImpl.graphics.ui;

public interface UISceneEventListener
{
    void onSceneInitializationComplete(UIScene p0);
    
    void onProcess(UIScene p0, int p1);
    
    void onResize(UIScene p0, int p1, int p2);
}
