package com.ankamagames.framework.graphics.engine.fx;

import com.ankamagames.framework.graphics.engine.*;

public interface EffectBase
{
    int getId();
    
    void activate(boolean p0);
    
    boolean isActivated();
    
    void clear();
    
    void reset();
    
    void update(int p0);
    
    void render(Renderer p0);
    
    void stop(int p0);
}
