package com.ankamagames.wakfu.common.game.dungeon;

public class MonsterScoreCalculator
{
    private static final int MULTI = 10;
    
    public static int getScore(final short npcLevel, final float multiplicator) {
        return (int)(multiplicator * npcLevel * 10.0f);
    }
}
