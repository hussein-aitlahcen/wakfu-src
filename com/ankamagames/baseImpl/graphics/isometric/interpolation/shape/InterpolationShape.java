package com.ankamagames.baseImpl.graphics.isometric.interpolation.shape;

import com.ankamagames.framework.kernel.core.maths.*;

public interface InterpolationShape
{
    public static final float EPSILON = 0.01f;
    
    void setScale(float p0);
    
    Point2 intersectLine(float p0, float p1);
}
