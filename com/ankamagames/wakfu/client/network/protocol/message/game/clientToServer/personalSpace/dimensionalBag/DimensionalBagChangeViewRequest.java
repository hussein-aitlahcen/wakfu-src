package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagChangeViewRequest extends OutputOnlyProxyMessage
{
    private int m_viewModelId;
    
    public void setViewModelId(final int viewModelId) {
        this.m_viewModelId = viewModelId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(this.m_viewModelId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10047;
    }
}
