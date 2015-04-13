package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorBudgetTransferRequestMessage extends OutputOnlyProxyMessage
{
    private int m_sourceProtectorId;
    private int m_destProtectorId;
    private int m_amount;
    
    public void setSourceProtectorId(final int sourceProtectorId) {
        this.m_sourceProtectorId = sourceProtectorId;
    }
    
    public void setDestProtectorId(final int destProtectorId) {
        this.m_destProtectorId = destProtectorId;
    }
    
    public void setAmount(final int amount) {
        this.m_amount = amount;
    }
    
    @Override
    public byte[] encode() {
        final byte[] array = new byte[12];
        final ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.putInt(this.m_sourceProtectorId);
        buffer.putInt(this.m_destProtectorId);
        buffer.putInt(this.m_amount);
        return this.addClientHeader((byte)6, array);
    }
    
    @Override
    public int getId() {
        return 20043;
    }
}
