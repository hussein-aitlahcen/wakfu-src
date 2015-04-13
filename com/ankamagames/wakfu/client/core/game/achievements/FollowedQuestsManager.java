package com.ankamagames.wakfu.client.core.game.achievements;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.fightChallenge.*;
import java.util.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import gnu.trove.*;

public class FollowedQuestsManager extends ImmutableFieldProvider
{
    public static final FollowedQuestsManager INSTANCE;
    public static final String FIGHT_CHALLENGE = "fightChallenge";
    public static final String ENVIRONMENT_QUEST = "environmentQuest";
    public static final String ALMANAX_QUEST = "almanaxQuest";
    public static final String REGULAR_QUESTS = "regularQuests";
    private int m_almanaxAchievementId;
    private int m_currentChallengeId;
    private int m_currentAchievementListId;
    private long m_characterId;
    
    public FollowedQuestsManager() {
        super();
        this.m_almanaxAchievementId = -1;
        this.m_currentChallengeId = -1;
        this.m_currentAchievementListId = -1;
    }
    
    public void init() {
        this.m_characterId = WakfuGameEntity.getInstance().getLocalPlayer().getId();
        this.m_almanaxAchievementId = -1;
        this.m_currentChallengeId = -1;
        this.m_currentAchievementListId = -1;
        PropertiesProvider.getInstance().setPropertyValue("followedAchievements", this);
        PropertiesProvider.getInstance().setPropertyValue("canFollowMoreAchievements", true);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("almanaxQuest")) {
            return (this.m_almanaxAchievementId == -1) ? null : AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, this.m_almanaxAchievementId);
        }
        if (fieldName.equals("environmentQuest")) {
            return (this.m_currentChallengeId == -1) ? null : ChallengeViewManager.INSTANCE.getChallengeView(this.m_currentChallengeId);
        }
        if (fieldName.equals("regularQuests")) {
            final Collection<AchievementView> quests = new ArrayList<AchievementView>();
            final TIntArrayList followed = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().getFollowed();
            for (int i = 0, size = followed.size(); i < size; ++i) {
                final AchievementView view = this.updateFollowedView(followed.get(i));
                quests.add(view);
            }
            return quests;
        }
        if (fieldName.equals("fightChallenge")) {
            return FightChallengeEventListener.INSTANCE.getChallenges();
        }
        return null;
    }
    
    private void setupRunningAlmanachQuest() {
        int almanaxAchievementId = -1;
        final ClientAchievementsContext context = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext();
        final TIntObjectIterator<Achievement> it = context.getAchievementIterator();
        while (it.hasNext()) {
            it.advance();
            final Achievement achievement = it.value();
            if (context.isAchievementActive(achievement.getId())) {
                if (context.isAchievementComplete(achievement.getId())) {
                    continue;
                }
                final boolean isAlmanachQuest = achievement.getCategory().hasParentInHierarchy(81);
                if (!isAlmanachQuest) {
                    continue;
                }
                almanaxAchievementId = achievement.getId();
                break;
            }
        }
        if (almanaxAchievementId != this.m_almanaxAchievementId) {
            this.m_almanaxAchievementId = almanaxAchievementId;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "almanaxQuest");
        }
    }
    
    private AchievementView updateFollowedView(final int achievementId) {
        final Achievement achievement = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().getAchievement(achievementId);
        if (achievement == null) {
            return null;
        }
        final AsbtractAchievementsView correctAchievementsView = AchievementsViewManager.INSTANCE.getCorrectAchievementsView(this.m_characterId, achievement.getId());
        if (correctAchievementsView == null) {
            return null;
        }
        final AchievementCategoryView categoryView = correctAchievementsView.getCategoryFromId(achievement.getCategory().getId());
        if (categoryView == null || categoryView.isHistory()) {
            return null;
        }
        final AchievementStandardCategoryView achievementStandardCategoryView = (AchievementStandardCategoryView)categoryView;
        return achievementStandardCategoryView.updateFollowedField(achievementId);
    }
    
    private void checkDialog() {
        if (!this.followsAchievements()) {
            UIAchievementsFrame.getInstance();
            UIAchievementsFrame.loadFollowedAchievements(false);
        }
        else {
            UIAchievementsFrame.getInstance();
            if (!UIAchievementsFrame.isFollowedAchievementDialogLoaded()) {
                UIAchievementsFrame.getInstance();
                UIAchievementsFrame.loadFollowedAchievements(true);
            }
        }
    }
    
    private boolean followsAchievements() {
        return this.m_currentAchievementListId == -1 && (this.m_almanaxAchievementId != -1 || this.m_currentChallengeId != -1 || !WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().getFollowed().isEmpty());
    }
    
    public void onFightChallengeChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "fightChallenge");
    }
    
    public void setCurrentChallengeId(final int currentChallengeId) {
        this.m_currentChallengeId = currentChallengeId;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "environmentQuest");
        this.checkDialog();
    }
    
    public void onAchievementFollowChanged(final int achievementId) {
        this.updateFollowedView(achievementId);
        this.onFollowedListChanged();
    }
    
    public void onFollowedListChanged() {
        this.setupRunningAlmanachQuest();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "regularQuests");
        final TIntArrayList followed = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().getFollowed();
        PropertiesProvider.getInstance().setPropertyValue("canFollowMoreAchievements", followed.size() < 4);
        this.checkDialog();
    }
    
    public void onAchievementActivated(final int achievementId) {
        final AchievementList achievementList = AchievementListManager.INSTANCE.getListByAchievementId(achievementId);
        if (achievementList != null) {
            this.m_currentAchievementListId = achievementList.getId();
            UIAchievementsFrame.getInstance().loadQuestListDescription(AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievementId));
        }
        this.checkDialog();
    }
    
    public void onAchievementFinished(final int achievementId) {
        final AchievementList achievementList = AchievementListManager.INSTANCE.getListByAchievementId(achievementId);
        if (achievementList != null) {
            final int lastAchievementId = achievementList.getAchievement(achievementList.size() - 1);
            if (achievementId == lastAchievementId) {
                this.m_currentAchievementListId = -1;
                UIAchievementsFrame.getInstance().unloadQuestListDescription();
            }
        }
        this.checkDialog();
    }
    
    public void onTimeTick() {
        if (this.m_currentChallengeId != -1) {
            final AbstractChallengeView challengeView = ChallengeViewManager.INSTANCE.getChallengeView(this.m_currentChallengeId);
            challengeView.updateRemainingTime();
        }
    }
    
    static {
        INSTANCE = new FollowedQuestsManager();
    }
}
