package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class GuildChangeDescriptionRequestMessage extends OutputOnlyProxyMessage
{
    private String m_desc;
    
    public GuildChangeDescriptionRequestMessage(final String desc) {
        super();
        this.m_desc = desc;
    }
    
    @Override
    public byte[] encode() {
        final byte[] desc = StringUtils.toUTF8(this.m_desc);
        final ByteBuffer bb = ByteBuffer.allocate(4 + desc.length);
        bb.putInt(desc.length);
        bb.put(desc);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20077;
    }
}
