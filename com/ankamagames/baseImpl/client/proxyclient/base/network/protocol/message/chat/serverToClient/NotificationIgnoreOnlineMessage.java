package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class NotificationIgnoreOnlineMessage extends InputOnlyProxyMessage
{
    private String m_IgnoreName;
    private String m_IgnoreCharacterName;
    private long m_userId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte[] name = new byte[buffer.get() & 0xFF];
        buffer.get(name);
        this.m_IgnoreName = StringUtils.fromUTF8(name);
        final byte[] characterName = new byte[buffer.get() & 0xFF];
        buffer.get(characterName);
        this.m_IgnoreCharacterName = StringUtils.fromUTF8(characterName);
        this.m_userId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 3164;
    }
    
    public String getIgnoreName() {
        return this.m_IgnoreName;
    }
    
    public String getIgnoreCharacterName() {
        return this.m_IgnoreCharacterName;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
}
