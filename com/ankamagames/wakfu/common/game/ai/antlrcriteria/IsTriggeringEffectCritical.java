package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class IsTriggeringEffectCritical extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public IsTriggeringEffectCritical(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return IsTriggeringEffectCritical.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final WakfuRunningEffect triggeringEffect = CriteriaUtils.getTriggeringEffect(criterionContent);
        if (triggeringEffect == null) {
            return -1;
        }
        final WakfuEffect genericEffect = ((RunningEffect<WakfuEffect, EC>)triggeringEffect).getGenericEffect();
        if (genericEffect == null) {
            return -1;
        }
        return genericEffect.checkFlags(1L) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_TRIGGERING_EFFECT_CRITICAL;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
