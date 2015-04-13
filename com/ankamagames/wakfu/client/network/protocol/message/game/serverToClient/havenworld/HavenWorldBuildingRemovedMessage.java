package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class HavenWorldBuildingRemovedMessage extends InputOnlyProxyMessage
{
    private long m_buildingUid;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_buildingUid = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 5514;
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
}
