package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.time.*;

public final class GetCurrentFighterId extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCurrentFighterId.SIGNATURES;
    }
    
    public GetCurrentFighterId(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            return 0L;
        }
        final AbstractTimeline timeline = fight.getTimeline();
        return timeline.getCurrentFighterId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CURRENT_FIGHTER_ID;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
