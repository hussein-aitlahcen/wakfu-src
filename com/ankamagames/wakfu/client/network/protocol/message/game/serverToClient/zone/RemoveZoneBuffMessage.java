package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.zone;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RemoveZoneBuffMessage extends InputOnlyProxyMessage
{
    private int m_buffId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length != 4) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_buffId = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15202;
    }
    
    public int getBuffId() {
        return this.m_buffId;
    }
}
