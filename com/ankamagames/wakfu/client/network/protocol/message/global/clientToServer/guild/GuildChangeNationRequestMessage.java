package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class GuildChangeNationRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_nationId;
    
    public GuildChangeNationRequestMessage(final int nationId) {
        super();
        this.m_nationId = nationId;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray bb = new ByteArray();
        bb.putInt(this.m_nationId);
        return this.addClientHeader((byte)6, bb.toArray());
    }
    
    @Override
    public int getId() {
        return 20074;
    }
}
