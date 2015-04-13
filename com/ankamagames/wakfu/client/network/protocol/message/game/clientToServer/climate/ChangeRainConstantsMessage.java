package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.climate;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChangeRainConstantsMessage extends OutputOnlyProxyMessage
{
    private int m_locationX;
    private int m_locationY;
    private float m_precipitations;
    private short m_instanceId;
    
    public void setLocation(final int x, final int y) {
        this.m_locationX = x;
        this.m_locationY = y;
    }
    
    public void setPrecipitations(final float precipitations) {
        this.m_precipitations = precipitations;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(14);
        buffer.putShort(this.m_instanceId);
        buffer.putInt(this.m_locationX);
        buffer.putInt(this.m_locationY);
        buffer.putFloat(this.m_precipitations);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 9401;
    }
    
    public void setInstanceId(final short instanceId) {
        this.m_instanceId = instanceId;
    }
}
