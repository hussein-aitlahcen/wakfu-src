package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;

public class IsAchievementObjectiveComplete extends FunctionCriterion
{
    private int m_objectiveId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsAchievementObjectiveComplete.signatures;
    }
    
    public IsAchievementObjectiveComplete() {
        super();
    }
    
    public IsAchievementObjectiveComplete(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_objectiveId = (int)args.get(0).getLongValue(null, null, null, null);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        AchievementsContext achievementContext = null;
        if (criterionContext instanceof AchievementsContext) {
            achievementContext = (AchievementsContext)criterionContext;
        }
        else if (criterionUser instanceof AchievementsContextProvider) {
            achievementContext = ((AchievementsContextProvider)criterionUser).getAchievementsContext();
        }
        if (achievementContext == null) {
            throw new CriteriaExecutionException("Utilisation du crit\u00e8re IsAchievementObjectiveComplete hors d'un contexte d'achievement");
        }
        if (achievementContext.hasObjective(this.m_objectiveId)) {
            return achievementContext.isObjectiveCompleted(this.m_objectiveId) ? 0 : -1;
        }
        final ObjectiveDefinition objective = achievementContext.getObjective(this.m_objectiveId);
        if (objective == null) {
            throw new CriteriaExecutionException("R\u00e9cup\u00e9ration d'un objectif d'achievement inconnu id=" + this.m_objectiveId);
        }
        final int achievementId = objective.getParentAchievement().getId();
        if (achievementContext.hasAchievement(achievementId)) {
            return achievementContext.isAchievementComplete(achievementId) ? 0 : -1;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ACHIEVEMENT_OBJECTIVE_COMPLETE;
    }
    
    static {
        IsAchievementObjectiveComplete.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsAchievementObjectiveComplete.signatures.add(sig);
    }
}
