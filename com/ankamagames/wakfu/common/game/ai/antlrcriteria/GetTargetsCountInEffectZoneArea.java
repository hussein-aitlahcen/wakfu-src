package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effect.targeting.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class GetTargetsCountInEffectZoneArea extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_zoneId;
    
    public GetTargetsCountInEffectZoneArea(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_zoneId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTargetsCountInEffectZoneArea.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null || !(criterionTarget instanceof Point3)) {
            return 0L;
        }
        final AbstractEffectArea area = this.getArea(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (area == null) {
            return 0L;
        }
        final Point3 targetCell = (Point3)criterionTarget;
        final GrowingArray<WakfuEffect> effects = ((BasicEffectArea<WakfuEffect, P>)area).getEffects();
        final Iterator<WakfuEffect> it = effects.iterator();
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        final EffectTargetsComputer targetsComputer = new EffectTargetsComputer();
        final Set<EffectUser> allTargets = new HashSet<EffectUser>();
        while (it.hasNext()) {
            final WakfuEffect effect = it.next();
            final List<List<EffectUser>> targets = targetsComputer.determineTargets(effect, area, fight.getContext(), targetCell.getX(), targetCell.getY(), targetCell.getZ());
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
    
    private AbstractEffectArea getArea(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final long zoneId = this.m_zoneId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        return StaticEffectAreaManager.getInstance().getAreaFromId(zoneId);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA;
    }
    
    static {
        GetTargetsCountInEffectZoneArea.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        GetTargetsCountInEffectZoneArea.signatures.add(sig);
    }
}
