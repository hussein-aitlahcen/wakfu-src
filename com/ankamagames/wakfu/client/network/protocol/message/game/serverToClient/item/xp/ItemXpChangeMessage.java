package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.xp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ItemXpChangeMessage extends InputOnlyProxyMessage
{
    private long m_itemId;
    private long m_xp;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_itemId = bb.getLong();
        this.m_xp = bb.getLong();
        return true;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public long getXp() {
        return this.m_xp;
    }
    
    @Override
    public int getId() {
        return 15990;
    }
    
    @Override
    public String toString() {
        return "ItemXpChangeMessage{m_itemId=" + this.m_itemId + ", m_xp=" + this.m_xp + '}';
    }
}
