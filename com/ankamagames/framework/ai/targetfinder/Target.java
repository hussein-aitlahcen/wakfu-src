package com.ankamagames.framework.ai.targetfinder;

import com.ankamagames.framework.ai.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface Target extends PhysicalRadiusOwner
{
    Direction8 getDirection();
    
    long getId();
    
    byte getHeight();
    
    boolean canBeTargeted();
}
