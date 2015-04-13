package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CreateGuildAnswerMessage extends InputOnlyProxyMessage
{
    private int m_errorCode;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.getInt();
        return false;
    }
    
    @Override
    public int getId() {
        return 20054;
    }
    
    public int getErrorCode() {
        return this.m_errorCode;
    }
}
