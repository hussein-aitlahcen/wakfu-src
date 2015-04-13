package com.ankamagames.baseImpl.graphics.alea.display.lights;

import com.ankamagames.baseImpl.graphics.alea.display.lights.lightMap.*;

public interface LitSceneModifier extends LightMapModifier
{
    void update(int p0);
    
    int getPriority();
    
    boolean useless();
}
