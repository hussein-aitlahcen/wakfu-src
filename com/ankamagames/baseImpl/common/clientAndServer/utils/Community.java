package com.ankamagames.baseImpl.common.clientAndServer.utils;

public enum Community
{
    FR(0, "fr"), 
    UK(1, "en"), 
    INT(2, "int"), 
    DE(3, "de"), 
    ES(4, "es"), 
    RU(5, "ru"), 
    PT(6, "pt"), 
    NL(7, "nl"), 
    JP(8, "jp"), 
    IT(9, "it"), 
    NA(11, "na"), 
    CN(12, "cn"), 
    ASIA(13, "asia"), 
    TW(14, "tw");
    
    public static final Community DEFAULT_COMMUNITY;
    private final int m_id;
    private final String m_name;
    
    private Community(final int id, final String name) {
        this.m_id = id;
        this.m_name = name;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public static Community getFromId(final int id) {
        for (final Community community : values()) {
            if (id == community.m_id) {
                return community;
            }
        }
        return Community.DEFAULT_COMMUNITY;
    }
    
    static {
        DEFAULT_COMMUNITY = Community.UK;
    }
}
