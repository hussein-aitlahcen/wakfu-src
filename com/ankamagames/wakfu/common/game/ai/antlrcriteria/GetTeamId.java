package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetTeamId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private String m_targetType;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTeamId.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetTeamId(final ArrayList<ParserObject> args) {
        super();
        if (this.checkType(args) == 1) {
            this.m_targetType = args.get(0).getValue();
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1L;
        }
        return this.getSign() * targetCharacter.getTeamId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TEAM_ID;
    }
    
    static {
        (GetTeamId.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        GetTeamId.signatures.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
