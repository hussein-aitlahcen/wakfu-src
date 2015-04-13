package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossFunctionTriggeringValue extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossFunctionTriggeringValue.PARAMETERS_LIST_SET;
    }
    
    public HpLossFunctionTriggeringValue() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossFunctionTriggeringValue newInstance() {
        HpLossFunctionTriggeringValue re;
        try {
            re = (HpLossFunctionTriggeringValue)HpLossFunctionTriggeringValue.m_staticPool.borrowObject();
            re.m_pool = HpLossFunctionTriggeringValue.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossFunctionTriggeringValue();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossFunctionTriggeringValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossFunctionTriggeringValue : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE != null && linkedRE.getValue() == 0) {
            this.setNotified();
            return;
        }
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    protected void extractParams(final RunningEffect triggerRE) {
        this.m_condition = 0;
        this.m_staticcomputeMode = ComputeMode.CLASSIC;
        this.m_staticElement = Elements.PHYSICAL;
        final RunningEffect triggeringEffect = (triggerRE != null) ? triggerRE : ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect();
        if (triggeringEffect == null) {
            this.m_value = 0;
            HpLossFunctionTriggeringValue.m_logger.error((Object)"Impossible de calculer la valeur de cet effet, il doit etre d\u00e9clencher par un autre");
            return;
        }
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            return;
        }
        final float percentage = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final float value = triggeringEffect.getValue() * percentage / 100.0f;
        this.m_value = ValueRounder.randomRound(value);
        final int paramsCount = ((WakfuEffect)this.m_genericEffect).getParamsCount();
        if (paramsCount > 1) {
            this.m_staticElement = Elements.getElementFromId((byte)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
        if (paramsCount >= 3) {
            this.setCondition(((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossFunctionTriggeringValue>() {
            @Override
            public HpLossFunctionTriggeringValue makeObject() {
                return new HpLossFunctionTriggeringValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("% de la valeur de l'effet d\u00e9clencheur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur de l'effet d\u00e9clencheur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Avec \u00e9l\u00e9ment", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur de l'effet d\u00e9clencheur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Avec \u00e9l\u00e9ment et modificateurs", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur de l'effet d\u00e9clencheur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("mod : boost(1) / res (2) / rebound (4) / absorb(8) ", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
