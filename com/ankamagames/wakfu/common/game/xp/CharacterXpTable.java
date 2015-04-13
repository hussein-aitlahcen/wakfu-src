package com.ankamagames.wakfu.common.game.xp;

import com.ankamagames.framework.kernel.core.maths.*;

public class CharacterXpTable
{
    static final short MIN_LEVEL = 1;
    static final long[] TABLE;
    private static XpTable m_instance;
    
    private static double getMonsterXp(final int level) {
        int lvl = level;
        if (lvl < 1) {
            lvl = 1;
        }
        return NewMonsterXp.getMonsterBaseXp((short)lvl);
    }
    
    private static double getNeededFightsCount(final int level) {
        return NeededFightsCountComputer.getNeededFightsCount(level);
    }
    
    public static XpTable getInstance() {
        if (CharacterXpTable.m_instance == null) {
            CharacterXpTable.m_instance = XpTableFactory.createXpTable(XpTableFactory.fromArray(CharacterXpTable.TABLE).withMinLevel(1).withMaxLevel(200));
        }
        return CharacterXpTable.m_instance;
    }
    
    public static void main(final String[] args) {
        final XpTable newTable = getInstance();
        final XpTable oldTable = CharacterXpTableOld.getInstance();
        System.err.println("level;old xp min;old xp max;new xp min;new xp max");
        for (short i = 1; i <= 200; ++i) {
            final long oldXpMin = oldTable.getXpByLevel(i);
            final long oldXpMax = oldTable.getXpByLevel(i + 1);
            final long newXpMin = newTable.getXpByLevel(i);
            final long newXpMax = newTable.getXpByLevel(i + 1);
            System.err.println(i + ";" + oldXpMin + ";" + oldXpMax + ";" + newXpMin + ";" + newXpMax);
        }
    }
    
    static {
        (TABLE = new long[200])[0] = 0L;
        double xp = 0.0;
        for (int i = 1, length = CharacterXpTable.TABLE.length; i < length; ++i) {
            final double monsterXp = getMonsterXp(i);
            final double neededFightsCount = Math.max(1.0, getNeededFightsCount(i));
            final double xpForThisLevel = monsterXp * neededFightsCount;
            xp += xpForThisLevel;
            CharacterXpTable.TABLE[i] = (long)MathHelper.round(xp, -1);
        }
        CharacterXpTable.m_instance = null;
    }
}
