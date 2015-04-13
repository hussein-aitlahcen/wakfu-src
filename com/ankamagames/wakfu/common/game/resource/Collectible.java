package com.ankamagames.wakfu.common.game.resource;

import com.ankamagames.framework.kernel.core.maths.*;

public interface Collectible
{
    Point3 getPosition();
    
    int getFamilyId();
    
    short getInstanceId();
}
