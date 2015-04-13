package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class FighterActorDirectionChangeRequestMessage extends OutputOnlyProxyMessage
{
    private long m_fighterId;
    private Direction8 m_direction8;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putLong(this.m_fighterId);
        buffer.put((byte)this.m_direction8.m_index);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4521;
    }
    
    public void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
    
    public void setDirection8(final Direction8 direction8) {
        this.m_direction8 = direction8;
    }
}
