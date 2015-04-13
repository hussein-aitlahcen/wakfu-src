package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.resource;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ResourceModificationMessage extends InputOnlyProxyMessage
{
    private int m_x;
    private int m_y;
    private short m_z;
    private short m_stateId;
    private int m_apsId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_z = buffer.getShort();
        this.m_stateId = buffer.getShort();
        this.m_apsId = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4201;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public short getZ() {
        return this.m_z;
    }
    
    public short getStateId() {
        return this.m_stateId;
    }
    
    public int getApsId() {
        return this.m_apsId;
    }
}
