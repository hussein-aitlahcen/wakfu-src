package com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper;

import com.ankamagames.framework.graphics.engine.*;

public interface ColorHelper
{
    void applyImmediate(VertexBufferPCT p0, float[] p1, int p2);
    
    void applyDelayed(float[] p0);
}
