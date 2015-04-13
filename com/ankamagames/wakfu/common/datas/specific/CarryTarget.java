package com.ankamagames.wakfu.common.datas.specific;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface CarryTarget
{
    boolean canJumpFromCarrier();
    
    boolean canBeCarriedBy(Carrier p0);
    
    boolean isCarried();
    
    void onCarryEvent(Carrier p0);
    
    void onUncarryEvent();
    
    byte getHeight();
    
    @Nullable
    Carrier getCarrier();
    
    int getWorldCellX();
    
    int getWorldCellY();
    
    short getWorldCellAltitude();
    
    void setPosition(int p0, int p1, short p2);
    
    void setDirection(Direction8 p0);
    
    void onPositionChanged();
    
    boolean isBlockingMovement();
    
    Direction8 getDirection();
}
