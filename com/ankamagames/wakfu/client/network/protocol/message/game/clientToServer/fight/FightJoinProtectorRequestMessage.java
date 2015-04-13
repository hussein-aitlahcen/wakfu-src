package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class FightJoinProtectorRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_protectorId;
    private final boolean m_locked;
    
    public FightJoinProtectorRequestMessage(final int protectorId, final boolean locked) {
        super();
        this.m_protectorId = protectorId;
        this.m_locked = locked;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(this.m_protectorId);
        buffer.put((byte)(this.m_locked ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 8009;
    }
}
