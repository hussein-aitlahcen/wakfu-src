package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ModeratorChatCreationMessage extends InputOnlyProxyMessage
{
    private String m_remoteUser;
    private String m_message;
    private long m_sourceAccountId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_sourceAccountId = bb.getLong();
        final byte[] name = new byte[bb.get() & 0xFF];
        bb.get(name);
        this.m_remoteUser = StringUtils.fromUTF8(name);
        final byte[] message = new byte[bb.get() & 0xFF];
        bb.get(message);
        this.m_message = StringUtils.fromUTF8(message);
        return true;
    }
    
    @Override
    public int getId() {
        return 3178;
    }
    
    public String getRemoteUser() {
        return this.m_remoteUser;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public long getSourceAccountId() {
        return this.m_sourceAccountId;
    }
}
