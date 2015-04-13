package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class AchievementGoalView extends AbstractQuestGoalView
{
    private final Objective m_goal;
    private final ClientAchievementsContext m_achievementsContext;
    
    public AchievementGoalView(final long characterId, final Objective goal) {
        super();
        this.m_achievementsContext = AchievementContextManager.INSTANCE.getContext(characterId);
        this.m_goal = goal;
    }
    
    @Override
    protected long getProgressionValue() {
        try {
            final Variable[] vars = this.m_goal.getRelatedVariables();
            if (vars == null) {
                return -1L;
            }
            final int varId = vars[0].getId();
            return this.m_achievementsContext.hasVariable(varId) ? this.m_achievementsContext.getVariableValue(varId) : null;
        }
        catch (Exception e) {
            return -1L;
        }
    }
    
    public int getId() {
        return this.m_goal.getId();
    }
    
    @Override
    protected String getDescription() {
        return WakfuTranslator.getInstance().getString(64, this.m_goal.getId(), new Object[0]);
    }
    
    @Override
    protected boolean isCompleted() {
        final int goalId = this.m_goal.getId();
        return !this.m_achievementsContext.hasObjective(goalId) || this.m_achievementsContext.isObjectiveCompleted(goalId);
    }
    
    @Override
    protected boolean isFailed() {
        return false;
    }
    
    @Override
    protected boolean isCompassed() {
        return AchievementsViewManager.INSTANCE.hasCompassedObjectiveId(this.m_goal.getId());
    }
    
    @Override
    protected boolean canBeCompassed() {
        return this.m_goal.isPositionFeedback();
    }
    
    @Override
    protected boolean canBeCompassedNow() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId() == this.m_goal.getWorldId();
    }
    
    public void updateCompletion() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isCompleted");
    }
    
    public void updateProgression() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "progressionText");
    }
}
