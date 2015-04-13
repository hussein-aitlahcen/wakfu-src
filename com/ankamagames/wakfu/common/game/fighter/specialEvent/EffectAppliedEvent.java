package com.ankamagames.wakfu.common.game.fighter.specialEvent;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;

public class EffectAppliedEvent implements SpecialEvent
{
    private WakfuRunningEffect m_effect;
    
    @Override
    public int getId() {
        return 1000;
    }
    
    public EffectAppliedEvent(final WakfuRunningEffect effect) {
        super();
        this.m_effect = effect;
    }
    
    public WakfuRunningEffect getEffect() {
        return this.m_effect;
    }
}
