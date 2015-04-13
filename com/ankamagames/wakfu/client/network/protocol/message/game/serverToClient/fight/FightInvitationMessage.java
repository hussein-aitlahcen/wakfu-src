package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightInvitationMessage extends InputOnlyProxyMessage
{
    private long m_fighterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_fighterId = buff.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 7902;
    }
    
    public final long getFighterId() {
        return this.m_fighterId;
    }
}
