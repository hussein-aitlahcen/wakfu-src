package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public final class PlayerChangeConnectionStateMessage extends AbstractFightMessage
{
    private long m_playerId;
    private byte m_connectionState;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_playerId = bb.getLong();
        this.m_connectionState = bb.get();
        return false;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public byte getConnectionState() {
        return this.m_connectionState;
    }
    
    @Override
    public int getId() {
        return 8415;
    }
    
    @Override
    public String toString() {
        return "PlayerChangeConnectionStateMessage{m_playerId=" + this.m_playerId + ", m_connectionState=" + this.m_connectionState + '}';
    }
}
