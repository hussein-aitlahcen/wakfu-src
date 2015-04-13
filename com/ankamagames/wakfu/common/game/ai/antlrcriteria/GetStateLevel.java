package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetStateLevel extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_useTarget;
    private long m_stateId;
    
    public GetStateLevel(final ArrayList<ParserObject> args) {
        super();
        this.m_useTarget = false;
        final byte type = this.checkType(args);
        this.m_stateId = args.get(0).getLongValue(null, null, null, null);
        if (type == 1) {
            this.m_useTarget = args.get(1).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetStateLevel.SIGNATURES;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1L;
        }
        return target.getStateLevel(this.m_stateId);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_STATE_LEVEL;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_NUMBER_SIGNATURE);
        GetStateLevel.SIGNATURES.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING });
    }
}
