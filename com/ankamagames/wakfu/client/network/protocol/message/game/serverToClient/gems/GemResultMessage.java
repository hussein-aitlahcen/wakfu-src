package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gems;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GemResultMessage extends InputOnlyProxyMessage
{
    private long m_gemmedItemId;
    private int m_gemRefItemId;
    private byte m_index;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_gemmedItemId = bb.getLong();
        this.m_gemRefItemId = bb.getInt();
        this.m_index = bb.get();
        return true;
    }
    
    public long getGemmedItemId() {
        return this.m_gemmedItemId;
    }
    
    public int getGemRefItemId() {
        return this.m_gemRefItemId;
    }
    
    public byte getIndex() {
        return this.m_index;
    }
    
    @Override
    public int getId() {
        return 13102;
    }
    
    @Override
    public String toString() {
        return "GemResultMessage{m_gemmedItemId" + this.m_gemmedItemId + ", m_gemRefItemId" + this.m_gemRefItemId + '}';
    }
}
