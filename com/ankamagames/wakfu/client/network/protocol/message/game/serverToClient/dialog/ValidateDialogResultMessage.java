package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dialog;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ValidateDialogResultMessage extends InputOnlyProxyMessage
{
    private int m_dialogId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_dialogId = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15702;
    }
    
    public int getDialogId() {
        return this.m_dialogId;
    }
}
