package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import gnu.trove.*;

public final class AchievementsModel
{
    public static final AchievementsModel INSTANCE;
    private static final Logger m_logger;
    private final TIntObjectHashMap<Variable> m_modelVariables;
    private final TIntObjectHashMap<Category> m_modelCategories;
    private final TIntObjectHashMap<Achievement> m_modelAchievements;
    private final TIntObjectHashMap<Objective> m_modelObjectives;
    private final TIntObjectHashMap<Reward> m_modelRewards;
    private final ArrayList<Category> m_rootCategories;
    private ClientAchievementsContext m_initialContext;
    
    public AchievementsModel() {
        super();
        this.m_modelVariables = new TIntObjectHashMap<Variable>();
        this.m_modelCategories = new TIntObjectHashMap<Category>();
        this.m_modelAchievements = new TIntObjectHashMap<Achievement>();
        this.m_modelObjectives = new TIntObjectHashMap<Objective>();
        this.m_modelRewards = new TIntObjectHashMap<Reward>();
        this.m_rootCategories = new ArrayList<Category>();
    }
    
    public boolean registerVariable(final int id, final String name, final boolean exportForSteam) {
        this.m_modelVariables.put(id, new Variable(id, name, exportForSteam));
        return true;
    }
    
    public boolean registerCategory(final int id, final int parentId, final String name) {
        final Category parent = this.m_modelCategories.get(parentId);
        if (parent == null && parentId != 0) {
            AchievementsModel.m_logger.error((Object)("Categorie parente non enregistr\u00e9e : " + parentId));
            return false;
        }
        final Category category = new Category(id, parent, name);
        if (parent != null) {
            parent.addChildCategory(category);
        }
        this.m_modelCategories.put(id, category);
        if (parent == null && !this.m_rootCategories.contains(category)) {
            this.m_rootCategories.add(category);
        }
        return true;
    }
    
    public boolean registerAchievement(final int id, final int categoryId, final boolean isVisible, final boolean notifyOnPass, final String name, final String description, final SimpleCriterion unlockingCriterion, final int duration, final long cooldown, final boolean shareable, final boolean repeatable, final boolean needsUserAccept, final int recommandedLevel, final int recommandedPlayers, final boolean followable, final int displayOnActivationDelay, final GameDateConst periodStartDate, final GameIntervalConst period, final boolean isActive, final boolean autoCompass, final int gfxId, final byte rank, final SimpleCriterion activationCriterion, final int order) {
        final Category category = this.m_modelCategories.get(categoryId);
        if (category != null) {
            final Achievement achievement = new Achievement(id, category, isVisible, notifyOnPass, name, description, unlockingCriterion, duration, cooldown, shareable, repeatable, needsUserAccept, recommandedLevel, recommandedPlayers, followable, displayOnActivationDelay, periodStartDate, period, autoCompass, gfxId, rank, activationCriterion, order);
            achievement.setActive(isActive);
            this.m_modelAchievements.put(id, achievement);
            category.addAchievement(achievement);
            return true;
        }
        AchievementsModel.m_logger.error((Object)("La cat\u00e9gorie sp\u00e9cifi\u00e9e n'est pas enregistr\u00e9e : " + categoryId));
        return false;
    }
    
    public boolean registerObjective(final int id, final int achievementId, final int[] relatedVariables, final String shortDescription, final String fullDescription, final boolean feedback, final boolean hasPositionFeedback, final short x, final short y, final short z, final short worldId) {
        final Achievement achievement = this.m_modelAchievements.get(achievementId);
        if (achievement != null) {
            Variable[] variables = null;
            if (relatedVariables != null && relatedVariables.length > 0) {
                variables = new Variable[relatedVariables.length];
                for (int i = 0; i < relatedVariables.length; ++i) {
                    variables[i] = this.m_modelVariables.get(relatedVariables[i]);
                    if (variables[i] == null) {
                        AchievementsModel.m_logger.error((Object)("Une variable sp\u00e9cifi\u00e9e n'est pas enregistr\u00e9e : " + relatedVariables[i]));
                        return false;
                    }
                }
            }
            final Objective objective = new Objective(id, achievement, variables, shortDescription, fullDescription, feedback, hasPositionFeedback, x, y, z, worldId);
            this.m_modelObjectives.put(id, objective);
            achievement.addObjective(objective);
            return true;
        }
        AchievementsModel.m_logger.error((Object)("L'achievement sp\u00e9cifi\u00e9 n'est pas enregistr\u00e9 : " + achievementId));
        return false;
    }
    
    public boolean registerReward(final int id, final int achievementId, final RewardType type, final int[] params) {
        final Achievement achievement = this.m_modelAchievements.get(achievementId);
        if (achievement == null) {
            AchievementsModel.m_logger.error((Object)("L'achievement sp\u00e9cifi\u00e9 n'est pas enregistr\u00e9 : " + achievementId));
            return false;
        }
        if (type == null) {
            AchievementsModel.m_logger.error((Object)("Le type de reward sp\u00e9cifi\u00e9 n'existe pas : " + type));
            return false;
        }
        final Reward reward = new Reward(id, achievement, type, params);
        this.m_modelRewards.put(id, reward);
        achievement.addReward(reward);
        return true;
    }
    
    public Achievement getAchievementModel(final int key) {
        return this.m_modelAchievements.get(key);
    }
    
    public ClientAchievementsContext createContext() {
        if (this.m_initialContext == null) {
            this.m_initialContext = new ClientAchievementsContext(0, this.m_modelVariables, this.m_modelAchievements);
        }
        return new ClientAchievementsContext(this.m_initialContext);
    }
    
    public ArrayList<Category> getRootCategories() {
        return this.m_rootCategories;
    }
    
    public Objective getObjective(final int key) {
        return this.m_modelObjectives.get(key);
    }
    
    public AchievementVariable getVariableByName(final String variableName) {
        final TIntObjectIterator<Variable> it = this.m_modelVariables.iterator();
        while (it.hasNext()) {
            it.advance();
            final Variable var = it.value();
            if (var.getName().equals(variableName)) {
                return var;
            }
        }
        return null;
    }
    
    public boolean forEachAchievementModel(final TIntObjectProcedure<Achievement> procedure) {
        return this.m_modelAchievements.forEachEntry(procedure);
    }
    
    static {
        INSTANCE = new AchievementsModel();
        m_logger = Logger.getLogger((Class)AchievementsModel.class);
    }
}
