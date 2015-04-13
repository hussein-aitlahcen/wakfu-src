package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsControlledByAI extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsControlledByAI.signatures;
    }
    
    public IsControlledByAI(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        return (targetCharacter != null && targetCharacter.isControlledByAI()) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_CONTROLLED_BY_AI;
    }
    
    static {
        IsControlledByAI.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING };
        IsControlledByAI.signatures.add(sig);
    }
}
