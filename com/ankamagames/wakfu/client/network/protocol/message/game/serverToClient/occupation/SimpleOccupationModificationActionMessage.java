package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation;

import java.nio.*;

public class SimpleOccupationModificationActionMessage extends SimpleOccupationModificationMessage
{
    private long m_contextId;
    private byte m_contextType;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_contextType = buffer.get();
        this.m_contextId = buffer.getLong();
        this.decode(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 4181;
    }
    
    public byte getActionContextType() {
        return this.m_contextType;
    }
    
    public long getActionContextUniqueId() {
        return this.m_contextId;
    }
}
