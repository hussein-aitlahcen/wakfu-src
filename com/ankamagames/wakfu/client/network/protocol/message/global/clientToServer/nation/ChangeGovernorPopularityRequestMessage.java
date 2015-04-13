package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChangeGovernorPopularityRequestMessage extends OutputOnlyProxyMessage
{
    private byte m_opinionId;
    
    public void setOpinionId(final byte opinionId) {
        this.m_opinionId = opinionId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(this.m_opinionId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20041;
    }
}
