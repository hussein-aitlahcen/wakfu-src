package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.market;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class MarketAddItemRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_itemUid;
    private final byte m_packType;
    private final short m_packNumber;
    private final int m_packPrice;
    private final byte m_duration;
    
    public MarketAddItemRequestMessage(final long itemUid, final byte packType, final short packNumber, final int packPrice, final byte auctionDuration) {
        super();
        this.m_itemUid = itemUid;
        this.m_packType = packType;
        this.m_packNumber = packNumber;
        this.m_packPrice = packPrice;
        this.m_duration = auctionDuration;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(this.m_itemUid);
        bb.put(this.m_packType);
        bb.putShort(this.m_packNumber);
        bb.putInt(this.m_packPrice);
        bb.put(this.m_duration);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 15261;
    }
}
