package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class FightChallengeGoalView extends AbstractQuestGoalView
{
    private final int challengeId;
    
    public FightChallengeGoalView(final int challengeId) {
        super();
        this.challengeId = challengeId;
    }
    
    @Override
    protected String getDescription() {
        return WakfuTranslator.getInstance().getString(141, this.challengeId, new Object[0]);
    }
    
    @Override
    protected long getProgressionValue() {
        return -1L;
    }
    
    @Override
    protected boolean isCompleted() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext().getChallengeState(this.challengeId) == FightChallengeState.SUCCESS;
    }
    
    @Override
    protected boolean isFailed() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext().getChallengeState(this.challengeId) == FightChallengeState.FAILURE;
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
    
    public void onStateChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isCompleted", "isFailed");
    }
}
