package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public class ClientIpMessage extends InputOnlyProxyMessage
{
    private byte[] m_ip;
    
    public ClientIpMessage() {
        super();
        this.m_ip = new byte[4];
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 4, true)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        bb.get(this.m_ip);
        return true;
    }
    
    @Override
    public int getId() {
        return 110;
    }
    
    public byte[] getIp() {
        return this.m_ip;
    }
}
