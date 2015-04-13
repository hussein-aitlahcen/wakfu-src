package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementActivationRequestMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_achievementId;
    private long m_inviterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_achievementId = buffer.getInt();
        this.m_inviterId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15612;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public long getInviterId() {
        return this.m_inviterId;
    }
    
    public int getAchievementId() {
        return this.m_achievementId;
    }
}
