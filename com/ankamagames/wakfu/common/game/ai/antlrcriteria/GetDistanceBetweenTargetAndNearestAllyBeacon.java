package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.*;
import java.util.*;

public class GetDistanceBetweenTargetAndNearestAllyBeacon extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetDistanceBetweenTargetAndNearestAllyBeacon.SIGNATURES;
    }
    
    public GetDistanceBetweenTargetAndNearestAllyBeacon(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null || !(criterionUser instanceof CriterionUser)) {
            return -1L;
        }
        final boolean isFight = criterionContext instanceof AbstractFight;
        final boolean isFightContext = criterionContext instanceof WakfuFightEffectContext;
        if (!isFight && !isFightContext) {
            throw new CriteriaExecutionException("On essaie de compter r\u00e9cup\u00e9rer la distance avec une balise en dehors d'un combat...");
        }
        AbstractFight<?> fight;
        if (isFight) {
            fight = (AbstractFight<?>)criterionContext;
        }
        else {
            fight = ((WakfuFightEffectContext)criterionContext).getFight();
        }
        final CriterionUser user = (CriterionUser)criterionUser;
        final byte userTeamId = user.getTeamId();
        Point3 targetCell;
        if (criterionTarget instanceof PhysicalRadiusOwner) {
            final PhysicalRadiusOwner target = (PhysicalRadiusOwner)criterionTarget;
            targetCell = new Point3(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
        }
        else {
            if (!(criterionTarget instanceof Point3)) {
                return -1L;
            }
            targetCell = (Point3)criterionTarget;
        }
        final Collection<? extends BasicEffectArea> areas = fight.getActiveEffectAreas();
        int minDistance = 32767;
        for (final BasicEffectArea area : areas) {
            if (area.getType() != EffectAreaType.BEACON.getTypeId()) {
                continue;
            }
            if (area.getTeamId() != userTeamId) {
                continue;
            }
            final int distance = DistanceUtils.getIntersectionDistance(area, targetCell);
            if (distance >= minDistance) {
                continue;
            }
            minDistance = distance;
        }
        return minDistance;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
