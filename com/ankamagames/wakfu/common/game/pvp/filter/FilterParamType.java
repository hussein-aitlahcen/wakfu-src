package com.ankamagames.wakfu.common.game.pvp.filter;

import org.jetbrains.annotations.*;

public enum FilterParamType
{
    ALL((byte)0), 
    BREED((byte)1), 
    GUILD((byte)2);
    
    private final byte m_id;
    
    private FilterParamType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @Nullable
    public static FilterParamType getFromId(final byte id) {
        for (final FilterParamType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
