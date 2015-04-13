package com.ankamagames.wakfu.client.ui.protocol.message.Merchant;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class UIMerchantMessage extends UIMessage
{
    private Item m_item;
    private short m_destinationPosition;
    private long m_containerId;
    private MerchantInventoryItem m_merchantItem;
    private int m_x;
    private int m_y;
    private short m_quantity;
    
    public UIMerchantMessage() {
        super();
        this.m_destinationPosition = -1;
        this.m_containerId = -1L;
        this.m_quantity = -1;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public void setItem(final Item item) {
        this.m_item = item;
    }
    
    public short getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    public void setDestinationPosition(final short destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    public MerchantInventoryItem getMerchantItem() {
        return this.m_merchantItem;
    }
    
    public void setMerchantItem(final MerchantInventoryItem merchantItem) {
        this.m_merchantItem = merchantItem;
    }
    
    public long getContainerId() {
        return this.m_containerId;
    }
    
    public void setContainerId(final long containerId) {
        this.m_containerId = containerId;
    }
}
