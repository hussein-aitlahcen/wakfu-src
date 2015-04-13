package com.ankamagames.wakfu.client.core.game.travel;

import com.ankamagames.framework.external.*;

public enum TravelGfxType implements ExportableEnum
{
    ZAAP((byte)1), 
    BOAT((byte)2), 
    DRAGO((byte)3), 
    CANNON((byte)4);
    
    private final byte m_gfxId;
    
    private TravelGfxType(final byte gfxId) {
        this.m_gfxId = gfxId;
    }
    
    public byte getGfxId() {
        return this.m_gfxId;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_gfxId);
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static TravelGfxType getFromId(final byte gfxId) {
        for (final TravelGfxType type : values()) {
            if (type.m_gfxId == gfxId) {
                return type;
            }
        }
        return null;
    }
}
