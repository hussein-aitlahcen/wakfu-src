package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class PrivateContentMessage extends ChatContentMessage
{
    private long m_memberIdTalked;
    private String m_memberTalked;
    private long m_memberIdTalking;
    private String m_memberTalking;
    private String m_content;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_memberIdTalked = bb.getLong();
        final byte[] memberTalked = new byte[bb.get()];
        bb.get(memberTalked);
        this.m_memberTalked = StringUtils.fromUTF8(memberTalked);
        this.m_memberIdTalking = bb.getLong();
        final byte[] memberTalking = new byte[bb.get()];
        bb.get(memberTalking);
        this.m_memberTalking = StringUtils.fromUTF8(memberTalking);
        final byte[] content = new byte[bb.getInt()];
        bb.get(content);
        this.m_content = StringUtils.fromUTF8(content);
        return true;
    }
    
    @Override
    public long getMemberIDTalking() {
        return this.m_memberIdTalking;
    }
    
    @Override
    public String getMemberTalking() {
        return this.m_memberTalking;
    }
    
    public long getMemberIdTalked() {
        return this.m_memberIdTalked;
    }
    
    public String getMemberTalked() {
        return this.m_memberTalked;
    }
    
    @Override
    public String getMessageContent() {
        return this.m_content;
    }
    
    @Override
    public int getId() {
        return 3154;
    }
}
