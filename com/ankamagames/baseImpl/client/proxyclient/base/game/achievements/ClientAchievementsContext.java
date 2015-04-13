package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import gnu.trove.*;
import java.nio.*;

public final class ClientAchievementsContext implements AchievementsContext
{
    private static final int ACHIEVEMENT_MAX_LENGTH = 5;
    private static final boolean DEBUG_MODE = true;
    private static final Logger m_logger;
    private int m_version;
    private final TIntObjectHashMap<Variable> m_variables;
    private final TIntObjectHashMap<Achievement> m_achievements;
    private final TIntObjectHashMap<Objective> m_objectives;
    private final TIntObjectHashMap<TIntArrayList> m_objectivesByVariable;
    private final TIntArrayList m_followedAchievements;
    private final ArrayList<AchievementHistoryEntry> m_history;
    private AchievementsContextEventListener m_listener;
    private final TIntLongHashMap m_lastActivationTry;
    
    ClientAchievementsContext(final int version, final TIntObjectHashMap<Variable> variables, final TIntObjectHashMap<Achievement> achievements) {
        super();
        this.m_variables = new TIntObjectHashMap<Variable>();
        this.m_achievements = new TIntObjectHashMap<Achievement>();
        this.m_objectives = new TIntObjectHashMap<Objective>();
        this.m_objectivesByVariable = new TIntObjectHashMap<TIntArrayList>();
        this.m_followedAchievements = new TIntArrayList();
        this.m_history = new ArrayList<AchievementHistoryEntry>();
        this.m_lastActivationTry = new TIntLongHashMap();
        this.m_version = version;
        variables.forEachEntry(new TIntObjectProcedure<Variable>() {
            @Override
            public boolean execute(final int id, final Variable variable) {
                ClientAchievementsContext.this.m_variables.put(id, new Variable(variable));
                return true;
            }
        });
        achievements.forEachEntry(new TIntObjectProcedure<Achievement>() {
            @Override
            public boolean execute(final int id, final Achievement achievement) {
                final Achievement newAchievement = new Achievement(achievement);
                ClientAchievementsContext.this.m_achievements.put(id, newAchievement);
                final ArrayList<Objective> objectives = achievement.getObjectives();
                for (int i = 0, size = objectives.size(); i < size; ++i) {
                    final Objective objective = objectives.get(i);
                    final Variable[] variables = objective.getRelatedVariables();
                    final Variable[] newVariables = new Variable[(variables != null) ? variables.length : 0];
                    if (variables != null) {
                        for (int k = 0; k < variables.length; ++k) {
                            final Variable variable = variables[k];
                            newVariables[k] = new Variable(variable);
                            TIntArrayList objectivesByVar = ClientAchievementsContext.this.m_objectivesByVariable.get(variable.getId());
                            if (objectivesByVar == null) {
                                objectivesByVar = new TIntArrayList();
                                ClientAchievementsContext.this.m_objectivesByVariable.put(variable.getId(), objectivesByVar);
                            }
                            objectivesByVar.add(objective.getId());
                        }
                    }
                    final Objective newObjective = new Objective(objective, newAchievement, (Variable[])((variables != null) ? newVariables : null));
                    newAchievement.addObjective(newObjective);
                    ClientAchievementsContext.this.m_objectives.put(newObjective.getId(), newObjective);
                }
                final ArrayList<Reward> rewards = achievement.getRewards();
                if (rewards != null && !rewards.isEmpty()) {
                    for (int j = 0, size2 = rewards.size(); j < size2; ++j) {
                        final Reward reward = rewards.get(j);
                        newAchievement.addReward(reward);
                    }
                }
                return true;
            }
        });
    }
    
    ClientAchievementsContext(final ClientAchievementsContext context) {
        this(context.m_version, context.m_variables, context.m_achievements);
    }
    
    public void setEventListener(final AchievementsContextEventListener listener) {
        this.m_listener = listener;
    }
    
    public AchievementsContextEventListener getListener() {
        return this.m_listener;
    }
    
    public TIntArrayList getObjectivesByVariable(final int variableId) {
        return this.m_objectivesByVariable.get(variableId);
    }
    
    public void updateVariable(final int id, final long value) {
        final Variable variable = this.m_variables.get(id);
        if (variable != null) {
            final long previousValue = variable.getValue();
            variable.setValue(value);
            if (this.m_listener != null) {
                this.m_listener.onVarUpdated(variable, value - previousValue);
            }
            return;
        }
        throw new RuntimeException("Variable inconnue : " + id);
    }
    
    @Override
    public boolean isObjectiveCompleted(final int id) {
        final Objective objective = this.m_objectives.get(id);
        if (objective != null) {
            return objective.isCompleted();
        }
        throw new RuntimeException("Objectif inconnu : " + id);
    }
    
    @Override
    public boolean hasObjective(final int id) {
        return this.m_objectives.containsKey(id);
    }
    
    public boolean hasVariable(final int id) {
        return this.m_variables.containsKey(id);
    }
    
    @Override
    public boolean hasAchievement(final int id) {
        return this.m_achievements.containsKey(id);
    }
    
    public boolean isAchievementActivable(final int id, final AchievementsContextProvider provider) {
        final Achievement achievement = this.m_achievements.get(id);
        if (achievement != null) {
            return achievement.isActivable(provider);
        }
        throw new RuntimeException("Achievement inconnu : " + id);
    }
    
    public boolean isAchievementVisible(final int id, final AchievementsContextProvider provider) {
        final Achievement achievement = this.m_achievements.get(id);
        if (achievement != null) {
            return achievement.isVisible(provider);
        }
        throw new RuntimeException("Achievement inconnu : " + id);
    }
    
    @Override
    public boolean isAchievementFailed(final int achievementId) {
        final Achievement achievement = this.m_achievements.get(achievementId);
        if (achievement != null) {
            return AchievementHelper.isFailed(achievement, BaseGameDateProvider.INSTANCE.getDate());
        }
        throw new RuntimeException("Achievement inconnu : " + achievementId);
    }
    
    @Override
    public boolean isAchievementRunning(final int achievementId) {
        final Achievement achievement = this.m_achievements.get(achievementId);
        if (achievement != null) {
            return AchievementHelper.isActive(achievement, BaseGameDateProvider.INSTANCE.getDate()) && !achievement.isComplete();
        }
        throw new RuntimeException("Achievement inconnu : " + achievementId);
    }
    
    @Override
    public boolean isAchievementRepeatable(final int achievementId) {
        final Achievement achievement = this.m_achievements.get(achievementId);
        if (achievement != null) {
            return achievement.isRepeatable();
        }
        throw new RuntimeException("Achievement inconnu : " + achievementId);
    }
    
    @Override
    public boolean isAchievementComplete(final int id) {
        final Achievement achievement = this.m_achievements.get(id);
        if (achievement != null) {
            return achievement.isComplete();
        }
        throw new RuntimeException("Achievement inconnu : " + id);
    }
    
    @Override
    public AchievementVariable getVariableByName(final String variableName) {
        return AchievementsModel.INSTANCE.getVariableByName(variableName);
    }
    
    @Override
    public boolean isAchievementActive(final int id) {
        final Achievement achievement = this.m_achievements.get(id);
        if (achievement != null) {
            return AchievementHelper.isActive(achievement, BaseGameDateProvider.INSTANCE.getDate());
        }
        throw new RuntimeException("Achievement inconnu : " + id);
    }
    
    @Override
    public ObjectiveDefinition getObjective(final int objectiveId) {
        return AchievementsModel.INSTANCE.getObjective(objectiveId);
    }
    
    @Override
    public long getVariableValue(final int id) {
        final Variable variable = this.m_variables.get(id);
        if (variable != null) {
            return variable.getValue();
        }
        throw new RuntimeException("Variable inconnue : " + id);
    }
    
    @Override
    public boolean isFollowed(final int achievementId) {
        return this.m_followedAchievements.contains(achievementId);
    }
    
    @Override
    public boolean setFollowed(final int achievementId, final boolean follow) {
        if (follow) {
            if (this.m_followedAchievements.size() >= 4) {
                return false;
            }
            if (!this.m_followedAchievements.contains(achievementId)) {
                this.m_followedAchievements.add(achievementId);
                this.m_listener.onAchievementFollowed(achievementId, true);
                return true;
            }
        }
        else if (TroveUtils.removeFirstValue(this.m_followedAchievements, achievementId)) {
            this.m_listener.onAchievementFollowed(achievementId, false);
            return true;
        }
        return false;
    }
    
    public void requestAchievementActivation(final int achievementId, final long inviterId) {
        final Achievement achievement = this.m_achievements.get(achievementId);
        if (achievement != null) {
            this.m_listener.onAchievementActivationRequired(achievement, inviterId);
        }
    }
    
    public void failAchievement(final int achievementId) {
        this.failAchievement(achievementId, true);
    }
    
    public void failAchievement(final int achievementId, final boolean feedback) {
        final Achievement achievement = this.m_achievements.get(achievementId);
        if (achievement != null) {
            achievement.reset();
            achievement.setActive(false);
            if (feedback) {
                this.m_listener.onAchievementFailed(achievement);
            }
            final boolean wasFollowed = this.setFollowed(achievementId, false);
            if (wasFollowed) {
                this.m_listener.onAchievementFollowed(achievementId, false);
            }
        }
    }
    
    @Override
    public TIntArrayList getFollowed() {
        return this.m_followedAchievements;
    }
    
    public void completeObjective(final int id) {
        final Objective objective = this.m_objectives.get(id);
        if (objective != null) {
            objective.setCompleted(true);
            if (this.m_listener != null) {
                this.m_listener.onObjectiveCompleted(objective);
            }
            ClientAchievementsContext.m_logger.info((Object)("Achievement objective completed : " + id));
        }
        else {
            ClientAchievementsContext.m_logger.error((Object)("Impossible de trouver l'objectif " + id + " dans le contexte du joueur"));
        }
    }
    
    public void activateAchievement(final int id) {
        final Achievement achievement = this.m_achievements.get(id);
        if (achievement != null) {
            achievement.activate(this);
            ClientAchievementsContext.m_logger.info((Object)("Achievement activated : " + id));
            if (this.m_listener != null) {
                this.m_listener.onAchievementActivated(achievement);
            }
        }
        else {
            ClientAchievementsContext.m_logger.error((Object)("Impossible de trouver l'achievement " + id + " dans le contexte du joueur"));
        }
    }
    
    public void completeAchievement(final int id, final long unlockTime) {
        final Achievement achievement = this.m_achievements.get(id);
        if (achievement != null) {
            achievement.complete(this);
            ClientAchievementsContext.m_logger.info((Object)("! Achievement unlocked ! : " + id + " on " + new Date(unlockTime) + " (server time)"));
            this.m_history.add(new AchievementHistoryEntry(id, unlockTime));
            final int historyLen = this.m_history.size();
            if (historyLen > 0 && historyLen > 5) {
                this.m_history.remove(0);
            }
            final boolean wasFollowed = this.setFollowed(id, false);
            if (this.m_listener != null) {
                this.m_listener.onAchievementCompleted(achievement);
                if (wasFollowed) {
                    this.m_listener.onAchievementFollowed(id, false);
                }
            }
        }
        else {
            ClientAchievementsContext.m_logger.error((Object)("Impossible de trouver l'achievement " + id + " dans le contexte du joueur"));
        }
    }
    
    public ArrayList<AchievementHistoryEntry> getHistory() {
        return this.m_history;
    }
    
    public Achievement getAchievement(final int achievementId) {
        return this.m_achievements.get(achievementId);
    }
    
    public TIntObjectIterator<Achievement> getAchievementIterator() {
        return this.m_achievements.iterator();
    }
    
    private void consistencyCheck() {
        this.m_achievements.forEachEntry(new TIntObjectProcedure<Achievement>() {
            @Override
            public boolean execute(final int achievementId, final Achievement achievement) {
                if (achievement.isComplete()) {
                    final ArrayList<Objective> objectives = achievement.getObjectives();
                    for (int k = 0, sz = objectives.size(); k < sz; ++k) {
                        final Objective objective = objectives.get(k);
                        objective.setCompleted(true);
                    }
                }
                return true;
            }
        });
    }
    
    public void unserialize(final byte[] data) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        ClientAchievementsContext.m_logger.info((Object)("D\u00e9serialisation des achievements : size=" + data.length));
        this.m_version = buffer.getInt();
        for (int historyLength = buffer.get() & 0xFF, i = 0; i < historyLength; ++i) {
            final int achievementId = buffer.getInt();
            final long unlockTime = buffer.getLong();
            this.m_history.add(new AchievementHistoryEntry(achievementId, unlockTime));
        }
        for (int nbVariables = buffer.getInt(), j = 0; j < nbVariables; ++j) {
            final int id = buffer.getInt();
            final long value = buffer.getLong();
            final Variable variable = this.m_variables.get(id);
            if (variable != null) {
                variable.setValue(value);
            }
        }
        for (int nbAchievements = buffer.getInt(), k = 0; k < nbAchievements; ++k) {
            final int id2 = buffer.getInt();
            final boolean active = buffer.get() == 1;
            final boolean complete = buffer.get() == 1;
            final long lastCompleted = (this.m_version >= 1) ? buffer.getLong() : 0L;
            final long startTime = (this.m_version >= 2) ? buffer.getLong() : 0L;
            final Achievement achievement = this.m_achievements.get(id2);
            if (achievement != null) {
                achievement.setActive(active);
                achievement.setCompleted(complete);
                achievement.setLastCompletedTime(lastCompleted);
                achievement.setStartTime(startTime);
            }
            else {
                ClientAchievementsContext.m_logger.error((Object)("Achievement inexistant : Id=" + id2));
            }
        }
        for (int nbObjectives = buffer.getInt(), l = 0; l < nbObjectives; ++l) {
            final int id3 = buffer.getInt();
            final Objective objective = this.m_objectives.get(id3);
            if (objective != null) {
                objective.setCompleted(true);
            }
            else {
                ClientAchievementsContext.m_logger.error((Object)("Objectif inexistant : Id=" + id3));
            }
        }
        for (int numFollowed = buffer.get() & 0xFF, m = 0; m < numFollowed; ++m) {
            this.m_followedAchievements.add(buffer.getInt());
        }
        this.consistencyCheck();
        if (this.m_listener != null) {
            this.m_listener.onInitialize(this);
        }
    }
    
    public void tryToActivate(final int id) {
        final long now = System.currentTimeMillis();
        final long lastActivation = this.m_lastActivationTry.get(id);
        if (lastActivation == 0L || lastActivation + 30000L > now) {
            this.m_lastActivationTry.put(id, now);
            this.m_listener.onTryToActivate(id);
        }
    }
    
    @Override
    public boolean canResetAchievement(final int id) {
        final Achievement achievement = this.m_achievements.get(id);
        return achievement != null && AchievementHelper.canResetAchievement(achievement, BaseGameDateProvider.INSTANCE.getDate());
    }
    
    public void reset(final int achievementId) {
        final Achievement achievement = this.m_achievements.get(achievementId);
        achievement.reset();
        final ArrayList<Objective> objectives = achievement.getObjectives();
        for (int i = 0; i < objectives.size(); ++i) {
            final Objective objective = objectives.get(i);
            this.m_objectives.put(objective.getId(), objective);
            final Objective objectiveDefinition = AchievementsModel.INSTANCE.getObjective(objective.getId());
            final Variable[] relatedVariables = objectiveDefinition.getRelatedVariables();
            for (int j = 0; relatedVariables != null && j < relatedVariables.length; ++j) {
                this.updateVariable(relatedVariables[j].getId(), 0L);
            }
        }
        if (this.m_listener != null) {
            this.m_listener.onAchievementReset(achievement);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientAchievementsContext.class);
    }
}
