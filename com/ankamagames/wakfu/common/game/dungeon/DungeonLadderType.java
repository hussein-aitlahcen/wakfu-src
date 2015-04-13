package com.ankamagames.wakfu.common.game.dungeon;

import java.util.*;

public enum DungeonLadderType
{
    TIME_ATTACK(DungeonLadderConstants.TIME_ATTACK_COMPARATOR, DungeonLadderConstants.TIME_ATTACK_SCORE_VALIDATOR), 
    SURVIVAL(DungeonLadderConstants.SURVIVAL_COMPARATOR, DungeonLadderConstants.SURVIVAL_SCORE_VALIDATOR);
    
    private final Comparator<DungeonLadderResult> m_resultComparator;
    private final DungeonLadderScoreValidator m_scoreValidator;
    
    private DungeonLadderType(final Comparator<DungeonLadderResult> resultComparator, final DungeonLadderScoreValidator scoreValidator) {
        this.m_resultComparator = resultComparator;
        this.m_scoreValidator = scoreValidator;
    }
    
    public Comparator<DungeonLadderResult> getResultComparator() {
        return this.m_resultComparator;
    }
    
    public boolean isWorthLaddering(final long score, final ArrayList<DungeonLadderResult> previousResults) {
        return this.m_scoreValidator.isWorthLaddering(score, previousResults);
    }
}
