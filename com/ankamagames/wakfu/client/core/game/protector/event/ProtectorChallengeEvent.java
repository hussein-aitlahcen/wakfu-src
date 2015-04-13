package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public class ProtectorChallengeEvent extends ProtectorEvent
{
    private int m_challengeId;
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    public void setChallengeId(final int challengeId) {
        this.m_challengeId = challengeId;
    }
    
    @Override
    public ProtectorMood getProtectorMood() {
        return ProtectorMood.NEUTRAL;
    }
}
