package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

final class RunningEffectUtils
{
    @Nullable
    public static Elements getCasterBestElement(final WakfuRunningEffect effect) {
        if (effect == null) {
            return null;
        }
        if (effect.getCaster() == null) {
            return null;
        }
        final Elements[] elements = Elements.values();
        Elements bestElement = null;
        int bestValue = 0;
        for (int i = 0; i < elements.length; ++i) {
            final Elements element = elements[i];
            final EffectUser caster = effect.getCaster();
            if (caster.hasCharacteristic(element.getDamageBonusCharacteristic()) && (bestElement == null || bestValue < caster.getCharacteristicValue(element.getDamageBonusCharacteristic()))) {
                bestElement = element;
                bestValue = caster.getCharacteristicValue(element.getDamageBonusCharacteristic());
            }
        }
        return bestElement;
    }
    
    @Nullable
    public static Elements getTargetWeakestResistElement(final WakfuRunningEffect effect) {
        if (effect == null) {
            return null;
        }
        if (effect.getTarget() == null) {
            return null;
        }
        final Elements[] elements = Elements.values();
        Elements worstElement = null;
        int worstValue = Integer.MAX_VALUE;
        final EffectUser target = effect.getTarget();
        for (int i = 0; i < elements.length; ++i) {
            final Elements element = elements[i];
            if (element != Elements.STASIS) {
                final FighterCharacteristicType resist = element.getResistanceBonusCharacteristic();
                if (target.hasCharacteristic(resist) && (worstElement == null || worstValue > target.getCharacteristicValue(resist))) {
                    worstElement = element;
                    worstValue = target.getCharacteristicValue(resist);
                }
            }
        }
        return worstElement;
    }
    
    public static int likePreviousLevelRound(final float value, final float inc) {
        if (inc >= 0.0f) {
            return (int)Math.floor(value);
        }
        return (int)Math.ceil(value);
    }
    
    public static void setTriggerForElement(final Elements element, final StaticRunningEffect hpLoss) {
        if (element == null) {
            return;
        }
        switch (element) {
            case EARTH: {
                hpLoss.getTriggersToExecute().set(8);
                break;
            }
            case FIRE: {
                hpLoss.getTriggersToExecute().set(5);
                break;
            }
            case WATER: {
                hpLoss.getTriggersToExecute().set(6);
                break;
            }
            case AIR: {
                hpLoss.getTriggersToExecute().set(7);
                break;
            }
            case STASIS: {
                hpLoss.getTriggersToExecute().set(9);
                break;
            }
        }
    }
    
    public static List<EffectUser> getTargets(final WakfuRunningEffect effect, final boolean checkCriterion) {
        final WakfuEffect genericEffect = ((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect();
        final Point3 targetCell = effect.getTargetCell();
        final List<List<EffectUser>> potentialTargets = effect.determineTargets(genericEffect, effect.getCaster(), effect.getContext(), targetCell.getX(), targetCell.getY(), targetCell.getZ());
        final SimpleCriterion conditions = genericEffect.getConditions();
        final List<EffectUser> targetList = new ArrayList<EffectUser>();
        for (int i = 0, n = potentialTargets.size(); i < n; ++i) {
            final List<EffectUser> effectUsers = potentialTargets.get(i);
            for (int j = 0, k = effectUsers.size(); j < k; ++j) {
                final EffectUser effectUser = effectUsers.get(j);
                if (!targetList.contains(effectUser)) {
                    if (!checkCriterion || conditions == null || conditions.isValid(effect.getCaster(), effectUser, effect, effect.getContext())) {
                        targetList.add(effectUser);
                    }
                }
            }
        }
        return targetList;
    }
}
