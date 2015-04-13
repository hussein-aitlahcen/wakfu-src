package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gems;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GemRequestMessage extends OutputOnlyProxyMessage
{
    private long m_gemmedItemId;
    private long m_gemItemId;
    private byte m_index;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(17);
        buffer.putLong(this.m_gemmedItemId);
        buffer.putLong(this.m_gemItemId);
        buffer.put(this.m_index);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void setGemmedItemId(final long gemmedItemId) {
        this.m_gemmedItemId = gemmedItemId;
    }
    
    public void setGemItemId(final long gemItemId) {
        this.m_gemItemId = gemItemId;
    }
    
    public void setIndex(final byte index) {
        this.m_index = index;
    }
    
    @Override
    public int getId() {
        return 13101;
    }
}
