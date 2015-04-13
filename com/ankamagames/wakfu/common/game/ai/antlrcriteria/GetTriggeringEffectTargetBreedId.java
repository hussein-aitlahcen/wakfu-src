package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class GetTriggeringEffectTargetBreedId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    public GetTriggeringEffectTargetBreedId(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetTriggeringEffectTargetBreedId.signatures;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final EffectUser triggeringEffectCaster = CriteriaUtils.getTriggeringEffectTarget(criterionContent);
        if (triggeringEffectCaster == null) {
            return -1L;
        }
        if (!(triggeringEffectCaster instanceof BasicCharacterInfo)) {
            return -1L;
        }
        return ((BasicCharacterInfo)triggeringEffectCaster).getBreedId();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TRIGGERING_EFFECT_TARGET_BREED_ID;
    }
    
    static {
        (GetTriggeringEffectTargetBreedId.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
