package com.ankamagames.wakfu.common.game.xp;

import com.ankamagames.framework.kernel.core.maths.*;

public class CharacterXpTableOld
{
    static final short MIN_LEVEL = 1;
    static final long[] m_table;
    private static XpTable m_instance;
    
    private static double getMonsterXp(int level) {
        if (level < 1) {
            level = 1;
        }
        return level * 100 + 1.5 * Math.pow(level, 2.2);
    }
    
    private static double getNeededFightsCount(final int level) {
        if (level == 1) {
            return 1.0;
        }
        if (level < 100) {
            return level + 2 + 5.0E-4 * Math.pow(level, 2.7);
        }
        double result = 300.0;
        for (int levelAbove100 = level - 100; levelAbove100 > 0; levelAbove100 -= 10) {
            result += levelAbove100 * 10;
        }
        if (level >= 110) {
            result *= 2.5;
        }
        return result;
    }
    
    public static XpTable getInstance() {
        if (CharacterXpTableOld.m_instance == null) {
            CharacterXpTableOld.m_instance = XpTableFactory.createXpTable(XpTableFactory.fromArray(CharacterXpTableOld.m_table).withMinLevel(1).withMaxLevel(200));
        }
        return CharacterXpTableOld.m_instance;
    }
    
    public static void main(final String[] args) {
        final XpTable instance = getInstance();
        System.err.println("Niveau;Xp depuis niveau precedent;Xp totale");
        for (short i = 1; i <= 200; ++i) {
            System.err.println(i + " ; " + instance.getXpByLevel(i));
        }
    }
    
    static {
        (m_table = new long[200])[0] = 0L;
        double xp = 0.0;
        for (int i = 1; i < CharacterXpTableOld.m_table.length; ++i) {
            final double monsterXp = getMonsterXp(i);
            final double neededFightsCount = Math.max(1.0, getNeededFightsCount(i));
            final double xpForThisLevel = monsterXp * neededFightsCount;
            xp += xpForThisLevel;
            CharacterXpTableOld.m_table[i] = (long)MathHelper.round(xp, -1);
        }
        CharacterXpTableOld.m_instance = null;
    }
}
