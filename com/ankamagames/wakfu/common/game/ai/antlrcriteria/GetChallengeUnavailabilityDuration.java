package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.constants.*;
import java.util.*;

public final class GetChallengeUnavailabilityDuration extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetChallengeUnavailabilityDuration.SIGNATURES;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        return ChallengeConstants.CHALLENGE_UNAVAILABILITY_DURATION.toLong();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CHALLENGE_UNAVAILABILITY_DURATION;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
