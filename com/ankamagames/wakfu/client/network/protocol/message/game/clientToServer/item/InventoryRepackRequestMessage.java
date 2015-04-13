package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class InventoryRepackRequestMessage extends OutputOnlyProxyMessage
{
    private final byte m_bagPosition;
    private final short[] m_priorities;
    
    public InventoryRepackRequestMessage(final byte bagPosition, final short[] priorities) {
        super();
        this.m_bagPosition = bagPosition;
        this.m_priorities = priorities;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.put(this.m_bagPosition);
        ba.put((byte)this.m_priorities.length);
        for (final short priority : this.m_priorities) {
            ba.putShort(priority);
        }
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 5250;
    }
}
