package com.ankamagames.wakfu.common.game.gameActions;

public enum PlayScriptSource
{
    DEFAULT((byte)0), 
    GENERIC_IE((byte)1);
    
    private final byte m_id;
    
    private PlayScriptSource(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static PlayScriptSource fromId(final byte value) {
        for (final PlayScriptSource p : values()) {
            if (p.m_id == value) {
                return p;
            }
        }
        return PlayScriptSource.DEFAULT;
    }
}
