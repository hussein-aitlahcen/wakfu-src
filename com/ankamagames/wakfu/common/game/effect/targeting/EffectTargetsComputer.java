package com.ankamagames.wakfu.common.game.effect.targeting;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.ai.targetfinder.*;

public final class EffectTargetsComputer
{
    private static final Logger m_logger;
    private final WakfuRunningEffect m_effect;
    
    public EffectTargetsComputer() {
        super();
        this.m_effect = null;
    }
    
    public EffectTargetsComputer(final WakfuRunningEffect effect) {
        super();
        this.m_effect = effect;
    }
    
    public List<List<EffectUser>> determineTargets(final WakfuEffect genericEffect, final Target applicant, final EffectContext<WakfuEffect> context, final int targetCellx, final int targetCelly, final short targetCellz) {
        if (context == null) {
            EffectTargetsComputer.m_logger.error((Object)"On ne peut pas determiner les cibles sans contexte");
            return Collections.emptyList();
        }
        final HashMap<Long, EffectUser> targets = new HashMap<Long, EffectUser>();
        final List<EffectUser> allTargets = new ArrayList<EffectUser>();
        for (final AreaOfEffect aoe : genericEffect.getAreaOfEffect().getSubAOEs()) {
            final Iterable<EffectUser> targetsForCurrentAOE = TargetFinder.getInstance().getTargets(applicant, context.getTargetInformationProvider(), aoe, targetCellx, targetCelly, targetCellz, genericEffect.getTargetValidator());
            for (final EffectUser eu : targetsForCurrentAOE) {
                if (!this.validateCarriedTarget(genericEffect, eu, applicant)) {
                    continue;
                }
                if (this.targetAllreadyFound(targets, eu)) {
                    continue;
                }
                targets.put(eu.getId(), eu);
                allTargets.add(eu);
            }
        }
        this.sortTargets(allTargets, genericEffect, targetCellx, targetCelly, targetCellz);
        final byte maxTargets = genericEffect.getMaxTargetsCount();
        final List<EffectUser> finalTargetList = this.removeExceededTargets(allTargets, maxTargets);
        final ArrayList<List<EffectUser>> res = new ArrayList<List<EffectUser>>();
        res.add(finalTargetList);
        return res;
    }
    
    private void sortTargets(final List<EffectUser> targets, final WakfuEffect effect, final int x, final int y, final short z) {
        final Comparator<EffectUser> comparator = TargetComparatorProvider.getComparator(effect, x, y, z);
        if (comparator == null) {
            return;
        }
        Collections.sort(targets, comparator);
    }
    
    private boolean targetAllreadyFound(final HashMap<Long, EffectUser> targets, final EffectUser eu) {
        return targets.containsKey(eu.getId());
    }
    
    private boolean validateCarriedTarget(final WakfuEffect genericEffect, final EffectUser eu, final Target applicant) {
        if (!(eu instanceof CarryTarget)) {
            return true;
        }
        if (applicant == eu) {
            return true;
        }
        final TargetValidator<EffectUser> targetValidator = genericEffect.getTargetValidator();
        final boolean acceptCarried = targetValidator != null && targetValidator.isConditionActive(8192L);
        final CarryTarget carried = (CarryTarget)eu;
        return acceptCarried || !carried.isCarried();
    }
    
    private List<EffectUser> removeExceededTargets(final List<EffectUser> allTargets, final byte maxTargets) {
        if (maxTargets <= 0) {
            return allTargets;
        }
        if (allTargets.size() > maxTargets) {
            return allTargets.subList(0, maxTargets);
        }
        return allTargets;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectTargetsComputer.class);
    }
}
