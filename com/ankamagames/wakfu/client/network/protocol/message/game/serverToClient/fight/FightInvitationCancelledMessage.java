package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightInvitationCancelledMessage extends InputOnlyProxyMessage
{
    private byte m_reason;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_reason = buff.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 7904;
    }
    
    public byte getReason() {
        return this.m_reason;
    }
}
