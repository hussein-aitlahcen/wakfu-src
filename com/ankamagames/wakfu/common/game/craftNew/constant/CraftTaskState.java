package com.ankamagames.wakfu.common.game.craftNew.constant;

public enum CraftTaskState
{
    CANCELED((byte)0), 
    RUNNING((byte)1), 
    FAILED((byte)2), 
    SUCCESS((byte)3);
    
    private final byte m_id;
    
    private CraftTaskState(final byte id) {
        this.m_id = id;
    }
    
    public static CraftTaskState getFromId(final byte id) {
        for (final CraftTaskState craftTaskState : values()) {
            if (craftTaskState.m_id == id) {
                return craftTaskState;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
