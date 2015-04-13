package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorBuyRequestMessage extends OutputOnlyProxyMessage
{
    private int m_protectorId;
    private long m_merchantInventoryUid;
    private long m_itemUid;
    
    public void setProtectorId(final int protectorId) {
        this.m_protectorId = protectorId;
    }
    
    public void setMerchantInventoryUid(final long merchantInventoryUid) {
        this.m_merchantInventoryUid = merchantInventoryUid;
    }
    
    public void setItemUid(final long itemUid) {
        this.m_itemUid = itemUid;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.putInt(this.m_protectorId);
        buffer.putLong(this.m_merchantInventoryUid);
        buffer.putLong(this.m_itemUid);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15315;
    }
}
