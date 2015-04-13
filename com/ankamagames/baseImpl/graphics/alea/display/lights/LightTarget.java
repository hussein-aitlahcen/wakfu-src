package com.ankamagames.baseImpl.graphics.alea.display.lights;

public interface LightTarget
{
    void onLightAttached(IsoLightSource p0);
    
    void onLightDetached(IsoLightSource p0);
    
    void onDestroy();
}
