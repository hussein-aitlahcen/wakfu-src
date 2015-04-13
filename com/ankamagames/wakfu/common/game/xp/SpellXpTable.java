package com.ankamagames.wakfu.common.game.xp;

public class SpellXpTable
{
    private static XpTable m_instance;
    static final long XP_NEDED_TO_UNLOCK_SPELL_LEVEL_1 = 100L;
    static final long XP_INCREMENT_PER_LEVEL = 1000L;
    
    private static long[] populateTableFromCharacterXpTable() {
        final XpTable characterXpTable = CharacterXpTable.getInstance();
        final long[] table = new long[201];
        table[0] = 0L;
        table[1] = 100L;
        for (int i = 1; i < characterXpTable.getMaxLevel(); ++i) {
            table[i + 1] = table[i] + 1000L * i;
        }
        return table;
    }
    
    public static XpTable getInstance() {
        if (SpellXpTable.m_instance == null) {
            SpellXpTable.m_instance = XpTableFactory.createXpTable(XpTableFactory.fromArray(populateTableFromCharacterXpTable()).withMinLevel(0).withMaxLevel(200));
        }
        return SpellXpTable.m_instance;
    }
    
    static {
        SpellXpTable.m_instance = null;
    }
}
