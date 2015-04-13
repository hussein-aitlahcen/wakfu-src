package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class GroupPrivateContentDispatchMessage extends InputOnlyProxyMessage
{
    private long m_groupId;
    private String m_talkingCharacter;
    private String m_message;
    private long m_talkingId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_groupId = buff.getLong();
        final byte[] name = new byte[buff.getShort()];
        buff.get(name);
        this.m_talkingCharacter = StringUtils.fromUTF8(name);
        this.m_talkingId = buff.getLong();
        final byte[] message = new byte[buff.getShort()];
        buff.get(message);
        this.m_message = StringUtils.fromUTF8(message);
        return true;
    }
    
    @Override
    public int getId() {
        return 508;
    }
    
    public long getGroupId() {
        return this.m_groupId;
    }
    
    public String getTalkingCharacter() {
        return this.m_talkingCharacter;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public long getTalkingId() {
        return this.m_talkingId;
    }
}
