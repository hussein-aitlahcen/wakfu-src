package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class GuildAddMemberRequestMessage extends OutputOnlyProxyMessage
{
    private String m_requestedName;
    
    public GuildAddMemberRequestMessage(final String requestedName) {
        super();
        this.m_requestedName = requestedName;
    }
    
    @Override
    public byte[] encode() {
        final byte[] name = StringUtils.toUTF8(this.m_requestedName);
        final ByteBuffer bb = ByteBuffer.allocate(1 + name.length);
        bb.put((byte)name.length);
        bb.put(name);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20055;
    }
}
