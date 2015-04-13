package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class HavenWorldBuildingFactorUpdate extends InputOnlyProxyMessage
{
    private short m_worldId;
    private int m_factor;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_worldId = bb.getShort();
        this.m_factor = bb.getInt();
        return true;
    }
    
    public short getWorldId() {
        return this.m_worldId;
    }
    
    public int getFactor() {
        return this.m_factor;
    }
    
    @Override
    public int getId() {
        return 15655;
    }
}
