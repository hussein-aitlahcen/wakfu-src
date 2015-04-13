package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gems;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GemImproveRequestMessage extends OutputOnlyProxyMessage
{
    private long m_gemmedItemId;
    private byte m_slotIndex;
    private int m_toolGemRefId;
    private int m_toolGemRefId2;
    private boolean m_withHammer;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(18);
        buffer.putLong(this.m_gemmedItemId);
        buffer.put(this.m_slotIndex);
        buffer.putInt(this.m_toolGemRefId);
        buffer.putInt(this.m_toolGemRefId2);
        buffer.put((byte)(this.m_withHammer ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void setGemmedItemId(final long gemmedItemId) {
        this.m_gemmedItemId = gemmedItemId;
    }
    
    public void setSlotIndex(final byte slotIndex) {
        this.m_slotIndex = slotIndex;
    }
    
    public void setToolGemRefId(final int toolGemRefId) {
        this.m_toolGemRefId = toolGemRefId;
    }
    
    public void setToolGemRefId2(final int toolGemRefId2) {
        this.m_toolGemRefId2 = toolGemRefId2;
    }
    
    public void setWithHammer(final boolean withHammer) {
        this.m_withHammer = withHammer;
    }
    
    @Override
    public int getId() {
        return 13107;
    }
}
