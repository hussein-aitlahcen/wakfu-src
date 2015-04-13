package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightJoinFailureMessage extends InputOnlyProxyMessage
{
    private int m_reason;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_reason = buffer.getInt();
        return true;
    }
    
    public int getReason() {
        return this.m_reason;
    }
    
    @Override
    public int getId() {
        return 8004;
    }
}
