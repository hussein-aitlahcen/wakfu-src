package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class NationDiplomacyChangeRequestMessage extends OutputOnlyProxyMessage
{
    private int m_nationId;
    private byte m_alignmentId;
    
    public void setNationId(final int nationId) {
        this.m_nationId = nationId;
    }
    
    public void setAlignmentId(final byte alignmentId) {
        this.m_alignmentId = alignmentId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(5);
        bb.putInt(this.m_nationId);
        bb.put(this.m_alignmentId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20035;
    }
}
