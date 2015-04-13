package com.ankamagames.wakfu.common.game.aptitude;

public enum AptitudeType
{
    COMMON((byte)0), 
    SPELL((byte)1);
    
    private final byte m_id;
    
    private AptitudeType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static AptitudeType getFromId(final byte id) {
        for (final AptitudeType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
