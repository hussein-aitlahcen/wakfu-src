package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import java.nio.*;

public class SitOccupationMessage extends AbstractOccupationModificationMessage
{
    private long m_interactiveElementId;
    private long m_userId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_modificationType = buffer.get();
        this.m_occupationType = buffer.getShort();
        this.m_interactiveElementId = buffer.getLong();
        this.m_userId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15724;
    }
    
    public long getInteractiveElementId() {
        return this.m_interactiveElementId;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
}
