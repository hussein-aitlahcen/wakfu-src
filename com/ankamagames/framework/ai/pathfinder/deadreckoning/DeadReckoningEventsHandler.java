package com.ankamagames.framework.ai.pathfinder.deadreckoning;

import com.ankamagames.framework.kernel.core.maths.motion.*;

public interface DeadReckoningEventsHandler
{
    void onGapThresholdReached(long p0, LinearTrajectory p1, LinearTrajectory p2);
    
    void onTrajectoryUpdate(long p0, Trajectory p1);
}
