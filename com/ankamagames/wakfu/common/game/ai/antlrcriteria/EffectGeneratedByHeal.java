package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;

public class EffectGeneratedByHeal extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return EffectGeneratedByHeal.SIGNATURES;
    }
    
    public EffectGeneratedByHeal(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContent instanceof WakfuRunningEffect) {
            final RunningEffect effect = ((WakfuRunningEffect)criterionContent).getParent();
            if (effect != null && effect.getId() == RunningEffectConstants.HP_GAIN.getId()) {
                return 0;
            }
        }
        return 1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.EFFECTGENERATEDBYHEAL;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
