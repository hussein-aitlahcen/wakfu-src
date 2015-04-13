package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildChangeMemberRankRequestMessage extends OutputOnlyProxyMessage
{
    private long m_requestedId;
    private long m_rankId;
    
    public GuildChangeMemberRankRequestMessage(final long requestedId, final long rankId) {
        super();
        this.m_requestedId = requestedId;
        this.m_rankId = rankId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(this.m_requestedId);
        bb.putLong(this.m_rankId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20071;
    }
}
