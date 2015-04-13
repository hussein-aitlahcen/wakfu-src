package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fightChallenge;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import java.nio.*;

public class FightChallengeEndedMessage extends InputOnlyProxyMessage
{
    private int m_challengeId;
    private FightChallengeState m_state;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_challengeId = buffer.getInt();
        this.m_state = FightChallengeState.getFromId(buffer.get());
        return true;
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    public FightChallengeState getState() {
        return this.m_state;
    }
    
    @Override
    public int getId() {
        return 15512;
    }
}
