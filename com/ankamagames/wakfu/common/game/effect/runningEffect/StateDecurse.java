package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class StateDecurse extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected int m_levelToDecrease;
    private boolean m_decreaseLevel;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return StateDecurse.PARAMETERS_LIST_SET;
    }
    
    public StateDecurse() {
        super();
        this.setTriggersToExecute();
    }
    
    public static StateDecurse checkOut(final EffectContext<WakfuEffect> context, final int stateId, final EffectUser target) {
        assert target != null : "No target defined for StateDecurse effect";
        StateDecurse re;
        try {
            re = (StateDecurse)StateDecurse.m_staticPool.borrowObject();
            re.m_pool = StateDecurse.m_staticPool;
        }
        catch (Exception e) {
            re = new StateDecurse();
            re.m_isStatic = false;
            re.m_pool = null;
            StateDecurse.m_logger.error((Object)("Erreur lors d'un checkOut sur un ActionCost : " + e.getMessage()));
        }
        re.m_id = RunningEffectConstants.STATE_FORCE_UNAPPLY.getId();
        re.m_status = RunningEffectConstants.STATE_FORCE_UNAPPLY.getObject().getRunningEffectStatus();
        re.setTriggersToExecute();
        re.m_target = target;
        re.m_value = stateId;
        re.m_maxExecutionCount = -1;
        re.m_context = (EffectContext<FX>)context;
        return re;
    }
    
    @Override
    public StateDecurse newInstance() {
        StateDecurse re;
        try {
            re = (StateDecurse)StateDecurse.m_staticPool.borrowObject();
            re.m_pool = StateDecurse.m_staticPool;
        }
        catch (Exception e) {
            re = new StateDecurse();
            re.m_pool = null;
            re.m_isStatic = false;
            StateDecurse.m_logger.error((Object)("Erreur lors d'un checkOut sur un StateDecurse : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2225);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final ArrayList<RunningEffect> effectToRemove = new ArrayList<RunningEffect>();
        if (this.m_target == null) {
            StateDecurse.m_logger.warn((Object)"[Effect] Impossible d'appliquer un desenvoutement car la cible est null");
            this.setNotified(true);
            return;
        }
        final RunningEffectManager effectManager = this.m_target.getRunningEffectManager();
        if (effectManager == null) {
            return;
        }
        int newStateLevel = 0;
        newStateLevel = this.computeEffectsToRemove(effectToRemove, effectManager, newStateLevel);
        if (effectToRemove.isEmpty()) {
            this.setNotified();
            return;
        }
        for (final RunningEffect anEffectToRemove : effectToRemove) {
            ((TimedRunningEffectManager)effectManager).removeEffect(anEffectToRemove, true);
        }
        this.notifyExecution(linkedRE, trigger);
        if (this.isValueComputationEnabled() && this.m_decreaseLevel && newStateLevel > 0) {
            this.applyNewState(newStateLevel, linkedRE);
        }
    }
    
    protected int computeEffectsToRemove(final ArrayList<RunningEffect> effectToRemove, final RunningEffectManager effectManager, int newStateLevel) {
        for (final RunningEffect effect : effectManager) {
            if (!(effect instanceof StateRunningEffect)) {
                continue;
            }
            final State state = ((StateRunningEffect)effect).getState();
            if (state.getStateBaseId() != this.m_value || (state.isInamovable() && !ApplyState.UNDECURSABLE_STATE.contains(state.getStateBaseId()))) {
                continue;
            }
            effectToRemove.add(effect);
            newStateLevel = state.getLevel() - this.m_levelToDecrease;
        }
        return newStateLevel;
    }
    
    private void applyNewState(final int newStateLevel, final RunningEffect linkedRE) {
        final int stateUniqueId = State.getUniqueIdFromBasicInformation((short)this.m_value, (short)newStateLevel);
        final StateRunningEffect stateRunningEffect = StateRunningEffect.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_caster, (WakfuEffectContainer)this.m_effectContainer, stateUniqueId);
        if (stateRunningEffect == null) {
            StateDecurse.m_logger.error((Object)("Etat inconnu id " + this.m_value + ", level " + newStateLevel + ", effet correspondant : " + ((this.m_genericEffect != null) ? ((WakfuEffect)this.m_genericEffect).getEffectId() : "null")));
            return;
        }
        final WakfuEffectExecutionParameters parameters = WakfuEffectExecutionParameters.checkOut(true, false, null);
        stateRunningEffect.setExecutionParameters(parameters);
        ((RunningEffect<FX, WakfuEffectContainer>)stateRunningEffect).setEffectContainer((WakfuEffectContainer)this.m_effectContainer);
        ((RunningEffect<WakfuEffect, EC>)stateRunningEffect).setGenericEffect(this.getStateRunningGenericEffect());
        stateRunningEffect.addEndTriggers(this.getListeningTriggerForUnapplication());
        stateRunningEffect.setParent(linkedRE);
        stateRunningEffect.applyOnTargets(this.m_target);
        stateRunningEffect.release();
    }
    
    private WakfuEffect getStateRunningGenericEffect() {
        if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect() == null) {
            return DefaultEffect.getInstance();
        }
        if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().isUsableInWorld()) {
            if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().isUsableInFight()) {
                return DefaultWorldAndFightUsableEffect.getInstance();
            }
            return DefaultWorldUsableEffect.getInstance();
        }
        else {
            if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().isUsableInFight()) {
                return DefaultFightUsableEffect.getInstanceWithoutMaxLevel();
            }
            return DefaultEffect.getInstance();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            this.m_levelToDecrease = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_decreaseLevel = true;
        }
    }
    
    public void setLevelToDecrease(final int levelToDecrease) {
        this.m_levelToDecrease = levelToDecrease;
        if (levelToDecrease > 0) {
            this.m_decreaseLevel = true;
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_decreaseLevel = false;
        this.m_levelToDecrease = 0;
        super.onCheckOut();
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<StateDecurse>() {
            @Override
            public StateDecurse makeObject() {
                return new StateDecurse();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Decurse d'etat", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de l'\u00e9tat \u00e0 retirer", WakfuRunningEffectParameterType.ID) }), new WakfuRunningEffectParameterList("Decurse d'etat", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de l'\u00e9tat \u00e0 retirer", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("Niveau de l'etat a retirer", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
