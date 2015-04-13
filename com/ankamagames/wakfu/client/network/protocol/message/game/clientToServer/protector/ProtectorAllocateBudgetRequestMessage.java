package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.nio.*;

public class ProtectorAllocateBudgetRequestMessage extends OutputOnlyProxyMessage
{
    private int m_protectorId;
    private int m_cashAmount;
    private byte m_walletContextIdx;
    
    public void setBudget(final int protectorId, final ProtectorWalletContext context, final int cashAmount) {
        this.m_protectorId = protectorId;
        this.m_walletContextIdx = context.idx;
        this.m_cashAmount = cashAmount;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putInt(this.m_protectorId);
        buffer.putInt(this.m_cashAmount);
        buffer.put(this.m_walletContextIdx);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15331;
    }
}
