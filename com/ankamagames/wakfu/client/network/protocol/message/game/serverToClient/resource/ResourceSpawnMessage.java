package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.resource;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ResourceSpawnMessage extends InputOnlyProxyMessage
{
    private short m_typeId;
    private int m_x;
    private int m_y;
    private short m_z;
    private byte m_step;
    private boolean m_autoRespawn;
    private boolean m_justGrow;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 15, true)) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_typeId = buffer.getShort();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_z = buffer.getShort();
        this.m_step = buffer.get();
        this.m_justGrow = (buffer.get() == 1);
        this.m_autoRespawn = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 4200;
    }
    
    public short getTypeId() {
        return this.m_typeId;
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
    
    public byte getStep() {
        return this.m_step;
    }
    
    public boolean isAutoRespawn() {
        return this.m_autoRespawn;
    }
    
    public boolean isJustGrow() {
        return this.m_justGrow;
    }
}
