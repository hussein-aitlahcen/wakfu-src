package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ItemQuantityUpdateMessage extends InputOnlyProxyMessage
{
    private long m_itemId;
    private short m_quantity;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_itemId = buffer.getLong();
        this.m_quantity = buffer.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 5212;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
}
