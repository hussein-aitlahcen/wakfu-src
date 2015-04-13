package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.playerTitle;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PlayerTitleChangedMessage extends InputOnlyProxyMessage
{
    private long m_playerId;
    private short m_currentTitle;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_playerId = buffer.getLong();
        this.m_currentTitle = buffer.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 5504;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public short getCurrentTitle() {
        return this.m_currentTitle;
    }
}
