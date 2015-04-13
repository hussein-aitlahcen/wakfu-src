package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class CompanionUpdateXpMessage extends InputOnlyProxyMessage
{
    private long m_companionId;
    private long m_xp;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_companionId = bb.getLong();
        this.m_xp = bb.getLong();
        return false;
    }
    
    public long getXp() {
        return this.m_xp;
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    @Override
    public int getId() {
        return 5559;
    }
    
    @Override
    public String toString() {
        return "CompanionUpdateXpMessage{m_companionId=" + this.m_companionId + ", m_xp=" + this.m_xp + '}';
    }
}
