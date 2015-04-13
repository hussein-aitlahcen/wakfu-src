package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ApplyStateFunctionPaPmPw extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected short m_stateId;
    protected short m_stateLevelPerPA;
    protected short m_stateLevelPerPM;
    protected short m_stateLevelPerPW;
    protected boolean m_executeActionCost;
    
    public ApplyStateFunctionPaPmPw() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ApplyStateFunctionPaPmPw newInstance() {
        ApplyStateFunctionPaPmPw re;
        try {
            re = (ApplyStateFunctionPaPmPw)ApplyStateFunctionPaPmPw.m_staticPool.borrowObject();
            re.m_pool = ApplyStateFunctionPaPmPw.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyStateFunctionPaPmPw();
            re.m_isStatic = false;
            re.m_pool = null;
            ApplyStateFunctionPaPmPw.m_logger.error((Object)("Erreur lors d'un newInstance sur un LatentState : " + e.getMessage()));
        }
        re.m_stateId = this.m_stateId;
        re.m_stateLevelPerPA = this.m_stateLevelPerPA;
        re.m_stateLevelPerPM = this.m_stateLevelPerPM;
        re.m_stateLevelPerPW = this.m_stateLevelPerPW;
        re.m_executeActionCost = this.m_executeActionCost;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2226);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApplyStateFunctionPaPmPw.PARAMETERS_LIST_SET;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified(true);
        if (this.m_caster == null || this.m_target == null) {
            return;
        }
        final int remainingAP = (this.m_stateLevelPerPA == 0) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.AP);
        final int remainingMP = (this.m_stateLevelPerPM == 0) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.MP);
        final int remainingWP = (this.m_stateLevelPerPW == 0) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.WP);
        ActionCost actionCost;
        if (this.m_executeActionCost) {
            actionCost = ActionCost.checkOut((EffectContext<WakfuEffect>)this.m_context, new SpellCost((byte)remainingAP, (byte)remainingMP, (byte)remainingWP), this.m_caster);
        }
        else {
            actionCost = ActionCost.checkOut((EffectContext<WakfuEffect>)this.m_context, new SpellCost((byte)0, (byte)0, (byte)0), this.m_caster);
        }
        actionCost.setCaster(this.m_caster);
        actionCost.setRunningEffectStatus(RunningEffectStatus.NEUTRAL);
        actionCost.execute(null, false);
        final short stateLevel = (short)(remainingAP * this.m_stateLevelPerPA + remainingMP * this.m_stateLevelPerPM + remainingWP * this.m_stateLevelPerPW);
        final ApplyState applyState = ApplyState.checkout((EffectContext<WakfuEffect>)this.m_context, this.m_target, this.m_stateId, stateLevel, false);
        applyState.setCaster(this.m_caster);
        ((RunningEffect<FX, WakfuEffectContainer>)applyState).setEffectContainer((WakfuEffectContainer)this.m_effectContainer);
        ((RunningEffect<WakfuEffect, EC>)applyState).setGenericEffect((WakfuEffect)this.m_genericEffect);
        applyState.bypassResistancesCheck();
        applyState.execute(null, false);
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        this.m_stateId = (short)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_stateLevelPerPA = (short)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_stateLevelPerPM = (short)((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_stateLevelPerPW = (short)((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 5) {
            this.m_executeActionCost = true;
        }
        else {
            this.m_executeActionCost = (1 == ((WakfuEffect)this.m_genericEffect).getParam(4, level, RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyStateFunctionPaPmPw>() {
            @Override
            public ApplyStateFunctionPaPmPw makeObject() {
                return new ApplyStateFunctionPaPmPw();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default (pa/pm/pw, consomm\u00e9s \u00e0 la fin)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("levelPerPa", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("levelPerPm", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("levelPerPw", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Default (pa/pm/pw, consomm\u00e9s ou non)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("levelPerPa", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("levelPerPm", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("levelPerPw", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("consomm\u00e9s (0=non, 1 = oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
