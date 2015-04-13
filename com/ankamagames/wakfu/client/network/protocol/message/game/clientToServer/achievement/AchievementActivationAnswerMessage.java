package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementActivationAnswerMessage extends OutputOnlyProxyMessage
{
    private final int m_achievementId;
    private final long m_inviterId;
    private final boolean m_activate;
    
    public AchievementActivationAnswerMessage(final int achievementId, final long inviterId, final boolean activate) {
        super();
        this.m_achievementId = achievementId;
        this.m_inviterId = inviterId;
        this.m_activate = activate;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(13);
        buffer.putInt(this.m_achievementId);
        buffer.putLong(this.m_inviterId);
        buffer.put((byte)(this.m_activate ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15613;
    }
}
