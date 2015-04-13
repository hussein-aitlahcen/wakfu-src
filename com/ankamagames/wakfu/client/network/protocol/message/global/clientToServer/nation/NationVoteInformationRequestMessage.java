package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class NationVoteInformationRequestMessage extends OutputOnlyProxyMessage
{
    private int m_offset;
    
    public NationVoteInformationRequestMessage() {
        super();
        this.m_offset = 0;
    }
    
    public void setOffset(final int offset) {
        this.m_offset = offset;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_offset);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20001;
    }
}
