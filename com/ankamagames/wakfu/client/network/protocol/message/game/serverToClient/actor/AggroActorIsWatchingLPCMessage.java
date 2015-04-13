package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import java.util.*;

public class AggroActorIsWatchingLPCMessage extends InputOnlyProxyMessage
{
    private HashMap<Long, Byte> m_actorId;
    
    public AggroActorIsWatchingLPCMessage() {
        super();
        this.m_actorId = new HashMap<Long, Byte>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        for (int i = buffer.get() - 1; i >= 0; --i) {
            final long id = buffer.getLong();
            final byte status = buffer.get();
            this.m_actorId.put(id, status);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8036;
    }
    
    public Iterator<Map.Entry<Long, Byte>> getActorIdIterator() {
        return this.m_actorId.entrySet().iterator();
    }
}
