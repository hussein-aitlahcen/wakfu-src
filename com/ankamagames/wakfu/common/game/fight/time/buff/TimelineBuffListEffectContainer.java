package com.ankamagames.wakfu.common.game.fight.time.buff;

import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

final class TimelineBuffListEffectContainer implements WakfuEffectContainer
{
    WakfuEffect m_effect;
    long m_effectContainerId;
    
    TimelineBuffListEffectContainer(final long effectContainerId, final WakfuEffect effect) {
        super();
        this.m_effect = effect;
        this.m_effectContainerId = effectContainerId;
    }
    
    @Override
    public short getLevel() {
        return 0;
    }
    
    @Override
    public short getAggroWeight() {
        return 0;
    }
    
    @Override
    public short getAllyEfficacity() {
        return 0;
    }
    
    @Override
    public short getFoeEfficacity() {
        return 0;
    }
    
    @Override
    public int getContainerType() {
        return 21;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.m_effectContainerId;
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return Collections.singleton(this.m_effect).iterator();
    }
    
    public WakfuEffect getEffect() {
        return this.m_effect;
    }
}
