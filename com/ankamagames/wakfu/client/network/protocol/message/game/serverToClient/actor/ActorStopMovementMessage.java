package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ActorStopMovementMessage extends InputOnlyProxyMessage
{
    private long m_actorId;
    private int m_x;
    private int m_y;
    private short m_z;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_actorId = buffer.getLong();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_z = buffer.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 4115;
    }
    
    public long getActorId() {
        return this.m_actorId;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public short getZ() {
        return this.m_z;
    }
}
