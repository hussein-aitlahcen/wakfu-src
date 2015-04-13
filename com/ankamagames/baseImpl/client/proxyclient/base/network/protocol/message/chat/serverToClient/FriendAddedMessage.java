package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class FriendAddedMessage extends InputOnlyProxyMessage
{
    private String m_friendName;
    private String m_characterName;
    private long m_friendId;
    private short m_friendBreedId;
    private byte m_friendSex;
    private long m_friendXp;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] name = new byte[bb.get() & 0xFF];
        bb.get(name);
        this.m_friendName = StringUtils.fromUTF8(name);
        final byte[] characterName = new byte[bb.get() & 0xFF];
        bb.get(characterName);
        this.m_characterName = StringUtils.fromUTF8(characterName);
        this.m_friendId = bb.getLong();
        this.m_friendBreedId = bb.getShort();
        this.m_friendSex = bb.get();
        this.m_friendXp = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 3156;
    }
    
    public String getFriendName() {
        return this.m_friendName;
    }
    
    public long getFriendId() {
        return this.m_friendId;
    }
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public short getFriendBreedId() {
        return this.m_friendBreedId;
    }
    
    public byte getFriendSex() {
        return this.m_friendSex;
    }
    
    public long getFriendXp() {
        return this.m_friendXp;
    }
}
