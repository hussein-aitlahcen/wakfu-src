package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;

public class IsAchievementFailed extends FunctionCriterion
{
    private int m_achievementId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsAchievementFailed.signatures;
    }
    
    public IsAchievementFailed() {
        super();
    }
    
    public IsAchievementFailed(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_achievementId = (int)args.get(0).getLongValue(null, null, null, null);
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
            return -1;
        }
        if (achievementContext.hasAchievement(this.m_achievementId)) {
            return achievementContext.isAchievementFailed(this.m_achievementId) ? 0 : -1;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ACHIEVEMENT_FAILED;
    }
    
    static {
        IsAchievementFailed.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsAchievementFailed.signatures.add(sig);
    }
}
