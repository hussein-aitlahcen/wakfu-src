package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import java.nio.*;

public class MerchantItemUpdateMessage extends InputOnlyProxyMessage
{
    private long m_uniqueId;
    private short m_quantity;
    private int m_price;
    private PackType m_packType;
    
    public long getUniqueId() {
        return this.m_uniqueId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public int getPrice() {
        return this.m_price;
    }
    
    public PackType getPackType() {
        return this.m_packType;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_uniqueId = buffer.getLong();
        this.m_quantity = buffer.getShort();
        this.m_price = buffer.getInt();
        this.m_packType = PackType.fromQuantity(buffer.getShort());
        return true;
    }
    
    @Override
    public int getId() {
        return 5238;
    }
}
