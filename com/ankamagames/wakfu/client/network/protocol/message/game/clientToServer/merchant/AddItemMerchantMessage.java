package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AddItemMerchantMessage extends OutputOnlyProxyMessage
{
    private long m_sourceItemUid;
    private byte m_position;
    private short m_quantity;
    private long m_resultantItemUid;
    private long m_destinationMerchantInventoryUid;
    private short m_packSize;
    private int m_price;
    
    public AddItemMerchantMessage() {
        super();
        this.m_quantity = -1;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(this.m_sourceItemUid);
        buffer.put(this.m_position);
        buffer.putShort(this.m_quantity);
        buffer.putLong(this.m_resultantItemUid);
        buffer.putLong(this.m_destinationMerchantInventoryUid);
        buffer.putShort(this.m_packSize);
        buffer.putInt(this.m_price);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5229;
    }
    
    public void setSourceItemUid(final long sourceItemUid) {
        this.m_sourceItemUid = sourceItemUid;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    public void setResultantItemUid(final long resultantItemUid) {
        this.m_resultantItemUid = resultantItemUid;
    }
    
    public void setPosition(final byte position) {
        this.m_position = position;
    }
    
    public void setDestinationMerchantInventoryUid(final long destinationMerchantInventoryUid) {
        this.m_destinationMerchantInventoryUid = destinationMerchantInventoryUid;
    }
    
    public void setPackSize(final short packSize) {
        this.m_packSize = packSize;
    }
    
    public void setPrice(final int price) {
        this.m_price = price;
    }
}
