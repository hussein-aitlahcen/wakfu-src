package com.ankamagames.wakfu.client.core.game.challenge;

public class StaticChallengeView extends AbstractChallengeView
{
    private final ChallengeData m_challengeData;
    
    public StaticChallengeView(final ChallengeData data) {
        super();
        this.m_challengeData = data;
    }
    
    @Override
    public int getChallengeId() {
        return this.m_challengeData.getId();
    }
    
    @Override
    protected ChallengeData getChallengeData() {
        return this.m_challengeData;
    }
}
