package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;

public class GetEffectCaster extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetEffectCaster.SIGNATURES;
    }
    
    public GetEffectCaster(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof Target)) {
            GetEffectCaster.m_logger.error((Object)("Le crit\u00e8re d'effet est employ\u00e9 pour autre chose qu'un targetFinder.Target : " + criterionUser + " - " + criterionTarget + " - " + criterionContent + " - " + criterionContext));
            return 0L;
        }
        if (criterionUser == null) {
            return 0L;
        }
        return ((Target)criterionUser).getId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETEFFECTCASTER;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
