package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class InteractiveElementDespawnMessage extends InputOnlyProxyMessage
{
    private final TLongArrayList m_interactiveElementIds;
    
    public InteractiveElementDespawnMessage() {
        super();
        this.m_interactiveElementIds = new TLongArrayList();
    }
    
    public final TLongArrayList getInteractiveElementIds() {
        return this.m_interactiveElementIds;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final int count = buffer.getShort();
        this.m_interactiveElementIds.resetQuick();
        this.m_interactiveElementIds.ensureCapacity(count);
        for (int i = 0; i < count; ++i) {
            final long id = buffer.getLong();
            this.m_interactiveElementIds.add(id);
        }
        return true;
    }
    
    @Override
    public final int getId() {
        return 206;
    }
}
