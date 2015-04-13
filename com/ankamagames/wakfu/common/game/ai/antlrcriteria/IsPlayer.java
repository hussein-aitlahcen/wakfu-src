package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsPlayer extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private String m_targetType;
    
    public IsPlayer(final ArrayList<ParserObject> args) {
        super();
        final byte sigIndex = this.checkType(args);
        this.m_targetType = "target";
        if (sigIndex == 1) {
            this.m_targetType = args.get(0).getValue();
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsPlayer.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser user = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            return -1;
        }
        return user.is(CriterionUserType.PLAYER_CHARACTER) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_PLAYER;
    }
    
    static {
        (IsPlayer.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        IsPlayer.signatures.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
