package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gems;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GemMergeRequestMessage extends OutputOnlyProxyMessage
{
    private int m_gemRefId;
    private byte m_mergeType;
    private short m_quantity;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(7);
        buffer.putInt(this.m_gemRefId);
        buffer.put(this.m_mergeType);
        buffer.putShort(this.m_quantity);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void setGemRefId(final int gemRefId) {
        this.m_gemRefId = gemRefId;
    }
    
    public void setMergeType(final byte mergeType) {
        this.m_mergeType = mergeType;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    @Override
    public int getId() {
        return 13105;
    }
}
