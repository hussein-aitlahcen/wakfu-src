package com.ankamagames.wakfu.common.datas.specific;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;

public interface Carrier
{
    boolean canCarry(CarryTarget p0);
    
    boolean carry(CarryTarget p0);
    
    boolean isCarrying();
    
    @Nullable
    CarryTarget getCurrentCarryTarget();
    
    boolean uncarryTo(Point3 p0);
    
    boolean uncarryTo_effect(Point3 p0);
    
    void forceUncarry();
    
    int getWorldCellX();
    
    int getWorldCellY();
    
    short getWorldCellAltitude();
    
    BasicCharacterInfo getController();
}
