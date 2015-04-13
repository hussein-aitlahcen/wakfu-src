package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.dialog;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class StartDialogRequestMessage extends OutputOnlyProxyMessage
{
    private int m_dialogId;
    private byte m_sourceType;
    private long m_sourceId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(13);
        buffer.putInt(this.m_dialogId);
        buffer.put(this.m_sourceType);
        buffer.putLong(this.m_sourceId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15705;
    }
    
    public void setDialogId(final int id) {
        this.m_dialogId = id;
    }
    
    public void setSourceType(final byte sourceType) {
        this.m_sourceType = sourceType;
    }
    
    public void setSourceId(final long sourceId) {
        this.m_sourceId = sourceId;
    }
}
