package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildInvitationResultMessage extends OutputOnlyProxyMessage
{
    private final boolean m_accepted;
    
    public GuildInvitationResultMessage(final boolean accepted) {
        super();
        this.m_accepted = accepted;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put((byte)(this.m_accepted ? 1 : 0));
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20058;
    }
    
    @Override
    public String toString() {
        return "GuildInvitationResultMessage{m_accepted=" + this.m_accepted + '}';
    }
}
