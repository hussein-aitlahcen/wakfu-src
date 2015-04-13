package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightInvitPlayerRequestMessage extends OutputOnlyProxyMessage
{
    private long m_playerId;
    private int m_x;
    private int m_y;
    private short m_z;
    private boolean m_lockedInitialy;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buf = ByteBuffer.allocate(19);
        buf.putLong(this.m_playerId);
        buf.putInt(this.m_x);
        buf.putInt(this.m_y);
        buf.putShort(this.m_z);
        buf.put((byte)(this.m_lockedInitialy ? 1 : 0));
        return this.addClientHeader((byte)3, buf.array());
    }
    
    public void setPlayerId(final long playerId) {
        this.m_playerId = playerId;
    }
    
    public void setFightPositionRequested(final int x, final int y, final short z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public void setLockedInitialy(final boolean lockedInitialy) {
        this.m_lockedInitialy = lockedInitialy;
    }
    
    @Override
    public int getId() {
        return 7901;
    }
}
