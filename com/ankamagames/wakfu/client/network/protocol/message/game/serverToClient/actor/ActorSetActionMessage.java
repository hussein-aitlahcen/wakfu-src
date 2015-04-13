package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ActorSetActionMessage extends InputOnlyProxyMessage
{
    private long m_actorId;
    private int m_actionId;
    private int m_resX;
    private int m_resY;
    private byte m_end;
    private byte m_needHelp;
    private long m_elementId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_actorId = buffer.getLong();
        this.m_actionId = buffer.getInt();
        this.m_resX = buffer.getInt();
        this.m_resY = buffer.getInt();
        this.m_end = buffer.get();
        this.m_needHelp = buffer.get();
        this.m_elementId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 4116;
    }
    
    public long getActorId() {
        return this.m_actorId;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
    
    public int getResX() {
        return this.m_resX;
    }
    
    public int getResY() {
        return this.m_resY;
    }
    
    public boolean isEnd() {
        return this.m_end == 1;
    }
    
    public boolean isNeedHelp() {
        return this.m_needHelp == 1;
    }
    
    public long getElementId() {
        return this.m_elementId;
    }
}
