package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class NationGovernorTitleModificationRequestMessage extends OutputOnlyProxyMessage
{
    private short m_newTitle;
    
    public void setNewTitle(final short newTitle) {
        this.m_newTitle = newTitle;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort(this.m_newTitle);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20017;
    }
}
