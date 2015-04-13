package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

abstract class ModifySubEffectValueByCharac extends UsingEffectGroupRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ModifySubEffectValueByCharac.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final EffectUser characHolder = this.getCharacHolder();
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 1) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (characHolder.hasCharacteristic(this.m_charac)) {
                this.m_value = Math.max(this.m_value * characHolder.getCharacteristicValue(this.m_charac), 1);
            }
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final int increment = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (characHolder.hasCharacteristic(this.m_charac)) {
                this.m_value += increment * characHolder.getCharacteristicValue(this.m_charac);
            }
        }
    }
    
    protected abstract EffectUser getCharacHolder();
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_genericEffect == null) {
            this.setNotified();
            return;
        }
        this.executeEffectGroup((WakfuRunningEffect)triggerRE);
    }
    
    @Override
    protected boolean isProbabilityComputationDisabled() {
        return true;
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setResetLimitedApplyCount(false);
        params.setForcedValue(this.m_value, WakfuEffectExecutionParameters.ForcedValueType.PERCENT);
        return params;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur * charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur du modificateur", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Valeur de base charac + increment", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de base", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("increment", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
