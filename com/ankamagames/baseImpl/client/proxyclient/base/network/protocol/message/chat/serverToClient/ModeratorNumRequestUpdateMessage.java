package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ModeratorNumRequestUpdateMessage extends InputOnlyProxyMessage
{
    private int m_numRequester;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_numRequester = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 3180;
    }
    
    public int getNumRequester() {
        return this.m_numRequester;
    }
}
