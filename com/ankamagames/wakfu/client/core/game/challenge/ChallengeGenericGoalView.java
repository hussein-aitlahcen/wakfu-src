package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.wakfu.client.core.game.achievements.ui.*;

public class ChallengeGenericGoalView extends AbstractQuestGoalView
{
    private final int m_challengeId;
    
    public ChallengeGenericGoalView(final int challengeId) {
        super();
        this.m_challengeId = challengeId;
    }
    
    @Override
    protected String getDescription() {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.m_challengeId);
        return getGoalDescription(model);
    }
    
    @Override
    protected long getProgressionValue() {
        return 0L;
    }
    
    @Override
    protected boolean isCompleted() {
        return false;
    }
    
    @Override
    protected boolean isFailed() {
        return false;
    }
    
    protected static String getGoalDescription(final ChallengeDataModel model) {
        final String desc = model.getChallengeDescription();
        if (desc != null && desc.length() == 0) {
            return null;
        }
        return desc;
    }
    
    @Override
    protected boolean isCompassed() {
        return false;
    }
    
    @Override
    protected boolean canBeCompassed() {
        return false;
    }
    
    @Override
    protected boolean canBeCompassedNow() {
        return false;
    }
}
