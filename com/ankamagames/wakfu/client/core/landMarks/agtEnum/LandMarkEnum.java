package com.ankamagames.wakfu.client.core.landMarks.agtEnum;

import com.ankamagames.framework.external.*;
import org.jetbrains.annotations.*;

public enum LandMarkEnum implements ExportableEnum
{
    NONE((byte)(-1)), 
    PERSONAL_NOTE((byte)4), 
    CRAFT((byte)1), 
    RESPAWN_POINT((byte)3), 
    ZAAP((byte)5), 
    PROTECTORS((byte)6), 
    DUNGEONS((byte)7), 
    TP((byte)8), 
    HAVEN_WORLD_BUILDING((byte)9);
    
    private final byte m_type;
    
    private LandMarkEnum(final byte type) {
        this.m_type = type;
    }
    
    public byte getType() {
        return this.m_type;
    }
    
    @Nullable
    public static LandMarkEnum getFromType(final byte type) {
        for (final LandMarkEnum value : values()) {
            if (value.m_type == type) {
                return value;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_type);
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
