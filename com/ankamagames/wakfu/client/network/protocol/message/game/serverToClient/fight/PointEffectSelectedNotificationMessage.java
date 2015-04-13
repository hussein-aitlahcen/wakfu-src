package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public final class PointEffectSelectedNotificationMessage extends AbstractFightMessage
{
    private int m_effectId;
    private long m_chooserId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_effectId = bb.getInt();
        this.m_chooserId = bb.getLong();
        return true;
    }
    
    public int getEffectId() {
        return this.m_effectId;
    }
    
    public long getChooserId() {
        return this.m_chooserId;
    }
    
    @Override
    public int getId() {
        return 8308;
    }
}
