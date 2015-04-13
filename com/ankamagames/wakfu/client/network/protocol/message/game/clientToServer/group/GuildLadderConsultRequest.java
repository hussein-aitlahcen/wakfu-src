package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildLadderConsultRequest extends OutputOnlyProxyMessage
{
    private byte m_sortingType;
    private short m_index;
    
    public GuildLadderConsultRequest() {
        super();
        this.m_sortingType = -1;
        this.m_index = 0;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(3);
        bb.put(this.m_sortingType);
        bb.putShort(this.m_index);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20085;
    }
    
    public void setIndex(final short index) {
        this.m_index = index;
    }
    
    public void setSortingType(final byte sortingType) {
        this.m_sortingType = sortingType;
    }
}
