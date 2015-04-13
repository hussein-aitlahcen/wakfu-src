package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public final class PointEffectSelectionActivationMessage extends AbstractFightMessage
{
    private long m_fighterId;
    private long m_remainingMillis;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_fighterId = bb.getLong();
        this.m_remainingMillis = bb.getLong();
        return true;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public long getRemainingMillis() {
        return this.m_remainingMillis;
    }
    
    @Override
    public int getId() {
        return 8310;
    }
    
    @Override
    public String toString() {
        return "PointEffectSelectionActivationMessage{m_fighterId=" + this.m_fighterId + ", m_remainingMillis=" + this.m_remainingMillis + '}';
    }
}
