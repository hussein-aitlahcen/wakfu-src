package com.ankamagames.wakfu.common.game.buff;

import com.ankamagames.framework.external.*;

public enum BuffOrigin implements ExportableEnum
{
    MDC((byte)0), 
    SHUKRUTE((byte)1);
    
    private final byte m_type;
    
    public static BuffOrigin getFromValue(final byte value) {
        for (final BuffOrigin type : values()) {
            if (type.m_type == value) {
                return type;
            }
        }
        return BuffOrigin.MDC;
    }
    
    private BuffOrigin(final byte type) {
        this.m_type = type;
    }
    
    @Override
    public String getEnumId() {
        return Byte.toString(this.m_type);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
