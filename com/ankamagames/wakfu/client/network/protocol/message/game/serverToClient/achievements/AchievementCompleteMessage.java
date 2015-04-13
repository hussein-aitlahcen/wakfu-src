package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementCompleteMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_achievementId;
    private long m_unlockTime;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_achievementId = buffer.getInt();
        this.m_unlockTime = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15602;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getAchievementId() {
        return this.m_achievementId;
    }
    
    public long getUnlockTime() {
        return this.m_unlockTime;
    }
}
