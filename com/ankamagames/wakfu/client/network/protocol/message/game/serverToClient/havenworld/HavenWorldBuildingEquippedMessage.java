package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class HavenWorldBuildingEquippedMessage extends InputOnlyProxyMessage
{
    private long m_buildingUid;
    private int m_itemId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_buildingUid = buffer.getLong();
        this.m_itemId = buffer.getInt();
        return true;
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    @Override
    public int getId() {
        return 5524;
    }
}
