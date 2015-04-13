package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildMoveRankRequestMessage extends OutputOnlyProxyMessage
{
    private long m_rankId;
    private short m_position;
    
    public GuildMoveRankRequestMessage(final long rankId, final short position) {
        super();
        this.m_rankId = rankId;
        this.m_position = position;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(10);
        bb.putLong(this.m_rankId);
        bb.putShort(this.m_position);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20068;
    }
}
