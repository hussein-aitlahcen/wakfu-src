package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public class GetPosition extends PositionFunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetPosition.SIGNATURES;
    }
    
    public GetPosition(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicCharacterInfo user;
        if (criterionUser instanceof BasicCharacterInfo) {
            user = (BasicCharacterInfo)criterionUser;
        }
        else if (criterionTarget instanceof BasicCharacterInfo) {
            user = (BasicCharacterInfo)criterionTarget;
        }
        else if (criterionContent instanceof PlayerTriggered) {
            user = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        else {
            user = CriteriaUtils.getTargetCharacterInfoFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        }
        if (user == null) {
            throw new CriteriaExecutionException("On cherche la position dans un contexte sans Personnage");
        }
        return super.getSign(criterionUser, criterionTarget, criterionContent, criterionContext) * PositionValue.toLong(user.getPosition());
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return Double.longBitsToDouble(PositionValue.toLong(((BasicCharacterInfo)criterionUser).getPosition()));
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETPOSITION;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
