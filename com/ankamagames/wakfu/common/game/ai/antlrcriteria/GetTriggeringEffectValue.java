package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;

public final class GetTriggeringEffectValue extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public GetTriggeringEffectValue(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetTriggeringEffectValue.SIGNATURES;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final WakfuRunningEffect effect = CriteriaUtils.getTriggeringEffect(criterionContent);
        if (effect == null) {
            return 0L;
        }
        return effect.getValue();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TRIGGERING_EFFECT_VALUE;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
