package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SelectNationRequestMessage extends OutputOnlyProxyMessage
{
    private int m_nationId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.putInt(this.m_nationId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15643;
    }
    
    public void setNationId(final int nationId) {
        this.m_nationId = nationId;
    }
}
