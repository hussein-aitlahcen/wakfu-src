package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class HasState extends FunctionCriterion implements Targetable
{
    private static ArrayList<ParserType[]> signatures;
    protected NumericalValue m_stateId;
    protected NumericalValue m_stateLevel;
    protected String m_targetType;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasState.signatures;
    }
    
    public HasState(final ArrayList<ParserObject> args) {
        super();
        this.m_stateLevel = null;
        final byte type = this.checkType(args);
        this.m_stateId = args.get(0);
        if (type == 1 || type == 3) {
            this.m_stateLevel = args.get(1);
        }
        if (type == 2) {
            this.m_targetType = args.get(1).getValue();
        }
        if (type == 3) {
            this.m_targetType = args.get(2).getValue();
        }
    }
    
    public int getStateId() {
        if (this.m_stateId.isConstant() && this.m_stateId.isInteger()) {
            return (int)this.m_stateId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        CriterionUser targetUser = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetUser == null && criterionContent instanceof PlayerTriggered) {
            targetUser = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        if (targetUser == null) {
            return -1;
        }
        boolean hasState;
        if (this.m_stateLevel == null) {
            hasState = targetUser.hasState(this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        }
        else {
            hasState = targetUser.hasState(this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext), this.m_stateLevel.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        }
        if (hasState) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_STATE;
    }
    
    @Override
    public boolean isTarget() {
        return this.m_targetType != null && this.m_targetType.equalsIgnoreCase("target");
    }
    
    public int getStateLevel() {
        if (this.m_stateLevel != null && this.m_stateLevel.isConstant() && this.m_stateLevel.isInteger()) {
            return (int)this.m_stateLevel.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    static {
        (HasState.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER });
        HasState.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.NUMBER });
        HasState.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING });
        HasState.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.NUMBER, ParserType.STRING });
    }
}
