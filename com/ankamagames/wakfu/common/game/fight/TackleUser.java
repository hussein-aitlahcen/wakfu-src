package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.framework.ai.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface TackleUser extends PhysicalRadiusOwner
{
    int getDodgeValue();
    
    int getTackleValue();
    
    boolean canTackle();
    
    boolean isActiveProperty(PropertyType p0);
    
    PartLocalisator getPartLocalisator();
    
    Point3 getPositionConst();
    
    short getLevel();
}
