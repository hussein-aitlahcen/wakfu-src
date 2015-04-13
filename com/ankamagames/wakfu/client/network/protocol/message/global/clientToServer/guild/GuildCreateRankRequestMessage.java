package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class GuildCreateRankRequestMessage extends OutputOnlyProxyMessage
{
    private String m_rankName;
    private long m_authorisations;
    
    public GuildCreateRankRequestMessage(final String rankName, final long authorisations) {
        super();
        this.m_rankName = rankName;
        this.m_authorisations = authorisations;
    }
    
    @Override
    public byte[] encode() {
        final byte[] name = StringUtils.toUTF8(this.m_rankName);
        final ByteBuffer bb = ByteBuffer.allocate(1 + name.length + 8);
        bb.put((byte)name.length);
        bb.put(name);
        bb.putLong(this.m_authorisations);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20065;
    }
}
