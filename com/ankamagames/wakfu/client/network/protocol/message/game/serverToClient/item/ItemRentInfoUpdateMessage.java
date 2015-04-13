package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public final class ItemRentInfoUpdateMessage extends InputOnlyProxyMessage
{
    private long m_itemId;
    private RawRentInfo m_rawRentInfo;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_itemId = bb.getLong();
        (this.m_rawRentInfo = new RawRentInfo()).unserialize(bb);
        return true;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public RawRentInfo getRawRentInfo() {
        return this.m_rawRentInfo;
    }
    
    @Override
    public int getId() {
        return 5251;
    }
}
