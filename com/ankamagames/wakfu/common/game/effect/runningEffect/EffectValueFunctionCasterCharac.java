package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class EffectValueFunctionCasterCharac extends UsingEffectGroupRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return EffectValueFunctionCasterCharac.PARAMETERS_LIST_SET;
    }
    
    private EffectValueFunctionCasterCharac() {
        super();
    }
    
    public EffectValueFunctionCasterCharac(final FighterCharacteristicType charac) {
        super();
        this.setTriggersToExecute();
        this.m_charac = charac;
    }
    
    @Override
    public EffectValueFunctionCasterCharac newInstance() {
        EffectValueFunctionCasterCharac re;
        try {
            re = (EffectValueFunctionCasterCharac)EffectValueFunctionCasterCharac.m_staticPool.borrowObject();
            re.m_pool = EffectValueFunctionCasterCharac.m_staticPool;
        }
        catch (Exception e) {
            re = new EffectValueFunctionCasterCharac();
            re.m_pool = null;
            re.m_isStatic = false;
            EffectValueFunctionCasterCharac.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupSecondValueFunctionFirst : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 1) {
            final int baseValue = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (this.m_caster.hasCharacteristic(this.m_charac)) {
                this.m_value = baseValue * this.m_caster.getCharacteristicValue(this.m_charac);
            }
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            final int baseValue = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final int baseValuePercent = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (this.m_caster.hasCharacteristic(this.m_charac)) {
                final int baseValuePercentBonus = baseValuePercent * this.m_caster.getCharacteristicValue(this.m_charac);
                this.m_value = baseValue * (1 + baseValuePercentBonus / 100);
            }
        }
    }
    
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
        params.setForcedValue(this.m_value);
        return params;
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
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<EffectValueFunctionCasterCharac>() {
            @Override
            public EffectValueFunctionCasterCharac makeObject() {
                return new EffectValueFunctionCasterCharac((EffectValueFunctionCasterCharac$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur fonction d'une charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur / charac", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Valeur fonction d'une charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de base", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% valeur de base / charac", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
