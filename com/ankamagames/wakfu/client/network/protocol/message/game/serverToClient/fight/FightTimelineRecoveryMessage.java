package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class FightTimelineRecoveryMessage extends AbstractFightMessage
{
    private byte[] m_serializedTimeline;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        buffer.get(this.m_serializedTimeline = new byte[buffer.getShort()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 8010;
    }
    
    public byte[] getSerializedTimeline() {
        return this.m_serializedTimeline;
    }
}
