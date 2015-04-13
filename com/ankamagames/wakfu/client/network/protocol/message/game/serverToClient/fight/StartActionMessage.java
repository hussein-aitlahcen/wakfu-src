package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class StartActionMessage extends AbstractFightMessage
{
    private int m_timePointGap;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        this.m_timePointGap = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 8030;
    }
    
    public int getTimePointGap() {
        return this.m_timePointGap;
    }
}
