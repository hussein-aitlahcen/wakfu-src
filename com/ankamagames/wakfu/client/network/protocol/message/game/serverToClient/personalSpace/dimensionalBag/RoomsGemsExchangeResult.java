package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RoomsGemsExchangeResult extends InputOnlyProxyMessage
{
    private boolean m_sourcePrimary;
    private boolean m_destPrimary;
    private byte m_sourceRoomLayoutPosition;
    private byte m_destRoomLayoutPosition;
    
    public boolean isSourcePrimary() {
        return this.m_sourcePrimary;
    }
    
    public boolean isDestPrimary() {
        return this.m_destPrimary;
    }
    
    public byte getSourceRoomLayoutPosition() {
        return this.m_sourceRoomLayoutPosition;
    }
    
    public byte getDestRoomLayoutPosition() {
        return this.m_destRoomLayoutPosition;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_sourcePrimary = (buffer.get() == 1);
        this.m_destPrimary = (buffer.get() == 1);
        this.m_sourceRoomLayoutPosition = buffer.get();
        this.m_destRoomLayoutPosition = buffer.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 10040;
    }
}
