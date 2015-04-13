package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RollRandomElementsRequestMessage extends OutputOnlyProxyMessage
{
    private long m_itemUid;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_itemUid);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void setItemUid(final long itemUid) {
        this.m_itemUid = itemUid;
    }
    
    @Override
    public int getId() {
        return 13011;
    }
}
