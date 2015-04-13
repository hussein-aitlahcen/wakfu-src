package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.xp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class OtherPlayerLevelUpMessage extends InputOnlyProxyMessage
{
    private long m_playerId;
    private long m_playerXp;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_playerId = bb.getLong();
        this.m_playerXp = bb.getLong();
        return false;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public long getPlayerXp() {
        return this.m_playerXp;
    }
    
    @Override
    public int getId() {
        return 4220;
    }
}
