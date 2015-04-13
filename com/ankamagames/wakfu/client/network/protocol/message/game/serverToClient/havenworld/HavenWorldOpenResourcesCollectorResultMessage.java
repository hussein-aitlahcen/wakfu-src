package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class HavenWorldOpenResourcesCollectorResultMessage extends InputOnlyProxyMessage
{
    private int m_resources;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_resources = buffer.getInt();
        return true;
    }
    
    public int getResources() {
        return this.m_resources;
    }
    
    @Override
    public int getId() {
        return 20096;
    }
}
