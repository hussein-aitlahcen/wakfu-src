package com.ankamagames.wakfu.client.ui.protocol.message.exchange;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIExchangeMoveItemMessage extends UIMessage
{
    private short m_position;
    private Item m_item;
    private long m_exchangeId;
    private int m_itemQuantity;
    
    public int getItemQuantity() {
        return this.m_itemQuantity;
    }
    
    public void setItemQuantity(final int quantity) {
        this.m_itemQuantity = quantity;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public void setItem(final Item item) {
        this.m_item = item;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
    
    public short getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final short position) {
        this.m_position = position;
    }
}
