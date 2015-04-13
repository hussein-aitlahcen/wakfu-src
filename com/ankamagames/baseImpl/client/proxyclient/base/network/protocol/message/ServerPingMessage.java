package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public class ServerPingMessage extends InputOnlyProxyMessage
{
    private byte m_architectureTarget;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 1, true)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_architectureTarget = bb.get();
        return true;
    }
    
    public byte getArchitectureTarget() {
        return this.m_architectureTarget;
    }
    
    @Override
    public int getId() {
        return 111;
    }
}
