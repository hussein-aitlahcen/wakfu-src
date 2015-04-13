package com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public final class WhoisRequestMessage extends OutputOnlyProxyMessage
{
    private String m_characterName;
    
    @Override
    public byte[] encode() {
        final byte[] ndata = StringUtils.toUTF8(this.m_characterName);
        final int size = 1 + ndata.length;
        final ByteBuffer bb = ByteBuffer.allocate(size);
        bb.put((byte)ndata.length);
        bb.put(ndata);
        return this.addClientHeader((byte)2, bb.array());
    }
    
    @Override
    public int getId() {
        return 2060;
    }
    
    public void setCharacterName(final String characterName) {
        this.m_characterName = characterName;
    }
    
    @Override
    public String toString() {
        return "WhoisRequestMessage{m_characterName='" + this.m_characterName + '\'' + '}';
    }
}
