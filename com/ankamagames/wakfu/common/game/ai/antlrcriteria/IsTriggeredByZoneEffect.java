package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class IsTriggeredByZoneEffect extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public IsTriggeredByZoneEffect(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return IsTriggeredByZoneEffect.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final WakfuRunningEffect effect = CriteriaUtils.getTriggeringEffect(criterionContent);
        if (effect == null) {
            return -1;
        }
        final WakfuEffect genericEffect = ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect();
        if (genericEffect == null || (genericEffect.getAreaOfEffect().getType() == AreaOfEffectEnum.POINT && !genericEffect.hasProperty(RunningEffectPropertyType.ZONE_EFFECT))) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_TRIGGERED_BY_ZONE_EFFECT;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
