package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.market;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class MarketPurchaseRequestMessage extends OutputOnlyProxyMessage
{
    private long m_entryId;
    private short m_packQuantity;
    
    public MarketPurchaseRequestMessage() {
        super();
        this.m_entryId = -1L;
    }
    
    public void setEntryId(final long entryId) {
        this.m_entryId = entryId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(10);
        bb.putLong(this.m_entryId);
        bb.putShort(this.m_packQuantity);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 15265;
    }
    
    public void setPackQuantity(final short packQuantity) {
        this.m_packQuantity = packQuantity;
    }
}
