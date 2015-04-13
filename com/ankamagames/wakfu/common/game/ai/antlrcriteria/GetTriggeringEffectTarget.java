package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class GetTriggeringEffectTarget extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    public GetTriggeringEffectTarget(final List<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetTriggeringEffectTarget.signatures;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final EffectUser triggeringEffectTarget = CriteriaUtils.getTriggeringEffectTarget(criterionContent);
        if (triggeringEffectTarget == null) {
            return -1L;
        }
        if (!(triggeringEffectTarget instanceof BasicCharacterInfo)) {
            return -1L;
        }
        return triggeringEffectTarget.getId();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TRIGGERING_EFFECT_TARGET;
    }
    
    static {
        (GetTriggeringEffectTarget.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
