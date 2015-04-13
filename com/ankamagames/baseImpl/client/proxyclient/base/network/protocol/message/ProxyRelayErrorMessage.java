package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

public class ProxyRelayErrorMessage extends InputOnlyProxyMessage
{
    private byte m_serverGroup;
    private byte m_reason;
    
    public ProxyRelayErrorMessage() {
        super();
        this.m_serverGroup = -1;
        this.m_reason = -1;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 2, true)) {
            return false;
        }
        this.m_reason = rawDatas[0];
        this.m_serverGroup = rawDatas[1];
        return true;
    }
    
    @Override
    public int getId() {
        return 9;
    }
    
    public byte getServerGroup() {
        return this.m_serverGroup;
    }
    
    public byte getReason() {
        return this.m_reason;
    }
}
