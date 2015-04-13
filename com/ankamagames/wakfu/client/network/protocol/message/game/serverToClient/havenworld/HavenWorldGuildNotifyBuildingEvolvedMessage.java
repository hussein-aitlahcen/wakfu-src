package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class HavenWorldGuildNotifyBuildingEvolvedMessage extends InputOnlyProxyMessage
{
    private int m_buildingId;
    private int m_buildingIdTo;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_buildingId = buffer.getInt();
        this.m_buildingIdTo = buffer.getInt();
        return true;
    }
    
    public int getBuildingId() {
        return this.m_buildingId;
    }
    
    public int getBuildingIdTo() {
        return this.m_buildingIdTo;
    }
    
    @Override
    public int getId() {
        return 20073;
    }
}
