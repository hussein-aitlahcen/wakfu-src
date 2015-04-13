package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class HavenWorldBuildingInfoMessage extends InputOnlyProxyMessage
{
    private Building m_building;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_building = HavenWorldSerializer.unSerializeBuildingNoElements(bb);
        return true;
    }
    
    public Building getBuilding() {
        return this.m_building;
    }
    
    @Override
    public int getId() {
        return 5520;
    }
}
