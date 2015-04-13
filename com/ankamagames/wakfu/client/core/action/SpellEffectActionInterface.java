package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface SpellEffectActionInterface
{
    int getEffectValue();
    
    byte getExecutionStatus();
    
    long getTargetId();
    
    long getCasterId();
    
    int getFightId();
    
    WakfuRunningEffect getRunningEffect();
    
    Point3 getPosition();
    
    Point3 getTargetCell();
    
    Point3 getCasterPosition();
    
    Point3 getBearerPosition();
    
    int getArmorLossValue();
    
    int getBarrierLossValue();
}
