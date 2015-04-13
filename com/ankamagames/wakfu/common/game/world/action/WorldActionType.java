package com.ankamagames.wakfu.common.game.world.action;

public enum WorldActionType
{
    EFFECT_EXECUTION((byte)3), 
    EFFECT_APPLICATION((byte)21), 
    EFFECT_UNAPPLICATION((byte)22);
    
    private byte m_id;
    
    private WorldActionType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
