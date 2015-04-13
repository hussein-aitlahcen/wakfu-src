package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class EffectProbaFunctionCasterCharac extends UsingEffectGroupRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return EffectProbaFunctionCasterCharac.PARAMETERS_LIST_SET;
    }
    
    public EffectProbaFunctionCasterCharac() {
        super();
        this.setTriggersToExecute();
    }
    
    public EffectProbaFunctionCasterCharac(final FighterCharacteristicType charac) {
        super();
        this.m_charac = charac;
    }
    
    @Override
    public EffectProbaFunctionCasterCharac newInstance() {
        EffectProbaFunctionCasterCharac re;
        try {
            re = (EffectProbaFunctionCasterCharac)EffectProbaFunctionCasterCharac.m_staticPool.borrowObject();
            re.m_pool = EffectProbaFunctionCasterCharac.m_staticPool;
        }
        catch (Exception e) {
            re = new EffectProbaFunctionCasterCharac();
            re.m_pool = null;
            re.m_isStatic = false;
            EffectProbaFunctionCasterCharac.m_logger.error((Object)("Erreur lors d'un checkOut sur un EffectProbaFunctionCasterCharac : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (this.m_caster == null) {
            return;
        }
        if (!this.m_caster.hasCharacteristic(this.m_charac)) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 1) {
            final int characValue = this.m_caster.getCharacteristicValue(this.m_charac);
            this.m_value *= characValue;
        }
        else if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            final int inc = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final int characValue2 = this.m_caster.getCharacteristicValue(this.m_charac);
            this.m_value += inc * characValue2;
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (this.m_value <= 0) {
            return;
        }
        if (DiceRoll.roll(100) > this.m_value) {
            return;
        }
        this.executeEffectGroup((WakfuRunningEffect)triggerRE);
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
        this.m_charac = null;
        super.onCheckIn();
    }
    
    @Override
    protected boolean isProbabilityComputationDisabled() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<EffectProbaFunctionCasterCharac>() {
            @Override
            public EffectProbaFunctionCasterCharac makeObject() {
                return new EffectProbaFunctionCasterCharac();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Proba / charac", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Base", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Increment / charac", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
