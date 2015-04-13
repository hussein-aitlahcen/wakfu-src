package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ActorTeleportMessage extends InputOnlyProxyMessage
{
    private long m_actorId;
    private int m_x;
    private int m_y;
    private short m_altitude;
    private boolean m_generateMove;
    private boolean m_forceLoading;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 20, true)) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_actorId = buffer.getLong();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_altitude = buffer.getShort();
        this.m_generateMove = (buffer.get() == 1);
        this.m_forceLoading = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 4126;
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
    
    public short getAltitude() {
        return this.m_altitude;
    }
    
    public boolean isGenerateMove() {
        return this.m_generateMove;
    }
    
    public boolean forceLoading() {
        return this.m_forceLoading;
    }
}
