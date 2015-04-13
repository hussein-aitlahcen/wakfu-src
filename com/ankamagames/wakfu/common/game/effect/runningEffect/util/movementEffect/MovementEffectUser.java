package com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public interface MovementEffectUser extends EffectUser
{
    boolean isCarried();
    
    short getJumpCapacity();
    
    boolean isBlockingMovement();
}
