package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class AddHeroToGroupRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_heroId;
    
    public AddHeroToGroupRequestMessage(final long heroId) {
        super();
        this.m_heroId = heroId;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_heroId);
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 5564;
    }
    
    @Override
    public String toString() {
        return "AddHeroToGroupRequestMessage{m_heroId=" + this.m_heroId + '}';
    }
}
