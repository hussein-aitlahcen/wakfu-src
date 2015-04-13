package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

abstract class UsingEffectGroupRunningEffect extends WakfuRunningEffect
{
    private int m_effectsExecuted;
    private int m_effectsWithoutExecutions;
    private int m_executionsCount;
    
    protected int getEffectsExecuted() {
        return this.m_effectsExecuted;
    }
    
    protected int getEffectsWithoutExecutions() {
        return this.m_effectsWithoutExecutions;
    }
    
    protected int getExecutionsCount() {
        return this.m_executionsCount;
    }
    
    protected abstract boolean isProbabilityComputationDisabled();
    
    protected void executeEffectGroup(final WakfuRunningEffect triggerRE) {
        this.m_effectsExecuted = 0;
        this.m_effectsWithoutExecutions = 0;
        this.m_executionsCount = 0;
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup == null) {
            UsingEffectGroupRunningEffect.m_logger.error((Object)("Groupe d'effet inconnu" + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            this.setNotified();
            return;
        }
        final WakfuEffectExecutionParameters params = this.getExecutionParameters(triggerRE, this.isProbabilityComputationDisabled());
        if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
            params.setForcedLevel(this.getContainerLevel());
        }
        for (final WakfuEffect effect : effectGroup) {
            EffectExecutionResult result = null;
            if (this.useTarget()) {
                result = effect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_target, params, true);
            }
            else if (this.useTargetCell()) {
                result = effect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), null, params, true);
            }
            else {
                UsingEffectGroupRunningEffect.m_logger.error((Object)("Pas de type de cible d\u00e9fini, on n'execute pas le groupe d'effet " + this.getEffectId()));
            }
            if (result != null) {
                ++this.m_effectsExecuted;
                this.m_executionsCount += result.getExecutionCount();
                if (result.getExecutionCount() == 0) {
                    ++this.m_effectsWithoutExecutions;
                }
            }
            if (result != null) {
                result.clear();
            }
        }
        params.release();
    }
    
    protected void executeEffectGroup(final WakfuRunningEffect triggerRE, final EffectUser target) {
        this.m_effectsExecuted = 0;
        this.m_effectsWithoutExecutions = 0;
        this.m_executionsCount = 0;
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup == null) {
            UsingEffectGroupRunningEffect.m_logger.error((Object)("Groupe d'effet inconnu" + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            this.setNotified();
            return;
        }
        final WakfuEffectExecutionParameters params = this.getExecutionParameters(triggerRE, true);
        if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
            params.setForcedLevel(this.getContainerLevel());
        }
        for (final WakfuEffect effect : effectGroup) {
            final EffectExecutionResult result = effect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude(), target, params, false);
            if (result != null) {
                ++this.m_effectsExecuted;
                this.m_executionsCount += result.getExecutionCount();
                if (result.getExecutionCount() != 0) {
                    continue;
                }
                ++this.m_effectsWithoutExecutions;
            }
        }
        params.release();
    }
    
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setResetLimitedApplyCount(false);
        return params;
    }
}
