package com.ankamagames.baseImpl.graphics.alea.display.lights.lightMap;

import com.ankamagames.framework.kernel.core.maths.*;

public interface LightMap
{
    Rect getBounds();
    
    void setBounds(int p0, int p1, int p2, int p3);
    
    void addColors(int p0, int p1, int p2, float p3, float p4, float p5, float p6, float p7, float p8);
}
