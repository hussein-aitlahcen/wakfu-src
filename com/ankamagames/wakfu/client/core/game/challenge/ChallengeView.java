package com.ankamagames.wakfu.client.core.game.challenge;

public class ChallengeView extends AbstractChallengeView
{
    private final int m_challengeId;
    
    public ChallengeView(final int challengeId) {
        super();
        this.m_challengeId = challengeId;
    }
    
    @Override
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    @Override
    protected ChallengeData getChallengeData() {
        return ChallengeManager.getInstance().getChallengeData(this.m_challengeId);
    }
}
