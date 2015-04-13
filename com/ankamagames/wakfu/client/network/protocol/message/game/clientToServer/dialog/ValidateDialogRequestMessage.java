package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.dialog;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ValidateDialogRequestMessage extends OutputOnlyProxyMessage
{
    private int m_dialogId;
    private int m_choiceId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(this.m_dialogId);
        buffer.putInt(this.m_choiceId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15701;
    }
    
    public void setDialogId(final int id) {
        this.m_dialogId = id;
    }
    
    public void setChoiceId(final int choiceId) {
        this.m_choiceId = choiceId;
    }
}
