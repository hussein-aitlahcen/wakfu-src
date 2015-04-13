package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class ActorChangeDirectionMessage extends InputOnlyProxyMessage
{
    private long m_playerId;
    private Direction8 m_direction;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_playerId = buffer.getLong();
        this.m_direction = Direction8.getDirectionFromIndex(buffer.get());
        return true;
    }
    
    @Override
    public int getId() {
        return 4118;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    public int getActionId() {
        return 0;
    }
}
