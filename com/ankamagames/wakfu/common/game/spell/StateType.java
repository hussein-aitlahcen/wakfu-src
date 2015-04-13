package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.framework.external.*;

public enum StateType implements ExportableEnum
{
    NEGATIF((byte)(-1)), 
    NEUTRAL((byte)0), 
    POSITIF((byte)1);
    
    private final byte m_type;
    
    public static StateType getFromValue(final byte value) {
        for (final StateType type : values()) {
            if (type.m_type == value) {
                return type;
            }
        }
        return StateType.NEUTRAL;
    }
    
    private StateType(final byte type) {
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
