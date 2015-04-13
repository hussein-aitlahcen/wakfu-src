package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementShareRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_achievementId;
    
    public AchievementShareRequestMessage(final int achievementId) {
        super();
        this.m_achievementId = achievementId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(this.m_achievementId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15615;
    }
}
