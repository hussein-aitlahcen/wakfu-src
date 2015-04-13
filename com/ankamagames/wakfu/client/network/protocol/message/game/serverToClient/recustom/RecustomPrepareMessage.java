package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.recustom;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RecustomPrepareMessage extends InputOnlyProxyMessage
{
    private short m_recustomType;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_recustomType = buffer.getShort();
        return true;
    }
    
    public short getRecustomType() {
        return this.m_recustomType;
    }
    
    @Override
    public int getId() {
        return 15776;
    }
}
