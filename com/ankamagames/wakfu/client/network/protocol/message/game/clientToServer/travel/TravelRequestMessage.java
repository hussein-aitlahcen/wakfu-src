package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.travel;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class TravelRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_travelSourceId;
    private final long m_travelDestinationId;
    
    public TravelRequestMessage(final long travelSourceId, final long travelDestinationId) {
        super();
        this.m_travelSourceId = travelSourceId;
        this.m_travelDestinationId = travelDestinationId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(this.m_travelSourceId);
        buffer.putLong(this.m_travelDestinationId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15725;
    }
}
