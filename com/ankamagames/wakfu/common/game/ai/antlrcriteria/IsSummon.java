package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsSummon extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsSummon.signatures;
    }
    
    public IsSummon(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        return (targetCharacter != null && targetCharacter.isSummoned()) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_SUMMON;
    }
    
    static {
        (IsSummon.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING });
    }
}
