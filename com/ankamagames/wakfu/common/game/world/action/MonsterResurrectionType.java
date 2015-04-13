package com.ankamagames.wakfu.common.game.world.action;

import gnu.trove.*;

public enum MonsterResurrectionType
{
    NORMAL_RESURRECTION((byte)1), 
    PEST_RESURRECTION((byte)2);
    
    private static final TByteObjectHashMap<MonsterResurrectionType> m_typesById;
    private byte m_id;
    
    private MonsterResurrectionType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static MonsterResurrectionType getById(final byte id) {
        return MonsterResurrectionType.m_typesById.get(id);
    }
    
    static {
        m_typesById = new TByteObjectHashMap<MonsterResurrectionType>();
        for (final MonsterResurrectionType type : values()) {
            MonsterResurrectionType.m_typesById.put(type.getId(), type);
        }
    }
}
