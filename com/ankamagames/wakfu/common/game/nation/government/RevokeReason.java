package com.ankamagames.wakfu.common.game.nation.government;

public enum RevokeReason
{
    CITIZEN_SCORE((byte)0), 
    POPULARITY((byte)1), 
    BY_GOVERNMENT_MEMBER((byte)2), 
    GAME_ACTION((byte)3);
    
    private final byte m_id;
    
    private RevokeReason(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static RevokeReason byId(final byte id) {
        for (final RevokeReason reason : values()) {
            if (id == reason.m_id) {
                return reason;
            }
        }
        return null;
    }
}
