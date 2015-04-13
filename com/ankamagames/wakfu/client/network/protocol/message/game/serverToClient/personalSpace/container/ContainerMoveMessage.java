package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.container;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ContainerMoveMessage extends InputOnlyProxyMessage
{
    private long m_containerGuid;
    private int m_worldX;
    private int m_worldY;
    
    public long getContainerGuid() {
        return this.m_containerGuid;
    }
    
    public int getWorldX() {
        return this.m_worldX;
    }
    
    public int getWorldY() {
        return this.m_worldY;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_containerGuid = buffer.getLong();
        this.m_worldX = buffer.getInt();
        this.m_worldY = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 10064;
    }
}
