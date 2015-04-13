package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class Achievement implements AchievementInstance
{
    private final int m_id;
    private final Category m_category;
    private final boolean m_isVisible;
    private final boolean m_notifyOnPass;
    private final String m_name;
    private final String m_description;
    private final int m_duration;
    private final long m_cooldown;
    private final boolean m_shareable;
    private final boolean m_repeatable;
    private final boolean m_needsUserAccept;
    private final boolean m_followable;
    private final boolean m_autoCompass;
    private final int m_gfxId;
    private final int m_displayOnActivationDelay;
    private final ArrayList<Objective> m_objectives;
    private final ArrayList<Reward> m_rewards;
    private boolean m_active;
    private boolean m_completed;
    private long m_lastCompletedTime;
    private long m_startTime;
    private final int m_recommandedLevel;
    private final int m_recommandedPlayers;
    private final byte m_rank;
    private final int m_order;
    private final SimpleCriterion m_unlockingCriterion;
    private final SimpleCriterion m_activationCriterion;
    private GameDateConst m_periodStartDate;
    private GameIntervalConst m_period;
    
    Achievement(final Achievement achievement) {
        super();
        this.m_objectives = new ArrayList<Objective>();
        this.m_rewards = new ArrayList<Reward>();
        this.m_id = achievement.m_id;
        this.m_active = achievement.m_active;
        this.m_category = achievement.m_category;
        this.m_isVisible = achievement.m_isVisible;
        this.m_notifyOnPass = achievement.m_notifyOnPass;
        this.m_name = achievement.m_name;
        this.m_description = achievement.m_description;
        this.m_unlockingCriterion = achievement.m_unlockingCriterion;
        this.m_duration = achievement.m_duration;
        this.m_cooldown = achievement.m_cooldown;
        this.m_shareable = achievement.m_shareable;
        this.m_repeatable = achievement.m_repeatable;
        this.m_needsUserAccept = achievement.m_needsUserAccept;
        this.m_recommandedLevel = achievement.m_recommandedLevel;
        this.m_recommandedPlayers = achievement.m_recommandedPlayers;
        this.m_followable = achievement.m_followable;
        this.m_displayOnActivationDelay = achievement.m_displayOnActivationDelay;
        this.m_periodStartDate = achievement.m_periodStartDate;
        this.m_period = achievement.m_period;
        this.m_autoCompass = achievement.m_autoCompass;
        this.m_gfxId = achievement.m_gfxId;
        this.m_rank = achievement.m_rank;
        this.m_activationCriterion = achievement.m_activationCriterion;
        this.m_order = achievement.m_order;
    }
    
    Achievement(final int id, final Category category, final boolean isVisible, final boolean notifyOnPass, final String name, final String description, final SimpleCriterion unlockingCriterion, final int duration, final long cooldown, final boolean shareable, final boolean repeatable, final boolean needsUserAccept, final int recommandedLevel, final int recommandedPlayers, final boolean followable, final int displayOnActivationDelay, final GameDateConst periodStartDate, final GameIntervalConst period, final boolean autoCompass, final int gfxId, final byte rank, final SimpleCriterion activationCriterion, final int order) {
        super();
        this.m_objectives = new ArrayList<Objective>();
        this.m_rewards = new ArrayList<Reward>();
        this.m_id = id;
        this.m_category = category;
        this.m_isVisible = isVisible;
        this.m_notifyOnPass = notifyOnPass;
        this.m_rank = rank;
        this.m_activationCriterion = activationCriterion;
        this.m_name = ((name != null) ? name.intern() : null);
        this.m_description = ((description != null) ? description.intern() : null);
        this.m_unlockingCriterion = unlockingCriterion;
        this.m_duration = duration;
        this.m_cooldown = cooldown;
        this.m_shareable = shareable;
        this.m_repeatable = repeatable;
        this.m_needsUserAccept = needsUserAccept;
        this.m_recommandedLevel = recommandedLevel;
        this.m_recommandedPlayers = recommandedPlayers;
        this.m_followable = followable;
        this.m_displayOnActivationDelay = displayOnActivationDelay;
        this.m_periodStartDate = periodStartDate;
        this.m_period = period;
        this.m_autoCompass = autoCompass;
        this.m_gfxId = gfxId;
        this.m_order = order;
    }
    
    void addObjective(final Objective objective) {
        if (!this.m_objectives.contains(objective)) {
            this.m_objectives.add(objective);
        }
    }
    
    void addReward(final Reward reward) {
        if (!this.m_rewards.contains(reward)) {
            this.m_rewards.add(reward);
        }
    }
    
    public boolean isVisible() {
        return this.m_isVisible;
    }
    
    public boolean isVisible(final AchievementsContextProvider achievementContextProvider) {
        return this.m_isVisible && (this.m_unlockingCriterion == null || this.m_unlockingCriterion.isValid(achievementContextProvider, null, null, achievementContextProvider.getAchievementsContext()));
    }
    
    public boolean isActivable(final AchievementsContextProvider provider) {
        return this.m_activationCriterion == null || this.m_activationCriterion.isValid(provider, null, null, provider.getAchievementsContext());
    }
    
    @Override
    public boolean isActive() {
        return this.m_active;
    }
    
    void activate(final ClientAchievementsContext context) {
        this.m_active = true;
        if (this.m_periodStartDate != null || this.m_duration != 0) {
            final GameDateConst date = BaseGameDateProvider.INSTANCE.getDate();
            this.m_startTime = date.toLong();
        }
    }
    
    @Override
    public void setActive(final boolean active) {
        this.m_active = active;
    }
    
    @Override
    public boolean isComplete() {
        return this.m_completed;
    }
    
    void complete(final ClientAchievementsContext context) {
        this.m_completed = true;
        final GameDateConst date = BaseGameDateProvider.INSTANCE.getDate();
        if (this.m_cooldown != 0L) {
            this.m_lastCompletedTime = date.toLong();
        }
    }
    
    void setCompleted(final boolean completed) {
        this.m_completed = completed;
    }
    
    @Override
    public void setLastCompletedTime(final long lastCompletedTime) {
        this.m_lastCompletedTime = lastCompletedTime;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    public Category getCategory() {
        return this.m_category;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public ArrayList<Objective> getObjectives() {
        return this.m_objectives;
    }
    
    public ArrayList<Reward> getRewards() {
        return this.m_rewards;
    }
    
    @Override
    public int getDuration() {
        return this.m_duration;
    }
    
    @Override
    public long getCooldown() {
        return this.m_cooldown;
    }
    
    public boolean isNotifyOnPass() {
        return this.m_notifyOnPass;
    }
    
    public boolean isFollowable() {
        return this.m_followable;
    }
    
    public boolean isShareable() {
        return this.m_shareable;
    }
    
    @Override
    public boolean isRepeatable() {
        return this.m_repeatable;
    }
    
    @Override
    public long getLastCompletedTime() {
        return this.m_lastCompletedTime;
    }
    
    public boolean isNeedsUserAccept() {
        return this.m_needsUserAccept;
    }
    
    @Override
    public long getStartTime() {
        return this.m_startTime;
    }
    
    public void setStartTime(final long startTime) {
        this.m_startTime = startTime;
    }
    
    public int getRecommandedLevel() {
        return this.m_recommandedLevel;
    }
    
    public int getRecommandedPlayers() {
        return this.m_recommandedPlayers;
    }
    
    public int getDisplayOnActivationDelay() {
        return this.m_displayOnActivationDelay;
    }
    
    public void reset() {
        this.m_completed = false;
        for (int i = 0; i < this.m_objectives.size(); ++i) {
            this.m_objectives.get(i).reset();
        }
    }
    
    public void setPeriodStartDate(final GameDateConst periodStartDate) {
        this.m_periodStartDate = periodStartDate;
    }
    
    public void setPeriod(final GameIntervalConst period) {
        this.m_period = period;
    }
    
    @Override
    public GameDateConst getPeriodStartDate() {
        return this.m_periodStartDate;
    }
    
    @Override
    public GameIntervalConst getPeriod() {
        return this.m_period;
    }
    
    @Override
    public boolean canRepeatIfFailed() {
        return true;
    }
    
    public boolean isAutoCompass() {
        return this.m_autoCompass;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    @Override
    public byte getRank() {
        return this.m_rank;
    }
    
    @Override
    public int getOrder() {
        return this.m_order;
    }
    
    public SimpleCriterion getActivationCriterion() {
        return this.m_activationCriterion;
    }
}
