package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class NotificationFriendOfflineMessage extends InputOnlyProxyMessage
{
    private String m_friendName;
    private String m_characterName;
    private long m_userId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte[] fn = new byte[buffer.get() & 0xFF];
        buffer.get(fn);
        this.m_friendName = StringUtils.fromUTF8(fn);
        final byte[] cn = new byte[buffer.get() & 0xFF];
        buffer.get(cn);
        this.m_characterName = StringUtils.fromUTF8(cn);
        this.m_userId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 3150;
    }
    
    public String getFriendName() {
        return this.m_friendName;
    }
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
}
