package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class ClientCalendarSynchronizationMessage extends InputOnlyProxyMessage
{
    private long m_synchronizationTime;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_synchronizationTime = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 2063;
    }
    
    public long getSynchronizationTime() {
        return this.m_synchronizationTime;
    }
}
