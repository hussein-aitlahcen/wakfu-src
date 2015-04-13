package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GroupResultMessage extends InputOnlyProxyMessage
{
    private byte m_groupType;
    private int m_result;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_groupType = bb.get();
        this.m_result = bb.getInt();
        return true;
    }
    
    public byte getGroupType() {
        return this.m_groupType;
    }
    
    public int getResult() {
        return this.m_result;
    }
    
    @Override
    public int getId() {
        return 504;
    }
}
