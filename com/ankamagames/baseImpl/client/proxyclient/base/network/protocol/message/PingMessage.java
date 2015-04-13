package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public class PingMessage extends OutputOnlyProxyMessage
{
    protected byte m_serverId;
    protected int m_key;
    
    public byte getServerId() {
        return this.m_serverId;
    }
    
    public PingMessage(final byte serverId, final int key) {
        super();
        this.m_serverId = serverId;
        this.m_key = key;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(13);
        bb.put(this.m_serverId);
        bb.putInt(this.m_key);
        bb.putLong(System.nanoTime());
        return this.addClientHeader(this.m_serverId, bb.array());
    }
    
    @Override
    public int getId() {
        return 107;
    }
}
