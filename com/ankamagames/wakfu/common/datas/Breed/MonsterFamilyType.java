package com.ankamagames.wakfu.common.datas.Breed;

import com.ankamagames.framework.external.*;

public enum MonsterFamilyType implements ExportableEnum
{
    ECOSYSTEM((byte)1, "Ecosyst\u00e8me"), 
    DUNGEON((byte)2, "Donjon"), 
    SPECIAL((byte)3, "Sp\u00e9cial"), 
    MAIN((byte)4, "Principale"), 
    ARCADE((byte)5, "Arcade");
    
    private byte m_id;
    private String m_adminDescription;
    
    private MonsterFamilyType(final byte id, final String adminDescription) {
        this.m_id = id;
        this.m_adminDescription = adminDescription;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_adminDescription;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static MonsterFamilyType fromId(final byte id) {
        for (final MonsterFamilyType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
