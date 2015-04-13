package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.specific.*;

public class CanCarryTarget extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return CanCarryTarget.signatures;
    }
    
    public CanCarryTarget(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null || !(criterionTarget instanceof CarryTarget)) {
            return -1;
        }
        if (criterionUser == null || !(criterionUser instanceof Carrier)) {
            return -1;
        }
        final Carrier carrier = (Carrier)criterionUser;
        final CarryTarget carryTarget = (CarryTarget)criterionTarget;
        if (carrier.canCarry(carryTarget) && carryTarget.canBeCarriedBy(carrier)) {
            return 0;
        }
        return -2;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.CANCARRYTARGET;
    }
    
    static {
        CanCarryTarget.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        CanCarryTarget.signatures.add(sig);
    }
}
