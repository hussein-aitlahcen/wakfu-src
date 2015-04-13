package com.ankamagames.wakfu.common.game.xp;

public class ItemXpTable
{
    private static XpTable m_instance;
    static final short MIN_LEVEL = 0;
    static final short MAX_LEVEL = 100;
    
    public static XpTable getInstance() {
        if (ItemXpTable.m_instance == null) {
            ItemXpTable.m_instance = XpTableFactory.createXpTable(XpTableFactory.fromArray(CharacterXpTable.TABLE).withMinLevel(0).withMaxLevel(100).withFactor(0.10000000149011612));
        }
        return ItemXpTable.m_instance;
    }
    
    static {
        ItemXpTable.m_instance = null;
    }
}
