package com.ankamagames.wakfu.common.game.zone;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public final class ZoneBuff implements WakfuEffectContainer
{
    protected static final Logger m_logger;
    private int m_id;
    private long m_duration;
    private SimpleCriterion m_criterions;
    private final ArrayList<WakfuEffect> m_effects;
    
    public ZoneBuff(final int id, final long duration, final SimpleCriterion criterions) {
        super();
        this.m_effects = new ArrayList<WakfuEffect>();
        this.m_id = id;
        this.m_duration = duration;
        this.m_criterions = criterions;
    }
    
    @Override
    public final int getContainerType() {
        return 18;
    }
    
    @Override
    public final long getEffectContainerId() {
        return this.m_id;
    }
    
    public final int getId() {
        return this.m_id;
    }
    
    public final long getDuration() {
        return this.m_duration;
    }
    
    public final boolean isInfinite() {
        return this.m_duration <= 0L;
    }
    
    public final SimpleCriterion getCriterions() {
        return this.m_criterions;
    }
    
    public final void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
    }
    
    @Override
    public final Iterator<WakfuEffect> iterator() {
        return this.m_effects.iterator();
    }
    
    @Override
    public final short getLevel() {
        return 0;
    }
    
    @Override
    public final short getAggroWeight() {
        return 0;
    }
    
    @Override
    public final short getAllyEfficacity() {
        return 0;
    }
    
    @Override
    public final short getFoeEfficacity() {
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ZoneBuff.class);
    }
}
