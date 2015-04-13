package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.errorMessage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class WorldPropertyTypeErrorMessage extends InputOnlyProxyMessage
{
    private short m_errorId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorId = bb.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 3221;
    }
    
    public short getErrorId() {
        return this.m_errorId;
    }
}
