package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class UnEquipEventMessage extends InputOnlyProxyMessage
{
    private long m_uid;
    private byte m_pos;
    private long m_containerId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_uid = buff.getLong();
        this.m_pos = buff.get();
        this.m_containerId = buff.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 11108;
    }
    
    public long getUid() {
        return this.m_uid;
    }
    
    public byte getPos() {
        return this.m_pos;
    }
    
    public long getContainerId() {
        return this.m_containerId;
    }
}
