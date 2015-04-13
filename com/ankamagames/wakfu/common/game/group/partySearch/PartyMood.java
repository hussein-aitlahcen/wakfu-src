package com.ankamagames.wakfu.common.game.group.partySearch;

public enum PartyMood
{
    NONE((byte)0), 
    FUN((byte)1), 
    TOKENS((byte)2), 
    XP((byte)3), 
    DROP((byte)4);
    
    private final byte m_id;
    
    private PartyMood(final byte id) {
        this.m_id = id;
    }
    
    public static PartyMood getFromId(final byte id) {
        for (final PartyMood v : values()) {
            if (id == v.m_id) {
                return v;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
