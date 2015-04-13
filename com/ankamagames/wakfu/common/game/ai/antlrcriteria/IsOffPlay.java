package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class IsOffPlay extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOffPlay.signatures;
    }
    
    public IsOffPlay(final ArrayList<ParserObject> args) {
        super();
        final byte type = this.checkType(args);
        if (type == 1) {
            this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        return (targetCharacter != null && targetCharacter.isOffPlay()) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_OFF_PLAY;
    }
    
    static {
        (IsOffPlay.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        IsOffPlay.signatures.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
