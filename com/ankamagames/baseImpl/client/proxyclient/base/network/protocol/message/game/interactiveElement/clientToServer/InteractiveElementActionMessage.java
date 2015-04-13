package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class InteractiveElementActionMessage extends OutputOnlyProxyMessage
{
    private long m_elementId;
    private short m_actionId;
    
    public void setElementId(final long elementId) {
        this.m_elementId = elementId;
    }
    
    public void setActionId(final short actionId) {
        this.m_actionId = actionId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putLong(this.m_elementId);
        buffer.putShort(this.m_actionId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 201;
    }
}
