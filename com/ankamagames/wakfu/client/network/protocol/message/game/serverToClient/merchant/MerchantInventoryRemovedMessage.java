package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MerchantInventoryRemovedMessage extends InputOnlyProxyMessage
{
    private long m_merchantInventoryUid;
    
    public long getMerchantInventoryUid() {
        return this.m_merchantInventoryUid;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_merchantInventoryUid = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 10120;
    }
}
