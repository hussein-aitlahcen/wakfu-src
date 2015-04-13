package com.ankamagames.wakfu.common.account.antiAddiction;

public enum AntiAddictionLevel
{
    UNKNOWN(-1), 
    UN_ACTIVATED(0), 
    ACTIVATED(1);
    
    public final int m_id;
    
    private AntiAddictionLevel(final int id) {
        this.m_id = id;
    }
    
    public static AntiAddictionLevel fromId(final int id) {
        for (final AntiAddictionLevel level : values()) {
            if (level.m_id == id) {
                return level;
            }
        }
        return AntiAddictionLevel.UNKNOWN;
    }
}
