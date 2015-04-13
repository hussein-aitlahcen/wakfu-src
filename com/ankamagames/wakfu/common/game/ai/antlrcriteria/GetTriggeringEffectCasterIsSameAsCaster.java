package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class GetTriggeringEffectCasterIsSameAsCaster extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTriggeringEffectCasterIsSameAsCaster.signatures;
    }
    
    public GetTriggeringEffectCasterIsSameAsCaster(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionContent instanceof RunningEffect)) {
            throw new CriteriaExecutionException("Le crit\u00e8re d'effet est employ\u00e9 pour autre chose qu'un effet");
        }
        final EffectUser caster = ((RunningEffect)criterionContent).getCaster();
        if (CriteriaUtils.getTriggeringEffectCaster(criterionContent) != caster) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER;
    }
    
    static {
        (GetTriggeringEffectCasterIsSameAsCaster.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
