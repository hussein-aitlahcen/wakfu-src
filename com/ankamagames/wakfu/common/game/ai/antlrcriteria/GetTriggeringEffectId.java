package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;

public final class GetTriggeringEffectId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    public GetTriggeringEffectId(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetTriggeringEffectId.signatures;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final WakfuRunningEffect effect = CriteriaUtils.getTriggeringEffect(criterionContent);
        if (effect == null) {
            return -1L;
        }
        return effect.getEffectId();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TRIGGERING_EFFECT_ID;
    }
    
    static {
        (GetTriggeringEffectId.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
