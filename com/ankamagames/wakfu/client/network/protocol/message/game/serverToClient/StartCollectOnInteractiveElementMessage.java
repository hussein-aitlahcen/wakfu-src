package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import java.nio.*;

public class StartCollectOnInteractiveElementMessage extends AbstractOccupationModificationMessage
{
    private long m_interactifElementId;
    private long m_collectTime;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_modificationType = buffer.get();
        this.m_occupationType = buffer.getShort();
        this.m_concernedPlayerId = buffer.getLong();
        this.m_interactifElementId = buffer.getLong();
        this.m_collectTime = buffer.getLong();
        return true;
    }
    
    public long getCollectTime() {
        return this.m_collectTime;
    }
    
    @Override
    public int getId() {
        return 4206;
    }
    
    public long getInteractifElementId() {
        return this.m_interactifElementId;
    }
    
    @Override
    public String toString() {
        return "StartCollectOnInteractiveElementMessage{m_interactifElementId=" + this.m_interactifElementId + ", m_collectTime=" + this.m_collectTime + '}';
    }
}
