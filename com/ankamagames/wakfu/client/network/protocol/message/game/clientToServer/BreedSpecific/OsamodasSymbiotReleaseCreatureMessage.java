package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.BreedSpecific;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class OsamodasSymbiotReleaseCreatureMessage extends OutputOnlyProxyMessage
{
    private byte m_creatureIndex;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put(this.m_creatureIndex);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5401;
    }
    
    public void setCreatureId(final byte creatureId) {
        this.m_creatureIndex = creatureId;
    }
}
