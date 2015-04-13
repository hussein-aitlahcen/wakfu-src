package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SetChallengeCountdownMessage extends InputOnlyProxyMessage
{
    private int m_challengeId;
    private int m_duration;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_duration = buffer.getInt();
        this.m_challengeId = buffer.getInt();
        return true;
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    @Override
    public int getId() {
        return 11230;
    }
}
