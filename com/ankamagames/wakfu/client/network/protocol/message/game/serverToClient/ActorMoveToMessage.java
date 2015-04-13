package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class ActorMoveToMessage extends InputOnlyProxyMessage
{
    private long m_actorId;
    private int m_x;
    private int m_y;
    private short m_z;
    private byte m_direction;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length < 19) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_actorId = buffer.getLong();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_z = buffer.getShort();
        this.m_direction = buffer.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 4127;
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
    
    public byte getDirection() {
        return this.m_direction;
    }
}
