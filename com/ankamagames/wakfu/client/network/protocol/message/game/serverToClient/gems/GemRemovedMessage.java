package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gems;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GemRemovedMessage extends InputOnlyProxyMessage
{
    private long m_gemmedItemId;
    private byte m_index;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_gemmedItemId = bb.getLong();
        this.m_index = bb.get();
        return true;
    }
    
    public long getGemmedItemId() {
        return this.m_gemmedItemId;
    }
    
    public byte getIndex() {
        return this.m_index;
    }
    
    @Override
    public int getId() {
        return 13103;
    }
    
    @Override
    public String toString() {
        return "GemResultMessage{m_gemmedItemId" + this.m_gemmedItemId + ", m_index=" + this.m_index + '}';
    }
}
