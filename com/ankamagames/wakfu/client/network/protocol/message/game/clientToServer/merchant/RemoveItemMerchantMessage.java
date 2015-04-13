package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RemoveItemMerchantMessage extends OutputOnlyProxyMessage
{
    private long m_sourceMerchantInventoryUid;
    private long m_destinationContainer;
    private short m_destinationPosition;
    private long m_itemUid;
    private short m_quantity;
    private long m_newItemUid;
    
    public RemoveItemMerchantMessage() {
        super();
        this.m_quantity = -1;
        this.m_newItemUid = 0L;
    }
    
    @Override
    public byte[] encode() {
        int sizeDatas = 28;
        if (this.m_newItemUid != 0L && this.m_quantity >= 0) {
            sizeDatas += 8;
        }
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_sourceMerchantInventoryUid);
        buffer.putLong(this.m_itemUid);
        buffer.putLong(this.m_destinationContainer);
        buffer.putShort(this.m_destinationPosition);
        if (this.m_quantity >= 0 && this.m_newItemUid != 0L) {
            buffer.putShort(this.m_quantity);
            buffer.putLong(this.m_newItemUid);
        }
        else {
            buffer.putShort(this.m_quantity);
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5231;
    }
    
    public void setDestinationContainer(final long destinationContainer) {
        this.m_destinationContainer = destinationContainer;
    }
    
    public void setDestinationPosition(final short destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
    
    public void setItemUid(final long itemUid) {
        this.m_itemUid = itemUid;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    public void setNewItemUid(final long newItemUid) {
        this.m_newItemUid = newItemUid;
    }
    
    public void setSourceMerchantInventoryUid(final long sourceMerchantInventoryUid) {
        this.m_sourceMerchantInventoryUid = sourceMerchantInventoryUid;
    }
}
