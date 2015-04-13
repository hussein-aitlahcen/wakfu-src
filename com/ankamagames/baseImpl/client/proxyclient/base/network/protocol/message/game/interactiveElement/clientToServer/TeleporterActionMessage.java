package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class TeleporterActionMessage extends OutputOnlyProxyMessage
{
    private long m_elementId;
    private int m_exitId;
    
    public void setElementId(final long elementId) {
        this.m_elementId = elementId;
    }
    
    public void setExitId(final int exitId) {
        this.m_exitId = exitId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putLong(this.m_elementId);
        buffer.putInt(this.m_exitId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public long getElementId() {
        return this.m_elementId;
    }
    
    @Override
    public int getId() {
        return 205;
    }
}
