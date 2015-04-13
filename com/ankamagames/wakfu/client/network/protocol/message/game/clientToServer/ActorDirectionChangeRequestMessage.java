package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class ActorDirectionChangeRequestMessage extends OutputOnlyProxyMessage
{
    private final Direction8 m_direction8;
    
    public ActorDirectionChangeRequestMessage(final Direction8 direction8) {
        super();
        this.m_direction8 = direction8;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte)this.m_direction8.m_index);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4117;
    }
}
