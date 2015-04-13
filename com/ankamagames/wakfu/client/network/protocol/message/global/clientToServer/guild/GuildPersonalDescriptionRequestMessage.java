package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class GuildPersonalDescriptionRequestMessage extends OutputOnlyProxyMessage
{
    private String m_desc;
    
    public GuildPersonalDescriptionRequestMessage(final String desc) {
        super();
        this.m_desc = desc;
    }
    
    @Override
    public byte[] encode() {
        final byte[] desc = StringUtils.toUTF8(this.m_desc);
        final ByteBuffer bb = ByteBuffer.allocate(1 + desc.length);
        bb.put((byte)desc.length);
        bb.put(desc);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20081;
    }
}
