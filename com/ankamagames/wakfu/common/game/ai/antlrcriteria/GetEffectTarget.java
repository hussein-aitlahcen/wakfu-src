package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class GetEffectTarget extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetEffectTarget.SIGNATURES;
    }
    
    public GetEffectTarget(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return 0L;
        }
        if (!(target instanceof Target)) {
            GetEffectTarget.m_logger.error((Object)("Le crit\u00e8re d'effet est employ\u00e9 pour autre chose qu'un targetFinder.Target : " + criterionUser + " - " + criterionTarget + " - " + criterionContent + " - " + criterionContext));
            return 0L;
        }
        return target.getId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETEFFECTTARGET;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
