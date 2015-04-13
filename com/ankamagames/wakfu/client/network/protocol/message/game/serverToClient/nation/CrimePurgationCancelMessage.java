package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CrimePurgationCancelMessage extends InputOnlyProxyMessage
{
    private int m_nationId;
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_nationId = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15133;
    }
}
