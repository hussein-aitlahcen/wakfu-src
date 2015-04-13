package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public final class GetFightCurrentTableTurn extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    public GetFightCurrentTableTurn(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetFightCurrentTableTurn.signatures;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            GetFightCurrentTableTurn.m_logger.error((Object)"Le critere ne peut pas etre utilis\u00e9 autre part que dans un contexte de combat");
            return -1L;
        }
        final TurnBasedTimeline timeline = fight.getTimeline();
        if (timeline == null) {
            return -1L;
        }
        return timeline.getCurrentTableturn();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_FGHT_CURRENT_TABLE_TURN;
    }
    
    static {
        (GetFightCurrentTableTurn.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
