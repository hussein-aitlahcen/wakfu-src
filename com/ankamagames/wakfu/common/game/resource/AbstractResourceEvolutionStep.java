package com.ankamagames.wakfu.common.game.resource;

import com.ankamagames.wakfu.common.game.craft.collect.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;

public class AbstractResourceEvolutionStep<TCollect extends AbstractCollectAction>
{
    private final byte m_stepIndex;
    private final IntObjectLightWeightMap<TCollect> m_collects;
    
    public AbstractResourceEvolutionStep(final byte stepIndex) {
        super();
        this.m_collects = new IntObjectLightWeightMap<TCollect>(0);
        this.m_stepIndex = stepIndex;
    }
    
    public final void ensureCollectCapacity(final int capacity) {
        this.m_collects.ensureCapacity(capacity);
    }
    
    public final void addCollectAction(final TCollect collect) {
        this.m_collects.put(collect.getId(), collect);
    }
    
    public final TCollect getCollectAction(final int collectId) {
        return this.m_collects.get(collectId);
    }
    
    public final TCollect getQuickCollect(final int index) {
        return this.m_collects.getQuickValue(index);
    }
    
    public final int getCollectsCount() {
        return this.m_collects.size();
    }
    
    public final byte getStepIndex() {
        return this.m_stepIndex;
    }
}
