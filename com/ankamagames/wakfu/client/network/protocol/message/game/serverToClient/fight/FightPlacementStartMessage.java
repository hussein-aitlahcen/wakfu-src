package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class FightPlacementStartMessage extends AbstractFightMessage
{
    private int m_remainingTime;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        this.m_remainingTime = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 8026;
    }
    
    public int getRemainingTime() {
        return this.m_remainingTime;
    }
}
