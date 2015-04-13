package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.bind;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ItemBindMessage extends InputOnlyProxyMessage
{
    private long m_itemId;
    private long m_data;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_itemId = bb.getLong();
        this.m_data = bb.getLong();
        return true;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public long getData() {
        return this.m_data;
    }
    
    @Override
    public int getId() {
        return 15992;
    }
    
    @Override
    public String toString() {
        return "ItemBindMessage{m_itemId=" + this.m_itemId + ", m_data=" + this.m_data + '}';
    }
}
