package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effect.targeting.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class GetTargetCountInBeaconArea extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    public GetTargetCountInBeaconArea(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTargetCountInBeaconArea.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null) {
            return 0L;
        }
        final AbstractEffectArea beacon = this.getArea(criterionTarget, criterionContext);
        if (beacon == null) {
            return 0L;
        }
        final GrowingArray<WakfuEffect> effects = ((BasicEffectArea<WakfuEffect, P>)beacon).getEffects();
        final Iterator<WakfuEffect> it = effects.iterator();
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        final EffectTargetsComputer targetsComputer = new EffectTargetsComputer();
        final Collection<EffectUser> allTargets = new HashSet<EffectUser>();
        while (it.hasNext()) {
            final WakfuEffect effect = it.next();
            final List<List<EffectUser>> targets = targetsComputer.determineTargets(effect, beacon, fight.getContext(), beacon.getWorldCellX(), beacon.getWorldCellY(), beacon.getWorldCellAltitude());
            if (targets.isEmpty()) {
                continue;
            }
            for (int i = 0, n = targets.size(); i < n; ++i) {
                final List<EffectUser> effectUsers = targets.get(i);
                if (!effectUsers.isEmpty()) {
                    for (int j = 0, m = effectUsers.size(); j < m; ++j) {
                        final EffectUser effectUser = effectUsers.get(j);
                        allTargets.add(effectUser);
                    }
                }
            }
        }
        return allTargets.size();
    }
    
    private AbstractEffectArea getArea(final Object criterionTarget, final Object criterionContext) {
        Object area = criterionTarget;
        if (criterionTarget instanceof EffectAreaProvider) {
            area = ((EffectAreaProvider)criterionTarget).getEffectArea();
        }
        AbstractBeaconEffectArea beacon = null;
        if (area instanceof AbstractBeaconEffectArea) {
            beacon = (AbstractBeaconEffectArea)criterionTarget;
        }
        else if (criterionTarget instanceof Point3) {
            final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
            final Point3 target = (Point3)criterionTarget;
            final FightObstacle obstacle = fight.getFightMap().getObstacle(target.getX(), target.getY());
            if (obstacle instanceof AbstractBeaconEffectArea) {
                beacon = (AbstractBeaconEffectArea)obstacle;
            }
        }
        return beacon;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TARGET_COUNT_IN_BEACON_AREA;
    }
    
    static {
        (GetTargetCountInBeaconArea.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
