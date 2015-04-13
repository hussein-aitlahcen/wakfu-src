package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class GetEffectCasterOriginalController extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetEffectCasterOriginalController.signatures;
    }
    
    public GetEffectCasterOriginalController(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContent == null) {
            return 0L;
        }
        if (!(criterionContent instanceof RunningEffect)) {
            throw new CriteriaExecutionException("Le crit\u00e8re d'effet est employ\u00e9 pour autre chose qu'un effet");
        }
        final EffectUser user = ((RunningEffect)criterionContent).getCaster();
        if (user == null || !(user instanceof CriterionUser)) {
            return 0L;
        }
        return ((CriterionUser)user).getOriginalControllerId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_EFFECT_CASTER_ORIGINAL_CONTROLLER;
    }
    
    static {
        (GetEffectCasterOriginalController.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
