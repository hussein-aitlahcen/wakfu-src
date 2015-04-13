package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.dialog;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CancelDialogRequestMessage extends OutputOnlyProxyMessage
{
    private int m_dialogId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(this.m_dialogId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15703;
    }
    
    public void setDialogId(final int id) {
        this.m_dialogId = id;
    }
}
