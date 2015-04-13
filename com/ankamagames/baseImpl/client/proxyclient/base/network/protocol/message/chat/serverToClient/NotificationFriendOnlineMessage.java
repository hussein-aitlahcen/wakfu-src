package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class NotificationFriendOnlineMessage extends InputOnlyProxyMessage
{
    private String m_friendName;
    private String m_characterName;
    private long m_userId;
    private String m_commentary;
    private short m_breedId;
    private long m_xp;
    private byte m_sex;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte[] name = new byte[buffer.get() & 0xFF];
        buffer.get(name);
        this.m_friendName = StringUtils.fromUTF8(name);
        final byte[] characterName = new byte[buffer.get() & 0xFF];
        buffer.get(characterName);
        this.m_characterName = StringUtils.fromUTF8(characterName);
        final byte[] commentary = new byte[buffer.get() & 0xFF];
        buffer.get(commentary);
        this.m_commentary = StringUtils.fromUTF8(commentary);
        this.m_userId = buffer.getLong();
        this.m_breedId = buffer.getShort();
        this.m_sex = buffer.get();
        this.m_xp = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 3148;
    }
    
    public String getFriendName() {
        return this.m_friendName;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public byte getSex() {
        return this.m_sex;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public String getCommentary() {
        return this.m_commentary;
    }
    
    public long getXp() {
        return this.m_xp;
    }
}
