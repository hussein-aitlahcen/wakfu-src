package com.ankamagames.wakfu.client.core.game.achievements.ui;

import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public abstract class AbstractFightChallengeView extends AbstractQuestView
{
    public static final String ADDITIONAL_INFOS = "additionalInfos";
    public static final String REWARDS = "rewards";
    protected final int m_challengeId;
    protected final ArrayList<AbstractQuestGoalView> m_goal;
    
    public AbstractFightChallengeView(final int challengeId) {
        super();
        this.m_goal = new ArrayList<AbstractQuestGoalView>();
        this.m_challengeId = challengeId;
        this.m_goal.add(new FightChallengeGoalView(challengeId));
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isCompleted")) {
            return this.isCompleted();
        }
        if (fieldName.equals("isFailed")) {
            return this.isFailed();
        }
        if (fieldName.equals("rewards")) {
            return this.getRewards();
        }
        if (!fieldName.equals("additionalInfos")) {
            return super.getFieldValue(fieldName);
        }
        if (!WakfuTranslator.getInstance().containsContentKey(142, this.m_challengeId)) {
            return null;
        }
        final String info = WakfuTranslator.getInstance().getString(142, this.m_challengeId, new Object[0]);
        if (info.isEmpty()) {
            return null;
        }
        return info;
    }
    
    protected abstract boolean isFailed();
    
    protected abstract boolean isCompleted();
    
    public abstract String getRewards();
    
    @Override
    protected String getBackgroundText() {
        return null;
    }
    
    @Override
    protected String getName() {
        return WakfuTranslator.getInstance().getString(140, this.m_challengeId, new Object[0]);
    }
    
    @Override
    protected String getIconUrl() {
        final FightChallenge challenge = FightChallengeManager.INSTANCE.getChallenge(this.m_challengeId);
        final int gfxId = (challenge != null) ? challenge.getGfxId() : -1;
        return WakfuConfiguration.getInstance().getIconUrl("fightChallengeIconsPath", "defaultIconPath", gfxId);
    }
    
    @Override
    public int getType() {
        return 3;
    }
    
    @Override
    public int getId() {
        return this.m_challengeId;
    }
    
    @Override
    protected String getRemainingTime() {
        return null;
    }
    
    @Override
    protected ArrayList<AbstractQuestGoalView> getGoals() {
        return this.m_goal;
    }
    
    @Override
    protected String getRanking() {
        return null;
    }
    
    @Override
    protected String getStyle() {
        return null;
    }
    
    public void onStateChanged() {
        final FightChallengeGoalView view = this.m_goal.get(0);
        view.onStateChanged();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isCompleted", "isFailed");
    }
}
