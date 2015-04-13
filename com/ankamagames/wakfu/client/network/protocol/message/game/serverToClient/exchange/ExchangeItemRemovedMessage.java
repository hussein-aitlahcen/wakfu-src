package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;

public class ExchangeItemRemovedMessage extends InputOnlyProxyMessage
{
    private long m_exchangeId;
    private Item m_item;
    private long m_userId;
    private short m_itemQuantity;
    private byte m_validity;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_exchangeId = buffer.getLong();
        this.m_userId = buffer.getLong();
        this.m_validity = buffer.get();
        this.m_itemQuantity = buffer.getShort();
        final RawInventoryItem rawItem = new RawInventoryItem();
        if (rawItem.unserialize(buffer)) {
            (this.m_item = new Item()).fromRaw(rawItem);
            return true;
        }
        return false;
    }
    
    @Override
    public int getId() {
        return 6012;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
    
    public byte getValidity() {
        return this.m_validity;
    }
    
    public short getItemQuantity() {
        return this.m_itemQuantity;
    }
}
