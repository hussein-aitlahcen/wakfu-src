package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeAddItemMessage extends OutputOnlyProxyMessage
{
    private long m_itemId;
    private long m_exchangeId;
    private short m_itemQuantity;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 18;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_itemId);
        buffer.putLong(this.m_exchangeId);
        buffer.putShort(this.m_itemQuantity);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 6009;
    }
    
    public void setItemId(final long itemId) {
        this.m_itemId = itemId;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
    
    public void setItemQuantity(final short itemQuantity) {
        this.m_itemQuantity = itemQuantity;
    }
}
