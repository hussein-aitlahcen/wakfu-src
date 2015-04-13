package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightInvitAnswerRequestMessage extends OutputOnlyProxyMessage
{
    private long m_initiatingPlayerId;
    private boolean m_answer;
    private boolean m_locked;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buf = ByteBuffer.allocate(10);
        buf.putLong(this.m_initiatingPlayerId);
        buf.put((byte)(this.m_answer ? 1 : 0));
        buf.put((byte)(this.m_locked ? 1 : 0));
        return this.addClientHeader((byte)3, buf.array());
    }
    
    public void setPlayerId(final long playerId) {
        this.m_initiatingPlayerId = playerId;
    }
    
    public void setAnswer(final boolean answer) {
        this.m_answer = answer;
    }
    
    public void setLocked(final boolean locked) {
        this.m_locked = locked;
    }
    
    @Override
    public int getId() {
        return 7903;
    }
}
