package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class ClientSystemConfigurationMessage extends InputOnlyProxyMessage
{
    private byte[] m_data;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        bb.get(this.m_data = new byte[bb.getInt()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 2067;
    }
    
    public byte[] getData() {
        return this.m_data;
    }
}
