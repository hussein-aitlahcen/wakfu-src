package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementFollowRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_achievementId;
    private final boolean m_follow;
    
    public AchievementFollowRequestMessage(final int achievementId, final boolean follow) {
        super();
        this.m_achievementId = achievementId;
        this.m_follow = follow;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(this.m_achievementId);
        buffer.put((byte)(this.m_follow ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15607;
    }
}
