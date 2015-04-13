package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import gnu.trove.*;

public class AchievementView extends AbstractQuestView
{
    public static final String NAME_FIELD = "name";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String IS_COMPLETED_FIELD = "isCompleted";
    public static final String IS_FOLLOWABLE_FIELD = "isFollowable";
    public static final String IS_ACTIVATED_FIELD = "isActivated";
    public static final String IS_FOLLOWED_FIELD = "isFollowed";
    public static final String PROGRESSION_TEXT_FIELD = "progressionText";
    public static final String PROGRESSION_VALUE_FIELD = "progressionValue";
    public static final String GOALS_FIELD = "goals";
    public static final String REMAINING_GOALS_FIELD = "remainingGoals";
    public static final String HAS_BASE_REWARDS = "hasBaseRewards";
    public static final String REWARDS_FIELD = "rewards";
    public static final String XP_REWARD_FIELD = "xpReward";
    public static final String APTITUDE_REWARD_FIELD = "aptitudeReward";
    public static final String TEXT_REWARDS_FIELD = "textRewards";
    public static final String VISUAL_REWARDS_FIELD = "visualRewards";
    public static final String STATE_COLOR_FIELD = "stateColor";
    public static final String IS_QUEST_FIELD = "isQuest";
    public static final String IS_SHAREABLE = "isShareable";
    public static final String DURATION = "duration";
    public static final String RECOMMANDED_LEVEL = "recommandedLevel";
    public static final String RECOMMANDED_PLAYERS = "recommandedPlayers";
    public static final String REMAINING_TIME = "remainingTime";
    public static final String COOLDOWN = "cooldown";
    public static final String RUNNING = "isRunning";
    public static final String CAN_RESET = "canReset";
    public static final String RANK = "rank";
    public static final String ACTIVABLE = "activable";
    public static final String ACTIVATION_CRITERION_DESCRIPTION = "activationCriterionDescription";
    public static final String MERCENARIES_STATE_COLOR = "mercenariesStateColor";
    public static final String CHARACTERS_WHO_WILL_ACTIVE_IT = "concernedCharacters";
    public static final String HEROES_PARTY_EXISTS = "heroesPartyExists";
    public static final String[] FIELDS;
    private static final Color COMPLETE_ACHIEVEMENT_COLOR;
    private static final Color ACTIVE_ACHIEVEMENT_COLOR;
    protected final int m_achievementId;
    private final int m_gfxId;
    private final int m_rootCategoryId;
    private final long m_characterId;
    protected final PlayerCharacter m_player;
    protected final ClientAchievementsContext m_achievementsContext;
    public static final GameIntervalConst TEN_SECONDS;
    
    public AchievementView(final long characterId, final int achievementId, final int rootCategoryId, final int gfxId) {
        super();
        this.m_characterId = characterId;
        this.m_player = HeroesManager.INSTANCE.getHero(characterId);
        this.m_achievementsContext = AchievementContextManager.INSTANCE.getContext(characterId);
        this.m_achievementId = achievementId;
        this.m_rootCategoryId = rootCategoryId;
        this.m_gfxId = gfxId;
    }
    
    @Override
    public String[] getFields() {
        return AchievementView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isCompleted")) {
            return this.m_achievementsContext.isAchievementComplete(this.m_achievementId);
        }
        if (fieldName.equals("isActivated")) {
            return this.isActive();
        }
        if (fieldName.equals("isFollowed")) {
            return this.isFollowed();
        }
        if (fieldName.equals("progressionText")) {
            return this.countGoalsCompleted() + "/" + this.getObjectiveTotalSize();
        }
        if (fieldName.equals("progressionValue")) {
            return this.countGoalsCompleted() / this.getObjectiveTotalSize();
        }
        if (fieldName.equals("remainingGoals")) {
            final Collection<AchievementGoalView> goals = new ArrayList<AchievementGoalView>();
            final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
            for (final Objective objective : achievement.getObjectives()) {
                if (objective.isFeedback() && !objective.isCompleted()) {
                    final AchievementGoalView objectiveView = AchievementsViewManager.INSTANCE.getObjective(this.m_characterId, objective.getId());
                    goals.add(objectiveView);
                }
            }
            return goals;
        }
        if (fieldName.equals("aptitudeReward")) {
            final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
            for (final Reward reward : achievement2.getRewards()) {
                if (reward.getType() == AchievementRewardType.GIVE_PLAYER_APTITUDE_POINTS) {
                    return new AchievementRewardView(reward);
                }
            }
            return null;
        }
        if (fieldName.equals("xpReward")) {
            final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
            for (final Reward reward : achievement2.getRewards()) {
                if (reward.getType() == AchievementRewardType.GIVE_PLAYER_XP || reward.getType() == AchievementRewardType.GIVE_PLAYER_XP_IN_PERCENT) {
                    return new AchievementRewardView(reward);
                }
            }
            return null;
        }
        if (fieldName.equals("textRewards")) {
            final Collection<AchievementRewardView> rewards = new ArrayList<AchievementRewardView>();
            final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
            for (final Reward reward2 : achievement.getRewards()) {
                if (reward2.getType().isDisplayed() && reward2.getType().isTextReward() && reward2.getType() != AchievementRewardType.GIVE_PLAYER_XP && reward2.getType() != AchievementRewardType.GIVE_PLAYER_XP_IN_PERCENT && reward2.getType() != AchievementRewardType.GIVE_PLAYER_APTITUDE_POINTS) {
                    rewards.add(new AchievementRewardView(reward2));
                }
            }
            return rewards;
        }
        if (fieldName.equals("visualRewards")) {
            final Collection<AchievementRewardView> rewards = new ArrayList<AchievementRewardView>();
            final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
            for (final Reward reward2 : achievement.getRewards()) {
                if (reward2.getType().isDisplayed() && !reward2.getType().isTextReward()) {
                    rewards.add(new AchievementRewardView(reward2));
                }
            }
            return rewards;
        }
        if (fieldName.equals("rewards")) {
            final Collection<AchievementRewardView> rewards = new ArrayList<AchievementRewardView>();
            final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
            for (final Reward reward2 : achievement.getRewards()) {
                rewards.add(new AchievementRewardView(reward2));
            }
            return rewards;
        }
        if (fieldName.equals("hasBaseRewards")) {
            final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
            for (final Reward reward : achievement2.getRewards()) {
                if ((reward.getType().isDisplayed() && !reward.getType().isTextReward()) || (reward.getType() != AchievementRewardType.GIVE_PLAYER_XP && reward.getType() != AchievementRewardType.GIVE_PLAYER_XP_IN_PERCENT && reward.getType() != AchievementRewardType.GIVE_PLAYER_APTITUDE_POINTS)) {
                    return true;
                }
            }
            return false;
        }
        if (fieldName.equals("stateColor")) {
            final ClientAchievementsContext context = this.m_achievementsContext;
            if (context.isAchievementComplete(this.m_achievementId)) {
                return AchievementView.COMPLETE_ACHIEVEMENT_COLOR;
            }
            if (context.isAchievementActive(this.m_achievementId)) {
                return Color.WHITE;
            }
            return Color.RED;
        }
        else {
            if (fieldName.equals("isQuest")) {
                return AchievementsViewManager.INSTANCE.isQuest(this.m_characterId, this.m_achievementId);
            }
            if (fieldName.equals("duration")) {
                final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                if (achievement2.getDuration() <= 0) {
                    return null;
                }
                final GameIntervalConst durationInterval = GameInterval.fromSeconds(achievement2.getDuration() / 1000);
                return WakfuTranslator.getInstance().getString("duration") + TimeUtils.getLongDescription(durationInterval);
            }
            else {
                if (fieldName.equals("isShareable")) {
                    final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                    final boolean active = AchievementHelper.isActive(achievement2, WakfuGameCalendar.getInstance().getDate());
                    return achievement2.isShareable() && active && !achievement2.isComplete();
                }
                if (fieldName.equals("recommandedPlayers")) {
                    final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                    return achievement2.getRecommandedPlayers();
                }
                if (fieldName.equals("recommandedLevel")) {
                    final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                    return WakfuTranslator.getInstance().getString("levelShort.custom", achievement2.getRecommandedLevel());
                }
                if (fieldName.equals("isFollowable")) {
                    final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                    return achievement2.isFollowable();
                }
                if (fieldName.equals("canReset")) {
                    return this.m_achievementsContext.canResetAchievement(this.m_achievementId);
                }
                if (fieldName.equals("isRunning")) {
                    return this.m_achievementsContext.isAchievementRunning(this.m_achievementId);
                }
                if (fieldName.equals("rank")) {
                    final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                    return achievement2.getRank();
                }
                if (fieldName.equals("activationCriterionDescription")) {
                    final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                    final TextWidgetFormater sb = new TextWidgetFormater();
                    final String description = CriterionDescriptionGenerator.getDescription(achievement2.getActivationCriterion());
                    sb.openText().addColor(this.m_achievementsContext.isAchievementActivable(this.m_achievementId, this.m_player) ? Color.WHITE : Color.RED);
                    sb.append(description);
                    return sb.finishAndToString();
                }
                if (fieldName.equals("mercenariesStateColor")) {
                    if (this.m_achievementsContext.isAchievementComplete(this.m_achievementId)) {
                        return AchievementView.COMPLETE_ACHIEVEMENT_COLOR;
                    }
                    if (this.m_achievementsContext.isAchievementActive(this.m_achievementId)) {
                        return AchievementView.ACTIVE_ACHIEVEMENT_COLOR;
                    }
                    if (this.m_achievementsContext.isAchievementActivable(this.m_achievementId, this.m_player)) {
                        return Color.WHITE;
                    }
                    return Color.RED;
                }
                else {
                    if (fieldName.equals("activable")) {
                        return this.isActivable();
                    }
                    if (fieldName.equals("concernedCharacters")) {
                        return this.activationState();
                    }
                    if (fieldName.equals("heroesPartyExists")) {
                        return HeroesManager.INSTANCE.getHeroesInPartyQuantity(this.m_player.getOwnerId()) > 1;
                    }
                    if (!fieldName.equals("cooldown")) {
                        return super.getFieldValue(fieldName);
                    }
                    final Achievement achievement2 = this.m_achievementsContext.getAchievement(this.m_achievementId);
                    if (!achievement2.isComplete()) {
                        return null;
                    }
                    GameIntervalConst remainingTime;
                    if (achievement2.getCooldown() != 0L && achievement2.getLastCompletedTime() != 0L) {
                        remainingTime = AchievementHelper.getCoolDown(achievement2, WakfuGameCalendar.getInstance().getDate());
                    }
                    else if (achievement2.getPeriodStartDate() != null) {
                        remainingTime = AchievementHelper.getPeriodCoolDown(achievement2, WakfuGameCalendar.getInstance().getDate());
                    }
                    else {
                        remainingTime = GameIntervalConst.EMPTY_INTERVAL;
                    }
                    if (!remainingTime.isPositive()) {
                        return null;
                    }
                    return TimeUtils.getShortDescription(remainingTime);
                }
            }
        }
    }
    
    public int getOrder() {
        final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
        return achievement.getOrder();
    }
    
    private boolean isActivable() {
        final TLongHashSet heroesInParty = HeroesManager.INSTANCE.getHeroesInParty(this.m_player.getOwnerId());
        for (final long heroId : heroesInParty) {
            final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(heroId);
            if (context.canResetAchievement(this.m_achievementId) && context.isAchievementActivable(this.m_achievementId, HeroesManager.INSTANCE.getHero(heroId))) {
                return true;
            }
        }
        return false;
    }
    
    private String activationState() {
        final StringBuilder whoActivable = new StringBuilder();
        final StringBuilder whoActivated = new StringBuilder();
        final StringBuilder whoDone = new StringBuilder();
        final TLongHashSet heroesInParty = HeroesManager.INSTANCE.getHeroesInParty(this.m_player.getOwnerId());
        final TLongIterator it = heroesInParty.iterator();
        boolean firstDone = true;
        boolean firstActivated = true;
        boolean firstActivable = true;
        while (it.hasNext()) {
            final long heroId = it.next();
            final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(heroId);
            final PlayerCharacter hero = HeroesManager.INSTANCE.getHero(heroId);
            final boolean activable = context.canResetAchievement(this.m_achievementId) && context.isAchievementActivable(this.m_achievementId, hero);
            final boolean activated = context.isAchievementActive(this.m_achievementId) && !context.isAchievementComplete(this.m_achievementId);
            final boolean done = context.isAchievementActive(this.m_achievementId) && context.isAchievementComplete(this.m_achievementId);
            if (done && firstDone) {
                whoDone.append(hero.getName());
                firstDone = false;
            }
            else if (done) {
                whoDone.append(", ").append(hero.getName());
            }
            else if (activable && firstActivable) {
                whoActivable.append(hero.getName());
                firstActivable = false;
            }
            else if (activable) {
                whoActivable.append(", ").append(hero.getName());
            }
            else if (activated && firstActivated) {
                whoActivated.append(hero.getName());
                firstActivated = false;
            }
            else {
                if (!activated) {
                    continue;
                }
                whoActivated.append(", ").append(hero.getName());
            }
        }
        final TextWidgetFormater who = new TextWidgetFormater();
        if (!whoActivable.toString().isEmpty()) {
            who.b().append(WakfuTranslator.getInstance().getString("available") + ' ' + WakfuTranslator.getInstance().getString("colon"))._b().append(whoActivable);
        }
        if (!whoActivated.toString().isEmpty()) {
            if (!who.toString().isEmpty()) {
                who.newLine();
            }
            who.b().append(WakfuTranslator.getInstance().getString("inProgress") + ' ' + WakfuTranslator.getInstance().getString("colon"))._b().append(whoActivated);
        }
        if (!whoDone.toString().isEmpty()) {
            if (!who.toString().isEmpty()) {
                who.append('\n');
            }
            who.b().append(WakfuTranslator.getInstance().getString("terminated") + ' ' + WakfuTranslator.getInstance().getString("colon"))._b().append(whoDone);
        }
        return who.toString();
    }
    
    @Override
    protected String getBackgroundText() {
        final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
        if (achievement.getCategory().hasParentInHierarchy(81)) {
            return null;
        }
        return WakfuTranslator.getInstance().getString(63, this.m_achievementId, new Object[0]);
    }
    
    @Override
    protected String getName() {
        return WakfuTranslator.getInstance().getString(62, this.m_achievementId, new Object[0]);
    }
    
    @Override
    protected String getIconUrl() {
        return WakfuConfiguration.getInstance().getAchievementIconUrl(this.m_gfxId);
    }
    
    @Override
    public int getType() {
        return 1;
    }
    
    @Override
    protected String getRemainingTime() {
        final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
        if (!achievement.isActive() || achievement.getStartTime() == 0L) {
            return null;
        }
        GameIntervalConst remainingTime;
        if (achievement.getDuration() != 0) {
            remainingTime = AchievementHelper.getRemainingTime(achievement, WakfuGameCalendar.getInstance().getDate());
        }
        else if (achievement.getPeriodStartDate() != null) {
            remainingTime = AchievementHelper.getPeriodRemainingTime(achievement, WakfuGameCalendar.getInstance().getDate());
        }
        else {
            remainingTime = GameIntervalConst.EMPTY_INTERVAL;
        }
        if (!remainingTime.isPositive()) {
            return null;
        }
        return TimeUtils.getShortDescription(remainingTime);
    }
    
    @Override
    protected ArrayList<AbstractQuestGoalView> getGoals() {
        final ArrayList<AbstractQuestGoalView> goals = new ArrayList<AbstractQuestGoalView>();
        final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
        for (final Objective objective : achievement.getObjectives()) {
            if (objective.isFeedback()) {
                final AchievementGoalView objectiveView = AchievementsViewManager.INSTANCE.getObjective(this.m_characterId, objective.getId());
                goals.add(objectiveView);
            }
        }
        return goals;
    }
    
    @Override
    protected String getRanking() {
        return null;
    }
    
    @Override
    protected String getStyle() {
        return null;
    }
    
    public boolean isFollowed() {
        return this.m_achievementsContext.isFollowed(this.m_achievementId);
    }
    
    public boolean isInCount() {
        return this.m_achievementsContext.getAchievement(this.m_achievementId).isVisible();
    }
    
    public boolean isVisible() {
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(this.m_characterId);
        return hero != null && this.m_achievementsContext.isAchievementVisible(this.m_achievementId, (AchievementsContextProvider)hero);
    }
    
    public boolean isActive() {
        return this.m_achievementsContext.isAchievementActive(this.m_achievementId);
    }
    
    private int getObjectiveTotalSize() {
        final ArrayList<Objective> objectives = this.m_achievementsContext.getAchievement(this.m_achievementId).getObjectives();
        int size = 0;
        for (final Objective obj : objectives) {
            if (obj.isFeedback()) {
                ++size;
            }
        }
        return size;
    }
    
    private int countGoalsCompleted() {
        int count = 0;
        final ClientAchievementsContext context = this.m_achievementsContext;
        for (final Objective objective : context.getAchievement(this.m_achievementId).getObjectives()) {
            if (objective.isFeedback() && (!context.hasObjective(objective.getId()) || context.isObjectiveCompleted(objective.getId()))) {
                ++count;
            }
        }
        return count;
    }
    
    @Override
    public int getId() {
        return this.m_achievementId;
    }
    
    public int getRootCategoryId() {
        return this.m_rootCategoryId;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof AchievementView && this.m_achievementId == ((AchievementView)obj).m_achievementId;
    }
    
    public void updateFollowedField() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isFollowed");
    }
    
    public void updateObjectivesField() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "progressionText", "progressionValue", "remainingGoals");
    }
    
    public void updateStatesField() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "mercenariesStateColor", "activable", "activationCriterionDescription", "isActivated", "isCompleted", "isRunning", "canReset");
    }
    
    public void updateTimeDisplay() {
        final Achievement achievement = this.m_achievementsContext.getAchievement(this.m_achievementId);
        if (!this.m_achievementsContext.isAchievementComplete(this.m_achievementId) && achievement.isActive() && achievement.getPeriodStartDate() != null) {
            final GameIntervalConst remainingTime = AchievementHelper.getPeriodRemainingTime(achievement, WakfuGameCalendar.getInstance().getDate());
            if (remainingTime.lowerThan(AchievementView.TEN_SECONDS)) {
                this.m_achievementsContext.failAchievement(achievement.getId(), false);
                if (this.m_achievementsContext.isFollowed(achievement.getId())) {
                    final AchievementFollowRequestMessage netmsg = new AchievementFollowRequestMessage(achievement.getId(), false);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netmsg);
                }
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remainingTime", "cooldown");
    }
    
    @Override
    public String toString() {
        return "AchievementView{m_achievementId=" + this.m_achievementId + ", m_gfxId=" + this.m_gfxId + ", m_rootCategoryId=" + this.m_rootCategoryId + '}';
    }
    
    static {
        FIELDS = new String[] { "name", "iconUrl", "isCompleted", "isFollowed", "isActivated", "progressionText", "progressionValue", "backgroundText", "goals", "rewards", "textRewards", "stateColor", "remainingGoals", "isQuest" };
        COMPLETE_ACHIEVEMENT_COLOR = new Color(0.3f, 0.9f, 0.2f, 1.0f);
        ACTIVE_ACHIEVEMENT_COLOR = new Color(255, 222, 108, 255);
        TEN_SECONDS = new GameInterval(10, 0, 0, 0);
    }
}
