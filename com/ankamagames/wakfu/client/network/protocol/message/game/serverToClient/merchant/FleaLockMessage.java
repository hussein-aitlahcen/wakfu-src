package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FleaLockMessage extends InputOnlyProxyMessage
{
    private long m_merchantInventoryUid;
    private boolean m_locked;
    
    public long getMerchantInventoryUid() {
        return this.m_merchantInventoryUid;
    }
    
    public boolean isLocked() {
        return this.m_locked;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_merchantInventoryUid = buffer.getLong();
        this.m_locked = (buffer.get() != 0);
        return buffer.remaining() == 0;
    }
    
    @Override
    public int getId() {
        return 10108;
    }
}
