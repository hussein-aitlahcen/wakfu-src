package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class InteractiveElementUpdateMessage extends InputOnlyProxyMessage
{
    private long m_elementId;
    private byte[] m_sharedDatas;
    
    public long getElementId() {
        return this.m_elementId;
    }
    
    public byte[] getSharedDatas() {
        return this.m_sharedDatas;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decode(buffer);
        return true;
    }
    
    protected void decode(final ByteBuffer buffer) {
        this.m_elementId = buffer.getLong();
        final int len = buffer.getShort() & 0xFFFF;
        if (len > 0) {
            buffer.get(this.m_sharedDatas = new byte[len]);
        }
        else {
            this.m_sharedDatas = null;
        }
    }
    
    @Override
    public int getId() {
        return 202;
    }
}
