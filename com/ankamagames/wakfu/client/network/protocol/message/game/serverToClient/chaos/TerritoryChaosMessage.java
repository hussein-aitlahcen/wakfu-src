package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.chaos;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class TerritoryChaosMessage extends InputOnlyProxyMessage
{
    private int m_territoryId;
    private boolean m_isInChaos;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_territoryId = buff.getInt();
        this.m_isInChaos = (buff.get() == 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 9301;
    }
    
    public int getTerritoryId() {
        return this.m_territoryId;
    }
    
    public boolean isInChaos() {
        return this.m_isInChaos;
    }
}
