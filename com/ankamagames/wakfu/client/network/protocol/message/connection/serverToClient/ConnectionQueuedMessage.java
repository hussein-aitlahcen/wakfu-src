package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class ConnectionQueuedMessage extends InputOnlyProxyMessage
{
    private int m_queuePosition;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_queuePosition = bb.getInt();
        return false;
    }
    
    public int getQueuePosition() {
        return this.m_queuePosition;
    }
    
    @Override
    public int getId() {
        return 1208;
    }
    
    @Override
    public String toString() {
        return "ConnectionQueuedMessage{m_queuePosition=" + this.m_queuePosition + '}';
    }
}
