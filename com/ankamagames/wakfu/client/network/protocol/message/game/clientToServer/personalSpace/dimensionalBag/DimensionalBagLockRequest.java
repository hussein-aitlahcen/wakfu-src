package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagLockRequest extends OutputOnlyProxyMessage
{
    private boolean m_isLockRequest;
    
    public void setLockRequest(final boolean lockRequest) {
        this.m_isLockRequest = lockRequest;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte)(this.m_isLockRequest ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10045;
    }
}
