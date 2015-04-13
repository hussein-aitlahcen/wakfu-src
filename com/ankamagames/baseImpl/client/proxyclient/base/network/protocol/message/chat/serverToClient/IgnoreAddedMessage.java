package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class IgnoreAddedMessage extends InputOnlyProxyMessage
{
    private long m_ignoreId;
    private String m_ignoreName;
    private String m_characterName;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_ignoreId = bb.getLong();
        final byte[] ignoreName = new byte[bb.get() & 0xFF];
        bb.get(ignoreName);
        this.m_ignoreName = StringUtils.fromUTF8(ignoreName);
        final byte[] characterName = new byte[bb.get() & 0xFF];
        bb.get(characterName);
        this.m_characterName = StringUtils.fromUTF8(characterName);
        return true;
    }
    
    @Override
    public int getId() {
        return 3158;
    }
    
    public String getIgnoreName() {
        return this.m_ignoreName;
    }
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public long getIgnoreId() {
        return this.m_ignoreId;
    }
}
