package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class HasEffectWithSpecificId extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private final boolean m_useTarget;
    private final long m_effectSpecificId;
    
    public HasEffectWithSpecificId(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_effectSpecificId = args.get(1).getLongValue(null, null, null, null);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasEffectWithSpecificId.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1;
        }
        final RunningEffectManager rem = target.getRunningEffectManager();
        if (rem == null) {
            return -1;
        }
        final boolean remContainsEffect = rem.containsWithSpecificId((int)this.m_effectSpecificId);
        if (remContainsEffect) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_EFFECT_WITH_SPECIFIC_ID;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBER });
    }
}
