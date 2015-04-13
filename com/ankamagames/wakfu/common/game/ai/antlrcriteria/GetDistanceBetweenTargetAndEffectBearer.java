package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class GetDistanceBetweenTargetAndEffectBearer extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetDistanceBetweenTargetAndEffectBearer.signatures;
    }
    
    public GetDistanceBetweenTargetAndEffectBearer(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionContent instanceof WakfuRunningEffect)) {
            return -1L;
        }
        final RunningEffectManager effectManager = ((WakfuRunningEffect)criterionContent).getManagerWhereIamStored();
        if (effectManager == null) {
            GetDistanceBetweenTargetAndEffectBearer.m_logger.error((Object)"On ne peut pas calculer la distance a partir du porteur d'un effet, l'effet n'est pas stock\u00e9 dans un manager ");
            return -1L;
        }
        final EffectUser effectBearer = effectManager.getOwner();
        if (effectBearer == null) {
            return -1L;
        }
        if (criterionTarget instanceof PhysicalRadiusOwner) {
            final PhysicalRadiusOwner target = (PhysicalRadiusOwner)criterionTarget;
            return DistanceUtils.getIntersectionDistance(effectBearer, target);
        }
        if (criterionTarget instanceof Point3) {
            final Point3 target2 = (Point3)criterionTarget;
            return DistanceUtils.getIntersectionDistance(effectBearer, target2);
        }
        return -1L;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER;
    }
    
    static {
        (GetDistanceBetweenTargetAndEffectBearer.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
