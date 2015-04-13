package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.market;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class MarketRemoveItemRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_entryId;
    
    public MarketRemoveItemRequestMessage(final long entryId) {
        super();
        this.m_entryId = entryId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_entryId);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 15267;
    }
}
