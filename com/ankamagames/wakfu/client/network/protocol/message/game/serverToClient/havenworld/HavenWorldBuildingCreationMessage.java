package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;

public class HavenWorldBuildingCreationMessage extends InputOnlyProxyMessage
{
    private AbstractBuildingStruct m_info;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_info = BuildingStruct.fromRaw(buffer);
        return true;
    }
    
    public AbstractBuildingStruct getInfo() {
        return this.m_info;
    }
    
    @Override
    public int getId() {
        return 5512;
    }
}
