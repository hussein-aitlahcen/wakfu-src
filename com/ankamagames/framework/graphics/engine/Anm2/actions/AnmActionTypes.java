package com.ankamagames.framework.graphics.engine.Anm2.actions;

import gnu.trove.*;

public enum AnmActionTypes
{
    GO_TO_ANIMATION((byte)1), 
    GO_TO_STATIC_ANIMATION((byte)2), 
    RUN_SCRIPT((byte)3), 
    GO_TO_RANDOM_ANIMATION((byte)4), 
    HIT((byte)5), 
    DELETE((byte)6), 
    END((byte)7), 
    GO_TO_IF_PREVIOUS_ANIMATION((byte)8), 
    ADD_PARTICLE((byte)9), 
    SET_RADIUS((byte)10);
    
    private static final TByteObjectHashMap<AnmActionTypes> m_typesByIds;
    private final byte m_id;
    
    private AnmActionTypes(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static AnmActionTypes getFromId(final byte id) {
        return AnmActionTypes.m_typesByIds.get(id);
    }
    
    static {
        m_typesByIds = new TByteObjectHashMap<AnmActionTypes>();
        for (final AnmActionTypes type : values()) {
            AnmActionTypes.m_typesByIds.put(type.getId(), type);
        }
    }
}
