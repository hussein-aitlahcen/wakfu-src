package com.ankamagames.wakfu.client.core.landMarks.agtEnum;

import com.ankamagames.framework.external.*;

public enum LandMarkExportType implements ExportableEnum
{
    STATIC((byte)0, "Statiques"), 
    IE((byte)1, "Elements Interactifs"), 
    DUNGEONS((byte)2, "Dungeons"), 
    PROTECTORS((byte)3, "Protecteurs");
    
    private final byte m_id;
    private final String m_name;
    
    private LandMarkExportType(final byte id, final String name) {
        this.m_id = id;
        this.m_name = name;
    }
    
    public static LandMarkExportType getById(final byte id) {
        for (final LandMarkExportType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_name;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
