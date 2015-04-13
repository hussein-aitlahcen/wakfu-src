package com.ankamagames.framework.kernel.core.maths.motion;

import com.ankamagames.framework.kernel.core.maths.*;

public interface Spline
{
    Vector3 getPosition(long p0);
    
    Vector3 getInitialPosition();
    
    Vector3 getFinalPosition();
    
    long getFinalTime();
    
    long getInitialTime();
    
    Vector3 getNextPositionInDifferentCell(long p0);
}
