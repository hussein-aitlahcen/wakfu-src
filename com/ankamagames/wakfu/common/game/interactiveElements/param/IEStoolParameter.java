package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class IEStoolParameter extends IEParameter
{
    private static final SimpleCriterion ALWAYS_VALID_CRITERION;
    private final SimpleCriterion m_criterion;
    
    public IEStoolParameter(final int paramId, final SimpleCriterion criterion, final int visualId, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_criterion = ((criterion != null) ? criterion : IEStoolParameter.ALWAYS_VALID_CRITERION);
    }
    
    public final SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    static {
        ALWAYS_VALID_CRITERION = new ConstantBooleanCriterion(true);
    }
}
