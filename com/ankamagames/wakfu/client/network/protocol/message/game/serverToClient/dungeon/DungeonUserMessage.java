package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dungeon;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DungeonUserMessage extends InputOnlyProxyMessage
{
    private int m_dungeonDefinitionId;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_dungeonDefinitionId = bb.getInt();
        return true;
    }
    
    public int getDungeonDefinitionId() {
        return this.m_dungeonDefinitionId;
    }
    
    @Override
    public int getId() {
        return 15956;
    }
}
