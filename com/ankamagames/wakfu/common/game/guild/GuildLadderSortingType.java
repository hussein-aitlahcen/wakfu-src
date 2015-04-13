package com.ankamagames.wakfu.common.game.guild;

public enum GuildLadderSortingType
{
    LEVEL_CRESCENT((byte)0, true), 
    LEVEL_DESCENDING((byte)1, false), 
    GUILD_POINTS_CRESCENT((byte)2, true), 
    GUILD_POINTS_DESCENDING((byte)3, false), 
    CONQUEST_POINTS_CRESCENT((byte)4, true), 
    CONQUEST_POINTS_DESCENDING((byte)5, false);
    
    private final byte m_id;
    private boolean m_crescent;
    
    private GuildLadderSortingType(final byte id, final boolean crescent) {
        this.m_id = id;
        this.m_crescent = crescent;
    }
    
    public boolean isCrescent() {
        return this.m_crescent;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static GuildLadderSortingType getFromId(final byte id) {
        for (final GuildLadderSortingType guildLadderSortingType : values()) {
            if (guildLadderSortingType.m_id == id) {
                return guildLadderSortingType;
            }
        }
        return GuildLadderSortingType.GUILD_POINTS_DESCENDING;
    }
}
