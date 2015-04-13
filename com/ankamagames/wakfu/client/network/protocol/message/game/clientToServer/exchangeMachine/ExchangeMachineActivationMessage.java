package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchangeMachine;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeMachineActivationMessage extends OutputOnlyProxyMessage
{
    private long m_machineId;
    private int m_exchangeId;
    
    public ExchangeMachineActivationMessage(final long machineId, final int exchangeId) {
        super();
        this.m_machineId = machineId;
        this.m_exchangeId = exchangeId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putLong(this.m_machineId);
        buffer.putInt(this.m_exchangeId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15941;
    }
}
