package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.craft;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CraftXpGainedMessage extends InputOnlyProxyMessage
{
    private int m_refCraftId;
    private long m_xpAdded;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_refCraftId = bb.getInt();
        this.m_xpAdded = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15714;
    }
    
    public int getRefCraftId() {
        return this.m_refCraftId;
    }
    
    public long getXpAdded() {
        return this.m_xpAdded;
    }
}
