package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetIEPosition extends PositionFunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetIEPosition.signatures;
    }
    
    public GetIEPosition(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        MapInteractiveElement ie;
        if (criterionUser instanceof MapInteractiveElement) {
            ie = (MapInteractiveElement)criterionUser;
        }
        else if (criterionTarget instanceof MapInteractiveElement) {
            ie = (MapInteractiveElement)criterionTarget;
        }
        else {
            if (!(criterionContent instanceof TargetedEvent) || !(((TargetedEvent)criterionContent).getTarget() instanceof MapInteractiveElement)) {
                throw new CriteriaExecutionException("On cherche la position dans un contexte sans ie");
            }
            ie = (MapInteractiveElement)((TargetedEvent)criterionContent).getTarget();
        }
        return super.getSign(criterionUser, criterionTarget, criterionContent, criterionContext) * PositionValue.toLong(ie.getPosition());
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return Double.longBitsToDouble(PositionValue.toLong(((BasicCharacterInfo)criterionUser).getPosition()));
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETIEPOSITION;
    }
    
    static {
        (GetIEPosition.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
