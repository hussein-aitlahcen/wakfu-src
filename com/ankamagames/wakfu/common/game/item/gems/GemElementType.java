package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.framework.external.*;

public enum GemElementType implements ExportableEnum
{
    NONE((byte)0), 
    POWDER((byte)1), 
    GEM((byte)2);
    
    private final byte m_id;
    
    private GemElementType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static GemElementType getById(final byte id) {
        for (final GemElementType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
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
