package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChangeBagPositionRequestMessage extends OutputOnlyProxyMessage
{
    private byte m_destinationPosition;
    private long m_bagId;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 9;
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putLong(this.m_bagId);
        buffer.put(this.m_destinationPosition);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5220;
    }
    
    public void setDestinationPosition(final byte destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
    
    public void setBagId(final long bagId) {
        this.m_bagId = bagId;
    }
}
