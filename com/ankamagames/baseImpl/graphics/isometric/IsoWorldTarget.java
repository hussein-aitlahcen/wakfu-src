package com.ankamagames.baseImpl.graphics.isometric;

import com.ankamagames.framework.ai.targetfinder.*;

public interface IsoWorldTarget extends WorldPositionable
{
    void setWorldPosition(float p0, float p1);
    
    void setWorldPosition(float p0, float p1, float p2);
    
    float getEntityRenderRadius();
    
    boolean isVisible();
}
