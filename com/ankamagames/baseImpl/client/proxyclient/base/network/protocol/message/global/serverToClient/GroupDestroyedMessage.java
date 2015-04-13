package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GroupDestroyedMessage extends InputOnlyProxyMessage
{
    private long m_groupId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_groupId = buff.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 516;
    }
    
    public long getGroupId() {
        return this.m_groupId;
    }
}
