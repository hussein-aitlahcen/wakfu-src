package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public interface BasicFighter extends Poolable, Releasable, EffectUser, FightEffectUser, FightObstacle, MovementEffectUser
{
    void release();
    
    boolean isOnFight();
    
    long getId();
    
    void onSpecialFighterEvent(SpecialEvent p0);
    
    byte[] serializeFighterDatas();
    
    void unserializeFighterDatas(byte[] p0);
    
    short getLevel();
    
    void onJoinFight(BasicFight p0);
    
    void onLeaveFight();
    
    BasicFight getCurrentFight();
    
    byte getType();
}
