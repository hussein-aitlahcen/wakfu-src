package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.collector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CollectorContentMessage extends InputOnlyProxyMessage
{
    private long m_machineId;
    private byte[] m_content;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_machineId = bb.getLong();
        bb.get(this.m_content = new byte[bb.remaining()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 15730;
    }
    
    public long getMachineId() {
        return this.m_machineId;
    }
    
    public byte[] getContent() {
        return this.m_content;
    }
}
