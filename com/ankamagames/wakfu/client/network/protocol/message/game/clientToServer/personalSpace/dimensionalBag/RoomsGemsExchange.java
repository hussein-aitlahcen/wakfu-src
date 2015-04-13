package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RoomsGemsExchange extends OutputOnlyProxyMessage
{
    private boolean m_sourcePrimary;
    private boolean m_destPrimary;
    private byte m_sourceRoomLayoutPosition;
    private byte m_destRoomLayoutPosition;
    
    public void setSourcePrimary(final boolean sourcePrimary) {
        this.m_sourcePrimary = sourcePrimary;
    }
    
    public void setDestPrimary(final boolean destPrimary) {
        this.m_destPrimary = destPrimary;
    }
    
    public void setSourceRoomLayoutPosition(final byte sourceRoomLayoutPosition) {
        this.m_sourceRoomLayoutPosition = sourceRoomLayoutPosition;
    }
    
    public void setDestRoomLayoutPosition(final byte destRoomLayoutPosition) {
        this.m_destRoomLayoutPosition = destRoomLayoutPosition;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put((byte)(this.m_sourcePrimary ? 1 : 0));
        buffer.put((byte)(this.m_destPrimary ? 1 : 0));
        buffer.put(this.m_sourceRoomLayoutPosition);
        buffer.put(this.m_destRoomLayoutPosition);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10039;
    }
}
