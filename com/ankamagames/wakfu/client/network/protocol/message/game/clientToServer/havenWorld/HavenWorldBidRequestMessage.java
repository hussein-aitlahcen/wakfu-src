package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class HavenWorldBidRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_havenWorldId;
    private final int m_bidValue;
    
    public HavenWorldBidRequestMessage(final int havenWorldId, final int bidValue) {
        super();
        this.m_havenWorldId = havenWorldId;
        this.m_bidValue = bidValue;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray byteArray = new ByteArray();
        byteArray.putInt(this.m_havenWorldId);
        byteArray.putInt(this.m_bidValue);
        return this.addClientHeader((byte)6, byteArray.toArray());
    }
    
    @Override
    public int getId() {
        return 597;
    }
}
