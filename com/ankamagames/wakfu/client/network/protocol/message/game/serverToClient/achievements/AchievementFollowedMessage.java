package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementFollowedMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_achievementId;
    private boolean m_followed;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_achievementId = buffer.getInt();
        this.m_followed = (buffer.get() != 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 15608;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getAchievementId() {
        return this.m_achievementId;
    }
    
    public boolean isFollowed() {
        return this.m_followed;
    }
}
