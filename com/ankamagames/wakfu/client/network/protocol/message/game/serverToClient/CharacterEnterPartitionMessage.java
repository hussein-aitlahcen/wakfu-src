package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CharacterEnterPartitionMessage extends InputOnlyProxyMessage
{
    private int m_worldX;
    private int m_worldY;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_worldX = buffer.getInt();
        this.m_worldY = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4125;
    }
    
    public int getWorldX() {
        return this.m_worldX;
    }
    
    public int getWorldY() {
        return this.m_worldY;
    }
}
