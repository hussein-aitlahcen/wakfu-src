package com.ankamagames.wakfu.client.core;

public enum LoginPhase
{
    STEAM_LINK((byte)8), 
    DISPATCH((byte)8), 
    CONNECTION((byte)1), 
    CONNECTED((byte)0);
    
    private final byte m_serverId;
    
    private LoginPhase(final byte serverId) {
        this.m_serverId = serverId;
    }
    
    public byte getServerId() {
        return this.m_serverId;
    }
}
