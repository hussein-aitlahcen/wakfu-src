package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ErrorResultMessage extends InputOnlyProxyMessage
{
    private int m_resultId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_resultId = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4000;
    }
    
    public int getResultId() {
        return this.m_resultId;
    }
}
