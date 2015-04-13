package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagDespawnMessage extends InputOnlyProxyMessage
{
    private long m_ownerId;
    
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_ownerId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 10002;
    }
}
