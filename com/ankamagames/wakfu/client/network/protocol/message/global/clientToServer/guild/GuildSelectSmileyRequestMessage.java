package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildSelectSmileyRequestMessage extends OutputOnlyProxyMessage
{
    private byte m_smileyId;
    
    public GuildSelectSmileyRequestMessage(final byte smileyId) {
        super();
        this.m_smileyId = smileyId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(this.m_smileyId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20083;
    }
}
