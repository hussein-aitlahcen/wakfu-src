package com.ankamagames.baseImpl.graphics.alea.adviser;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public interface Adviser
{
    WorldPositionable getTarget();
    
    void setTarget(WorldPositionable p0);
    
    int getXOffset();
    
    void setXOffset(int p0);
    
    int getYOffset();
    
    void setYOffset(int p0);
    
    float getWorldX();
    
    float getWorldY();
    
    float getAltitude();
    
    void setPosition(float p0, float p1, float p2, float p3);
    
    int getDuration();
    
    boolean isAlive();
    
    void process(AleaWorldScene p0, int p1);
    
    void process(int p0);
    
    int getId();
    
    void setId(int p0);
    
    int getTypeId();
    
    void setTypeId(int p0);
    
    Entity getEntity();
    
    void cleanUp();
    
    boolean needsToPrecomputeZoom();
}
