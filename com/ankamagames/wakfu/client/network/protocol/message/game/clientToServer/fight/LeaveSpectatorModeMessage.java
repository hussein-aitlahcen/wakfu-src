package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class LeaveSpectatorModeMessage extends OutputOnlyProxyMessage
{
    private final int m_fightId;
    
    public LeaveSpectatorModeMessage(final int fightId) {
        super();
        this.m_fightId = fightId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_fightId);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 8039;
    }
}
