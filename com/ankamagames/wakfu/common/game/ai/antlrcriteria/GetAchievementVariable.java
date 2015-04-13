package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;

public class GetAchievementVariable extends FunctionValue
{
    private String m_variableName;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetAchievementVariable.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetAchievementVariable() {
        super();
    }
    
    public GetAchievementVariable(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_variableName = args.get(0).getValue();
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        AchievementsContext achievementContext = null;
        if (criterionContext instanceof AchievementsContext) {
            achievementContext = (AchievementsContext)criterionContext;
        }
        else if (criterionUser instanceof AchievementsContextProvider) {
            achievementContext = ((AchievementsContextProvider)criterionUser).getAchievementsContext();
        }
        if (achievementContext == null) {
            throw new CriteriaExecutionException("Utilisation du crit\u00e8re GetAchievementVariable hors d'un contexte d'achievement");
        }
        final AchievementVariable variable = achievementContext.getVariableByName(this.m_variableName);
        if (variable == null) {
            throw new CriteriaExecutionException("R\u00e9cup\u00e9ration d'une variable d'achievement de nom inconnu name=" + this.m_variableName);
        }
        return super.getSign() * achievementContext.getVariableValue(variable.getId());
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_ACHIEVEMENT_VARIABLE;
    }
    
    static {
        GetAchievementVariable.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING };
        GetAchievementVariable.signatures.add(sig);
    }
}
