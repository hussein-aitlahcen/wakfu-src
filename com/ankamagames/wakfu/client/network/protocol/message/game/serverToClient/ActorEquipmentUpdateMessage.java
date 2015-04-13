package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class ActorEquipmentUpdateMessage extends InputOnlyProxyMessage
{
    private long m_actorId;
    private final TByteIntHashMap m_changedItems;
    
    public ActorEquipmentUpdateMessage() {
        super();
        this.m_changedItems = new TByteIntHashMap();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buf = ByteBuffer.wrap(rawDatas);
        this.m_actorId = buf.getLong();
        for (byte count = buf.get(), i = 0; i < count; ++i) {
            final byte pos = buf.get();
            this.m_changedItems.put(pos, buf.getInt());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 5206;
    }
    
    public TByteIntHashMap getUpdatedItems() {
        return this.m_changedItems;
    }
    
    public long getActorId() {
        return this.m_actorId;
    }
}
