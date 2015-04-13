package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasEffectWithActionId extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_useTarget;
    private NumericalValue m_effectActionId;
    
    public HasEffectWithActionId(final ArrayList<ParserObject> args) {
        super();
        this.m_useTarget = false;
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_effectActionId = args.get(1);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasEffectWithActionId.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo target = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        final int effectActionId = (int)this.m_effectActionId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        return target.hasEffectWithActionId(effectActionId) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_EFFECT_WITH_ACTION_ID;
    }
    
    static {
        (HasEffectWithActionId.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBER });
    }
}
