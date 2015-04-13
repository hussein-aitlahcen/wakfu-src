package com.ankamagames.framework.kernel.core.maths.motion;

import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public interface Trajectory
{
    void setInitialTime(long p0);
    
    long getInitialTime();
    
    void setFinalTime(long p0);
    
    long getFinalTime();
    
    Vector3 getInitialPosition();
    
    void setInitialPosition(Vector3 p0);
    
    Vector3 getInitialVelocity();
    
    void setInitialVelocity(Vector3 p0);
    
    Vector3 getFinalPosition();
    
    void setFinalPosition(Vector3 p0);
    
    Vector3 getFinalVelocity();
    
    void setFinalVelocity(Vector3 p0);
    
    Vector3 getPosition(long p0);
    
    long getDuration();
    
    List<int[]> integerSteps(int p0);
}
