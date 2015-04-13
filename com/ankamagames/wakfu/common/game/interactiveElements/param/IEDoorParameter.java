package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.chaos.*;

public class IEDoorParameter extends IEParameter
{
    private final boolean m_consumeItem;
    private final int m_neededItemId;
    private final int m_kamaCost;
    private final SimpleCriterion m_criterion;
    
    public IEDoorParameter(final int paramId, final int visualId, final boolean consumeItem, final int neededItemId, final int kamaCost, final SimpleCriterion criterion, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_consumeItem = consumeItem;
        this.m_neededItemId = neededItemId;
        this.m_kamaCost = kamaCost;
        this.m_criterion = criterion;
    }
    
    public boolean isConsumeItem() {
        return this.m_consumeItem;
    }
    
    public int getNeededItemId() {
        return this.m_neededItemId;
    }
    
    public int getKamaCost() {
        return this.m_kamaCost;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
}
