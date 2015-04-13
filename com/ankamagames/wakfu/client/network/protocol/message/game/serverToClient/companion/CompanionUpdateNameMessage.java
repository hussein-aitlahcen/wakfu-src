package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class CompanionUpdateNameMessage extends InputOnlyProxyMessage
{
    private long m_companionId;
    private String m_name;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_companionId = bb.getLong();
        final byte[] bytes = new byte[bb.getInt()];
        bb.get(bytes);
        this.m_name = StringUtils.fromUTF8(bytes);
        return false;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    @Override
    public int getId() {
        return 5552;
    }
    
    @Override
    public String toString() {
        return "CompanionUpdateNameMessage{m_companionId=" + this.m_companionId + ", m_name='" + this.m_name + '\'' + '}';
    }
}
