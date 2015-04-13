package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildDeleteRankRequestMessage extends OutputOnlyProxyMessage
{
    private long m_rankId;
    
    public GuildDeleteRankRequestMessage(final long rankId) {
        super();
        this.m_rankId = rankId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_rankId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20067;
    }
}
