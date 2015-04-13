package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public class ServerPongMessage extends OutputOnlyProxyMessage
{
    private byte m_architecture;
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(0);
        return this.addClientHeader(this.m_architecture, bb.array());
    }
    
    @Override
    public int getId() {
        return 112;
    }
    
    public void setArchitecture(final byte architecture) {
        this.m_architecture = architecture;
    }
}
