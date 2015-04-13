package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RedModerationMessage extends ChatContentMessage
{
    private String m_memberTalking;
    private long m_memberIDTalking;
    private String m_messageContent;
    private boolean m_isPrivate;
    private boolean m_isTradkey;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] mt = new byte[bb.get() & 0xFF];
        bb.get(mt);
        this.m_memberTalking = StringUtils.fromUTF8(mt);
        this.m_memberIDTalking = bb.getLong();
        this.m_isTradkey = (bb.get() > 0);
        final byte[] mc = new byte[bb.get() & 0xFF];
        bb.get(mc);
        this.m_messageContent = StringUtils.fromUTF8(mc);
        this.m_isPrivate = (bb.get() > 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 3300;
    }
    
    public boolean isPrivate() {
        return this.m_isPrivate;
    }
    
    public boolean isTradkey() {
        return this.m_isTradkey;
    }
    
    @Override
    public String getMessageContent() {
        return this.m_messageContent;
    }
    
    @Override
    public long getMemberIDTalking() {
        return this.m_memberIDTalking;
    }
    
    @Override
    public String getMemberTalking() {
        return this.m_memberTalking;
    }
}
