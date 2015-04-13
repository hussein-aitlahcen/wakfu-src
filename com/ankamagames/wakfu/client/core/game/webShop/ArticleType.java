package com.ankamagames.wakfu.client.core.game.webShop;

public enum ArticleType
{
    ICE_GIFT("ICEGIFT"), 
    ICE_ITEM_GIFT("ICEITEMGIFT"), 
    VIRTUAL_SUBSCRIPTION_LEVEL("VIRTUALSUBSCRIPTIONLEVEL"), 
    ACCOUNT_STATUS("ACCOUNTSTATUS"), 
    OGRINE("OGRINE"), 
    DEFAULT("");
    
    private final String m_name;
    
    private ArticleType(final String name) {
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public static ArticleType getFromName(final String name) {
        for (final ArticleType type : values()) {
            if (type.m_name.equals(name)) {
                return type;
            }
        }
        return ArticleType.DEFAULT;
    }
}
