package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildRemoveMemberRequestMessage extends OutputOnlyProxyMessage
{
    private long m_requestedId;
    
    public GuildRemoveMemberRequestMessage(final long requestedId) {
        super();
        this.m_requestedId = requestedId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_requestedId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20060;
    }
}
