package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class HavenWorldGuildNotifyBuildingRemovedMessage extends InputOnlyProxyMessage
{
    private int m_buildingId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_buildingId = buffer.getInt();
        return true;
    }
    
    public int getBuildingId() {
        return this.m_buildingId;
    }
    
    @Override
    public int getId() {
        return 20098;
    }
}
