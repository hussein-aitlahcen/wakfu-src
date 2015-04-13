package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;

public class ItemIdCacheUpdateMessage extends InputOnlyProxyMessage
{
    private boolean m_squashing;
    private final ArrayList<Long> m_uids;
    
    public ItemIdCacheUpdateMessage() {
        super();
        this.m_squashing = false;
        this.m_uids = new ArrayList<Long>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        if (buffer.get() == 0) {
            this.m_squashing = true;
        }
        final byte idsCount = buffer.get();
        for (int i = 0; i < idsCount; ++i) {
            this.m_uids.add(buffer.getLong());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 5300;
    }
    
    public boolean getSquashing() {
        return this.m_squashing;
    }
    
    public ArrayList<Long> getUniqueIds() {
        return this.m_uids;
    }
}
