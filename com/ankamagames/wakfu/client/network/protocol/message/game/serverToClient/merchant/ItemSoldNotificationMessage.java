package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ItemSoldNotificationMessage extends InputOnlyProxyMessage
{
    private int m_itemRefId;
    private short m_quantity;
    private long m_totalKamas;
    
    public int getItemRefId() {
        return this.m_itemRefId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public long getTotalKamas() {
        return this.m_totalKamas;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length == 14) {
            final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
            this.m_itemRefId = buffer.getInt();
            this.m_quantity = buffer.getShort();
            this.m_totalKamas = buffer.getLong();
            return true;
        }
        return false;
    }
    
    @Override
    public int getId() {
        return 5244;
    }
}
