package com.ankamagames.wakfu.common.game.craftNew.constant;

public enum ContractState
{
    SUBMITTED((byte)0), 
    REFUSED((byte)1), 
    ACCEPTED((byte)2), 
    LAUNCHED((byte)3), 
    FAILED((byte)4), 
    SUCCESSFUL((byte)5);
    
    private final byte m_id;
    
    private ContractState(final byte id) {
        this.m_id = id;
    }
    
    public static ContractState getFromId(final byte id) {
        for (final ContractState contractState : values()) {
            if (contractState.m_id == id) {
                return contractState;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
