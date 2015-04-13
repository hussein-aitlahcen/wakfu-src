package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class StopChallengeCountdownMessage extends InputOnlyProxyMessage
{
    private int m_challengeId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_challengeId = buffer.getInt();
        return true;
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    @Override
    public int getId() {
        return 11232;
    }
}
