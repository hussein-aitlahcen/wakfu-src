package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetFighterId extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private String m_targetType;
    
    public GetFighterId(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_targetType = args.get(0).getValue();
    }
    
    public GetFighterId(final String targetType) {
        super();
        this.m_targetType = targetType;
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetFighterId.SIGNATURES;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_FIGHTER_ID;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1L;
        }
        return target.getId();
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
