package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class CompanionUpdateUnlockedMessage extends InputOnlyProxyMessage
{
    private long m_companionId;
    private boolean m_isUnlocked;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_companionId = bb.getLong();
        this.m_isUnlocked = (bb.get() == 1);
        return true;
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    public boolean isUnlocked() {
        return this.m_isUnlocked;
    }
    
    @Override
    public int getId() {
        return 5562;
    }
    
    @Override
    public String toString() {
        return "CompanionUpdateUnlockedMessage{m_companionId=" + this.m_companionId + ", m_isUnlocked=" + this.m_isUnlocked + '}';
    }
}
