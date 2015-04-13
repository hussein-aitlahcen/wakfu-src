package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MerchantItemRemoveMessage extends InputOnlyProxyMessage
{
    private long m_itemUniqueId;
    private long m_merchantInventoryUid;
    
    public long getItemUniqueId() {
        return this.m_itemUniqueId;
    }
    
    public long getMerchantInventoryUid() {
        return this.m_merchantInventoryUid;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_itemUniqueId = buffer.getLong();
        this.m_merchantInventoryUid = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 5236;
    }
}
