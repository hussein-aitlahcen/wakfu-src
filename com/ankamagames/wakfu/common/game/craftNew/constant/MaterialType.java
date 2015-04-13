package com.ankamagames.wakfu.common.game.craftNew.constant;

import com.ankamagames.framework.external.*;

public enum MaterialType implements ExportableEnum
{
    NONE((byte)0), 
    LABOR((byte)1), 
    TOOL((byte)2), 
    CONSUMABLE((byte)3);
    
    private final byte m_id;
    
    private MaterialType(final byte id) {
        this.m_id = id;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static MaterialType getFromId(final byte id) {
        for (final MaterialType materialType : values()) {
            if (materialType.m_id == id) {
                return materialType;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
