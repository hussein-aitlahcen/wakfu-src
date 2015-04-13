package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

public class ForcedDisconnectionReasonMessage extends InputOnlyProxyMessage
{
    private byte m_reason;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_reason = rawDatas[0];
        return true;
    }
    
    @Override
    public int getId() {
        return 6;
    }
    
    public byte getReason() {
        return this.m_reason;
    }
}
