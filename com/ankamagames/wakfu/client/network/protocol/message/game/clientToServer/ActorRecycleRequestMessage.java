package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import gnu.trove.*;

public class ActorRecycleRequestMessage extends OutputOnlyProxyMessage
{
    private long m_interactiveElementId;
    private TLongShortHashMap m_itemsToRecycle;
    
    public ActorRecycleRequestMessage(final long interactiveElementId, final TLongShortHashMap itemsToRecycle) {
        super();
        this.m_interactiveElementId = interactiveElementId;
        this.m_itemsToRecycle = itemsToRecycle;
    }
    
    @Override
    public byte[] encode() {
        final int dataSize = 12 + 10 * this.m_itemsToRecycle.size();
        final ByteBuffer buffer = ByteBuffer.allocate(dataSize);
        buffer.putLong(this.m_interactiveElementId);
        buffer.putInt(this.m_itemsToRecycle.size());
        this.m_itemsToRecycle.forEachEntry(new TLongShortProcedure() {
            @Override
            public boolean execute(final long itemUID, final short quantity) {
                buffer.putLong(itemUID);
                buffer.putShort(quantity);
                return true;
            }
        });
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4183;
    }
}
