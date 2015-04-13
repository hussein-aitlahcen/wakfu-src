package com.ankamagames.wakfu.common.game.travel;

import com.ankamagames.framework.external.*;

public enum TravelType implements ExportableEnum
{
    ZAAP((byte)1), 
    DRAGO((byte)2), 
    CANNON((byte)3), 
    BOAT((byte)4), 
    ZAAP_OUT_ONLY((byte)5), 
    HAVEN_WORLD_DRAGO((byte)6);
    
    private final byte m_id;
    
    private TravelType(final byte idx) {
        this.m_id = idx;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static TravelType getFromId(final byte typeId) {
        final TravelType[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final TravelType value = values[i];
            if (value.m_id == typeId) {
                return value;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return this.name() + " (" + this.m_id + ")";
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
