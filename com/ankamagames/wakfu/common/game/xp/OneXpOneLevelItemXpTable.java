package com.ankamagames.wakfu.common.game.xp;

public final class OneXpOneLevelItemXpTable
{
    private static XpTable INSTANCE;
    static final short MIN_LEVEL = 0;
    static final short MAX_LEVEL = 32000;
    static final long[] TABLE;
    
    public static XpTable getInstance() {
        if (OneXpOneLevelItemXpTable.INSTANCE == null) {
            OneXpOneLevelItemXpTable.INSTANCE = XpTableFactory.createXpTable(XpTableFactory.fromArray(OneXpOneLevelItemXpTable.TABLE).withMinLevel(0).withMaxLevel(32000));
        }
        return OneXpOneLevelItemXpTable.INSTANCE;
    }
    
    static {
        OneXpOneLevelItemXpTable.INSTANCE = null;
        (TABLE = new long[32000])[0] = 0L;
        for (int i = 1; i < OneXpOneLevelItemXpTable.TABLE.length; ++i) {
            OneXpOneLevelItemXpTable.TABLE[i] = OneXpOneLevelItemXpTable.TABLE[i - 1] + 1L;
        }
    }
}
