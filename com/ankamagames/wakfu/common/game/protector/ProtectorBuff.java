package com.ankamagames.wakfu.common.game.protector;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.buff.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public class ProtectorBuff implements WakfuEffectContainer
{
    private final int m_id;
    private final SimpleCriterion m_criterion;
    private final BuffOrigin m_origin;
    private final ArrayList<WakfuEffect> m_effects;
    
    protected ProtectorBuff(final int id, final SimpleCriterion criterion, final byte origin, final ArrayList<WakfuStandardEffect> effects) {
        super();
        this.m_effects = new ArrayList<WakfuEffect>();
        this.m_id = id;
        this.m_criterion = criterion;
        this.m_origin = BuffOrigin.getFromValue(origin);
        this.m_effects.addAll(effects);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return this.m_effects.iterator();
    }
    
    @Override
    public int getContainerType() {
        return 19;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.m_id;
    }
    
    public BuffOrigin getOrigin() {
        return this.m_origin;
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
}
