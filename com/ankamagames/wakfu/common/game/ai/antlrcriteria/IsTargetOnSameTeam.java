package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class IsTargetOnSameTeam extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsTargetOnSameTeam.signatures;
    }
    
    public boolean isInteger() {
        return true;
    }
    
    public IsTargetOnSameTeam(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        final BasicCharacterInfo casterCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (casterCharacter == null || targetCharacter == null) {
            return -1;
        }
        if (casterCharacter.getTeamId() == targetCharacter.getTeamId()) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_TARGET_ON_SAME_TEAM;
    }
    
    static {
        (IsTargetOnSameTeam.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
