package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class MerchantItemAddMessage extends InputOnlyProxyMessage
{
    private long m_merchantInventoryUid;
    private short m_position;
    private RawMerchantItem m_item;
    
    public long getMerchantInventoryUid() {
        return this.m_merchantInventoryUid;
    }
    
    public short getPosition() {
        return this.m_position;
    }
    
    public RawMerchantItem getItem() {
        return this.m_item;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_merchantInventoryUid = buffer.getLong();
        this.m_position = buffer.getShort();
        (this.m_item = new RawMerchantItem()).unserialize(buffer);
        return buffer.remaining() == 0;
    }
    
    @Override
    public int getId() {
        return 5232;
    }
}
