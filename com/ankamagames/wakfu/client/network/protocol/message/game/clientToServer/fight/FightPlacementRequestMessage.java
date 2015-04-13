package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class FightPlacementRequestMessage extends OutputOnlyProxyMessage
{
    private Point3 m_position;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(this.m_position.getX());
        buffer.putInt(this.m_position.getY());
        buffer.putShort(this.m_position.getZ());
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 8147;
    }
    
    public void setPosition(final Point3 position) {
        this.m_position = position;
    }
    
    @Override
    public String toString() {
        return "FightPlacementRequestMessage{m_position=" + this.m_position + '}';
    }
}
