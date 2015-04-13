package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightCreationRequestMessage extends OutputOnlyProxyMessage
{
    private long m_targetId;
    private int m_x;
    private int m_y;
    private short m_z;
    private boolean m_lockInitially;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(19);
        buffer.putLong(this.m_targetId);
        buffer.putInt(this.m_x);
        buffer.putInt(this.m_y);
        buffer.putShort(this.m_z);
        buffer.put((byte)(this.m_lockInitially ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 8001;
    }
    
    public void setTargetId(final long targetId) {
        this.m_targetId = targetId;
    }
    
    public void setTargetPosition(final int x, final int y, final short z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public void lockInitially() {
        this.m_lockInitially = true;
    }
}
