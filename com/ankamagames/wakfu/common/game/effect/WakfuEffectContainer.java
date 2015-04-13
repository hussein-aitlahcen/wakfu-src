package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public interface WakfuEffectContainer extends EffectContainer<WakfuEffect>
{
    short getLevel();
    
    short getAggroWeight();
    
    short getAllyEfficacity();
    
    short getFoeEfficacity();
}
