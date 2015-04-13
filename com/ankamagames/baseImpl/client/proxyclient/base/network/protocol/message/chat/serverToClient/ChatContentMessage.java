package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public abstract class ChatContentMessage extends InputOnlyProxyMessage
{
    private String m_memberTalking;
    private long m_memberIDTalking;
    private String m_messageContent;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] mt = new byte[bb.get() & 0xFF];
        bb.get(mt);
        this.m_memberTalking = StringUtils.fromUTF8(mt);
        this.m_memberIDTalking = bb.getLong();
        final byte[] mc = new byte[bb.get() & 0xFF];
        bb.get(mc);
        this.m_messageContent = StringUtils.fromUTF8(mc);
        return true;
    }
    
    public long getMemberIDTalking() {
        return this.m_memberIDTalking;
    }
    
    public String getMemberTalking() {
        return this.m_memberTalking;
    }
    
    public String getMessageContent() {
        return this.m_messageContent;
    }
}
