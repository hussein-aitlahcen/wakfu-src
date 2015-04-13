package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.monster;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class MonsterSpeakMessage extends InputOnlyProxyMessage
{
    private long m_monsterId;
    private String m_sentenceKey;
    
    public long getMonsterId() {
        return this.m_monsterId;
    }
    
    public String getSentenceKey() {
        return this.m_sentenceKey;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_monsterId = buffer.getLong();
        final short sentenceKeyLength = buffer.getShort();
        final byte[] sentenceKeyUTF8 = new byte[sentenceKeyLength];
        buffer.get(sentenceKeyUTF8);
        this.m_sentenceKey = StringUtils.fromUTF8(sentenceKeyUTF8);
        return true;
    }
    
    @Override
    public int getId() {
        return 15405;
    }
}
