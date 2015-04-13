package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class FighterReadyMessage extends AbstractFightMessage
{
    private long m_fighterId;
    private boolean m_ready;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buff);
        this.m_fighterId = buff.getLong();
        this.m_ready = (buff.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 8150;
    }
    
    public boolean isReady() {
        return this.m_ready;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
}
