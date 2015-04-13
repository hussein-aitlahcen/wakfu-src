package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MonsterGroupUpdateMessage extends InputOnlyProxyMessage
{
    private long m_character;
    private long m_newGroupId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_newGroupId = buffer.getLong();
        this.m_character = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 4134;
    }
    
    public long getCharacter() {
        return this.m_character;
    }
    
    public long getNewGroupId() {
        return this.m_newGroupId;
    }
}
