package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class GetDistanceBetweenCasterAndTarget extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetDistanceBetweenCasterAndTarget.SIGNATURES;
    }
    
    public GetDistanceBetweenCasterAndTarget(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser instanceof PhysicalRadiusOwner && criterionTarget instanceof PhysicalRadiusOwner) {
            final PhysicalRadiusOwner user = (PhysicalRadiusOwner)criterionUser;
            final PhysicalRadiusOwner target = (PhysicalRadiusOwner)criterionTarget;
            return DistanceUtils.getIntersectionDistance(user, target);
        }
        if (criterionUser instanceof PhysicalRadiusOwner && criterionTarget instanceof Point3) {
            final PhysicalRadiusOwner user = (PhysicalRadiusOwner)criterionUser;
            final Point3 target2 = (Point3)criterionTarget;
            return DistanceUtils.getIntersectionDistance(user, target2);
        }
        return -1L;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETDISTANCEBETWEENCASTERANDTARGET;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
