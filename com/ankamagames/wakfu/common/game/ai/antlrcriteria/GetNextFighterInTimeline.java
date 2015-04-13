package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.time.*;

public final class GetNextFighterInTimeline extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public GetNextFighterInTimeline(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            return 0L;
        }
        final AbstractTimeline timeline = fight.getTimeline();
        if (timeline == null) {
            return 0L;
        }
        final long nextFighterId = timeline.getNextFighter();
        return nextFighterId;
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetNextFighterInTimeline.SIGNATURES;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_NEXT_FIGHTER_IN_TIMELINE;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
