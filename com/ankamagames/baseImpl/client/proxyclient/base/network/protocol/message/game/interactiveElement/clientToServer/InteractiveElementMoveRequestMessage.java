package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class InteractiveElementMoveRequestMessage extends OutputOnlyProxyMessage
{
    private long m_elementId;
    private Point3 m_destination;
    
    public void setElementId(final long elementId) {
        this.m_elementId = elementId;
    }
    
    public void setDestination(final Point3 destination) {
        this.m_destination = destination;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(18);
        buffer.putLong(this.m_elementId);
        buffer.putInt(this.m_destination.getX());
        buffer.putInt(this.m_destination.getY());
        buffer.putShort(this.m_destination.getZ());
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 203;
    }
}
