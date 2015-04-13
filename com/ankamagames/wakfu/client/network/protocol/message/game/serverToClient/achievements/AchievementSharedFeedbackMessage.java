package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementSharedFeedbackMessage extends InputOnlyProxyMessage
{
    private int m_achievementId;
    private long m_playerId;
    private boolean m_accept;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_achievementId = buffer.getInt();
        this.m_playerId = buffer.getLong();
        this.m_accept = (buffer.get() != 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 15618;
    }
    
    public int getAchievementId() {
        return this.m_achievementId;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public boolean isAccept() {
        return this.m_accept;
    }
}
