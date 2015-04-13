package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class ActorPathUpdateMessage extends InputOnlyProxyMessage
{
    private long m_actorId;
    private Direction8Path m_path;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length < 19) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_actorId = buffer.getLong();
        this.m_path = Direction8Path.decodeFromBuffer(buffer);
        return this.m_path != null;
    }
    
    @Override
    public int getId() {
        return 4114;
    }
    
    public long getActorId() {
        return this.m_actorId;
    }
    
    public Direction8Path getPath() {
        return this.m_path;
    }
}
