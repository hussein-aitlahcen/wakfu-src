package com.ankamagames.wakfu.client.core.game.craft;

public enum CraftType
{
    CRAFT((byte)0), 
    HARVEST((byte)1);
    
    private final byte m_id;
    
    private CraftType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static CraftType getFromId(final byte id) {
        for (final CraftType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
