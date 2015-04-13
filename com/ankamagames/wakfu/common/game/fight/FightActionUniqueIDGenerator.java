package com.ankamagames.wakfu.common.game.fight;

public class FightActionUniqueIDGenerator
{
    private static int m_incrementalId;
    
    public static int getNextID() {
        ++FightActionUniqueIDGenerator.m_incrementalId;
        if (FightActionUniqueIDGenerator.m_incrementalId < 0) {
            FightActionUniqueIDGenerator.m_incrementalId = 0;
        }
        return FightActionUniqueIDGenerator.m_incrementalId;
    }
    
    static {
        FightActionUniqueIDGenerator.m_incrementalId = 0;
    }
}
