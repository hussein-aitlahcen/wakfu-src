package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public abstract class AbstractEffectGroup implements WakfuEffectContainer
{
    public static short DEFAULT_AGGRO_WEIGHT;
    public static short DEFAULT_ALLY_EFFICACITY;
    public static short DEFAULT_FOE_EFFICACITY;
    protected int m_effectGroupBaseId;
    protected short m_level;
    protected GrowingArray<WakfuEffect> m_effects;
    
    public static short getLevelFromUniqueId(final int uniqueId) {
        return (short)(uniqueId & 0xFF);
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    protected AbstractEffectGroup() {
        super();
        this.m_effects = new GrowingArray<WakfuEffect>();
    }
    
    public AbstractEffectGroup(final int basedId) {
        super();
        this.m_effects = new GrowingArray<WakfuEffect>();
        this.m_effectGroupBaseId = basedId;
    }
    
    public abstract AbstractEffectGroup instanceAnother(final short p0);
    
    public int getTurnDuration() {
        return 0;
    }
    
    public int getTableTurnDuration() {
        return 0;
    }
    
    public int getEffectGroupBaseId() {
        return this.m_effectGroupBaseId;
    }
    
    public void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return this.m_effects.iterator();
    }
    
    public int getEffectsCount() {
        return this.m_effects.size();
    }
    
    public WakfuEffect getEffect(final int num) {
        return this.m_effects.get(num);
    }
    
    @Override
    public int getContainerType() {
        return 4;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.m_effectGroupBaseId;
    }
    
    @Override
    public short getAggroWeight() {
        return AbstractEffectGroup.DEFAULT_AGGRO_WEIGHT;
    }
    
    @Override
    public short getAllyEfficacity() {
        return AbstractEffectGroup.DEFAULT_ALLY_EFFICACITY;
    }
    
    @Override
    public short getFoeEfficacity() {
        return AbstractEffectGroup.DEFAULT_FOE_EFFICACITY;
    }
    
    static {
        AbstractEffectGroup.DEFAULT_AGGRO_WEIGHT = 0;
        AbstractEffectGroup.DEFAULT_ALLY_EFFICACITY = 0;
        AbstractEffectGroup.DEFAULT_FOE_EFFICACITY = 0;
    }
}
