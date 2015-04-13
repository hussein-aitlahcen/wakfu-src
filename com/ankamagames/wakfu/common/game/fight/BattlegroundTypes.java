package com.ankamagames.wakfu.common.game.fight;

public enum BattlegroundTypes
{
    DYNAMIC((byte)1), 
    RECTANGULAR((byte)2), 
    USER_DEFINED((byte)3), 
    RANDOM_IN_CUSTOM_PVP((byte)4), 
    CUSTOM((byte)5);
    
    byte m_id;
    
    private BattlegroundTypes(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
