package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightJoinRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_targetId;
    private boolean m_lock;
    
    public FightJoinRequestMessage(final long targetId, final boolean lock) {
        super();
        this.m_targetId = targetId;
        this.m_lock = lock;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putLong(this.m_targetId);
        buffer.put((byte)(this.m_lock ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 8003;
    }
}
