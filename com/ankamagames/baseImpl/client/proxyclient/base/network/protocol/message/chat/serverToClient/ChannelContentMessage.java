package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ChannelContentMessage extends InputOnlyProxyMessage
{
    private String m_channelName;
    private String m_memberTalking;
    private String m_messageContent;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] cn = new byte[bb.get()];
        bb.get(cn);
        this.m_channelName = StringUtils.fromUTF8(cn);
        final byte[] mt = new byte[bb.get()];
        bb.get(mt);
        this.m_memberTalking = StringUtils.fromUTF8(mt);
        final byte[] mc = new byte[bb.get()];
        bb.get(mc);
        this.m_messageContent = StringUtils.fromUTF8(mc);
        return true;
    }
    
    @Override
    public int getId() {
        return 3140;
    }
    
    public String getChannelName() {
        return this.m_channelName;
    }
    
    public String getMemberTalking() {
        return this.m_memberTalking;
    }
    
    public String getMessageContent() {
        return this.m_messageContent;
    }
}
