package com.ankamagames.wakfu.common.game.dungeon;

import java.util.*;

public class DungeonLadderConstants
{
    public static final int LADDERS_SIZE = 3;
    public static Comparator<DungeonLadderResult> TIME_ATTACK_COMPARATOR;
    public static DungeonLadderScoreValidator TIME_ATTACK_SCORE_VALIDATOR;
    public static Comparator<DungeonLadderResult> SURVIVAL_COMPARATOR;
    public static DungeonLadderScoreValidator SURVIVAL_SCORE_VALIDATOR;
    
    static {
        DungeonLadderConstants.TIME_ATTACK_COMPARATOR = new Comparator<DungeonLadderResult>() {
            @Override
            public int compare(final DungeonLadderResult dungeonLadderResult, final DungeonLadderResult dungeonLadderResult1) {
                return new Long(dungeonLadderResult.getScore()).compareTo(Long.valueOf(dungeonLadderResult1.getScore()));
            }
        };
        DungeonLadderConstants.TIME_ATTACK_SCORE_VALIDATOR = new DungeonLadderScoreValidator() {
            @Override
            public boolean isWorthLaddering(final long score, final ArrayList<DungeonLadderResult> results) {
                return results.size() < 3 || score < results.get(results.size() - 1).getScore();
            }
        };
        DungeonLadderConstants.SURVIVAL_COMPARATOR = new Comparator<DungeonLadderResult>() {
            @Override
            public int compare(final DungeonLadderResult dungeonLadderResult, final DungeonLadderResult dungeonLadderResult1) {
                return -new Long(dungeonLadderResult.getScore()).compareTo(Long.valueOf(dungeonLadderResult1.getScore()));
            }
        };
        DungeonLadderConstants.SURVIVAL_SCORE_VALIDATOR = new DungeonLadderScoreValidator() {
            @Override
            public boolean isWorthLaddering(final long score, final ArrayList<DungeonLadderResult> results) {
                return results.size() < 3 || score > results.get(results.size() - 1).getScore();
            }
        };
    }
}
