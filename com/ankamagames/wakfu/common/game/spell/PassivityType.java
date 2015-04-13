package com.ankamagames.wakfu.common.game.spell;

public enum PassivityType
{
    ACTIVE((byte)0), 
    PASSIVE_FROM_1((byte)1), 
    PASSIVE_FROM_0((byte)2);
    
    private final byte m_id;
    
    private PassivityType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
