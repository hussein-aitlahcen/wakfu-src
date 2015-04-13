package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.monster.action;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MonsterActionMessage extends InputOnlyProxyMessage
{
    private long m_npcId;
    private long m_playerId;
    private long m_actionId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_npcId = buffer.getLong();
        this.m_playerId = buffer.getLong();
        this.m_actionId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 4530;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public long getNpcId() {
        return this.m_npcId;
    }
    
    public long getActionId() {
        return this.m_actionId;
    }
}
