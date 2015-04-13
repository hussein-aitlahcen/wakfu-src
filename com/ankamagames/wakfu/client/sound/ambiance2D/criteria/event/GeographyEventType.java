package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

public enum GeographyEventType
{
    SEA((byte)0), 
    RIVER((byte)1), 
    LAKE((byte)2), 
    LAVA((byte)3), 
    RUNNING_LAVA((byte)4);
    
    final byte m_id;
    
    private GeographyEventType(final byte id) {
        this.m_id = id;
    }
    
    public static GeographyEventType fromId(final byte id) {
        for (final GeographyEventType t : values()) {
            if (t.m_id == id) {
                return t;
            }
        }
        return null;
    }
}
