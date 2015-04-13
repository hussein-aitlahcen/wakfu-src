package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;

public final class GetTriggeringAncestorsCount extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    public GetTriggeringAncestorsCount(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetTriggeringAncestorsCount.signatures;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        int ancestorsCount = 0;
        WakfuRunningEffect triggeringEffect = CriteriaUtils.getTriggeringEffect(criterionContent);
        while (triggeringEffect != null) {
            ++ancestorsCount;
            final WakfuRunningEffect parent = CriteriaUtils.getTriggeringEffect(triggeringEffect);
            if (parent != triggeringEffect) {
                triggeringEffect = parent;
            }
            else {
                triggeringEffect = null;
            }
        }
        return ancestorsCount;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TRIGGERING_ANCESTORS_COUNT;
    }
    
    static {
        (GetTriggeringAncestorsCount.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
