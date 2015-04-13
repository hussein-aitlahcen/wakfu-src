package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PlayerWakfuGaugeModificationMessage extends InputOnlyProxyMessage
{
    private long m_playerId;
    private int m_newWakfuGaugeValue;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_playerId = bb.getLong();
        this.m_newWakfuGaugeValue = bb.getInt();
        return true;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public int getNewWakfuGaugeValue() {
        return this.m_newWakfuGaugeValue;
    }
    
    @Override
    public int getId() {
        return 12602;
    }
}
