package com.ankamagames.baseImpl.graphics.opengl;

public interface GLApplicationUIEventListener
{
    void onUIClosing();
    
    void onUIClosed();
    
    void onUIIconified(boolean p0);
    
    void onUIActivated(boolean p0);
    
    void onUIInitializationError(Object p0, String p1);
    
    void onUIResolutionChanged(ApplicationResolution p0);
}
