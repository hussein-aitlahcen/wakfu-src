package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class GuildEditRankRequestMessage extends OutputOnlyProxyMessage
{
    private long m_rankId;
    private long m_authorisations;
    private String m_name;
    
    public GuildEditRankRequestMessage(final long rankId, final long authorisations, final String name) {
        super();
        this.m_rankId = rankId;
        this.m_authorisations = authorisations;
        this.m_name = name;
    }
    
    @Override
    public byte[] encode() {
        final byte[] name = StringUtils.toUTF8(this.m_name);
        final ByteBuffer bb = ByteBuffer.allocate(17 + name.length);
        bb.putLong(this.m_rankId);
        bb.putLong(this.m_authorisations);
        bb.put((byte)name.length);
        bb.put(name);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20069;
    }
}
