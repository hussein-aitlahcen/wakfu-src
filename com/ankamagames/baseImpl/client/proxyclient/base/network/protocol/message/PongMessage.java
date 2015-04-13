package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public class PongMessage extends InputOnlyProxyMessage
{
    protected byte m_serverId;
    protected int m_key;
    protected long m_pingSentDate;
    protected long m_pingReceivedDate;
    protected long m_pongSentDate;
    protected long m_pongReceivedDate;
    
    public byte getServerId() {
        return this.m_serverId;
    }
    
    public int getKey() {
        return this.m_key;
    }
    
    public long getPingSentDate() {
        return this.m_pingSentDate;
    }
    
    public long getPingReceivedDate() {
        return this.m_pingReceivedDate;
    }
    
    public long getPongSentDate() {
        return this.m_pongSentDate;
    }
    
    public long getPongReceivedDate() {
        return this.m_pongReceivedDate;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 29, true)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_serverId = bb.get();
        this.m_key = bb.getInt();
        this.m_pingSentDate = bb.getLong();
        this.m_pingReceivedDate = bb.getLong();
        this.m_pongSentDate = bb.getLong();
        this.m_pongReceivedDate = System.nanoTime();
        return true;
    }
    
    @Override
    public int getId() {
        return 108;
    }
}
