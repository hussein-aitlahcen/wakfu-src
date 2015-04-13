package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsOnSpecificEffectAreaWithSpecificState extends IsOnSpecificEffectArea
{
    private static ArrayList<ParserType[]> signatures;
    private final NumericalValue m_stateId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOnSpecificEffectAreaWithSpecificState.signatures;
    }
    
    public IsOnSpecificEffectAreaWithSpecificState(final ArrayList<ParserObject> args) {
        super(args.get(0).getValue().equalsIgnoreCase("target"), args.get(1), args.get(2));
        this.checkType(args);
        this.m_stateId = args.get(3);
    }
    
    @Override
    protected boolean isValidForSpecificCriteria(final BasicEffectArea area, final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (area == null) {
            return false;
        }
        if (!(area instanceof CriterionUser)) {
            return false;
        }
        final long stateId = this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        return ((CriterionUser)area).hasState(stateId);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ON_SPECIFIC_EFFECT_AREA_WITH_SPECIFIC_STATE;
    }
    
    static {
        IsOnSpecificEffectAreaWithSpecificState.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING, ParserType.NUMBER, ParserType.BOOLEAN, ParserType.NUMBER };
        IsOnSpecificEffectAreaWithSpecificState.signatures.add(sig);
    }
}
