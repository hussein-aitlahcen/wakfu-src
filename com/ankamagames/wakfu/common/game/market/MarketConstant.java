package com.ankamagames.wakfu.common.game.market;

public interface MarketConstant
{
    public static final int DELETE_HISTORY_LIFETIME_DAY = 30;
    public static final long OUTDATED_ENTRIES_CHECK_DELAY = 5000L;
    public static final int NB_ENTRIES_PER_PAGE = 10;
    public static final int MAX_ENTRIES_PER_SELLER = 10;
    public static final float MARKET_ITEM_TAX = 0.01f;
    public static final float MARKET_BOARD_TAX = 0.001f;
    public static final int MAX_ITEM_MATCHES = 200;
    
    public enum ConsultReturnSerialType
    {
        MARKET_ENTRY, 
        MARKET_HISTORY_ENTRY;
    }
}
