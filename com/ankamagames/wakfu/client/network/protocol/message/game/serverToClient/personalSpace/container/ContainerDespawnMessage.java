package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.container;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ContainerDespawnMessage extends InputOnlyProxyMessage
{
    private long m_containerGuid;
    
    public long getContainerGuid() {
        return this.m_containerGuid;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_containerGuid = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 10062;
    }
}
