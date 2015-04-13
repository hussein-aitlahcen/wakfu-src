package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossThenHpLossFunctionPreviousValue extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_percentOfPreviousValue;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossThenHpLossFunctionPreviousValue.PARAMETERS_LIST_SET;
    }
    
    public HpLossThenHpLossFunctionPreviousValue() {
        super();
        this.setTriggersToExecute();
    }
    
    public HpLossThenHpLossFunctionPreviousValue(final Elements element, final ComputeMode mode) {
        super(element, mode);
    }
    
    @Override
    public HpLossThenHpLossFunctionPreviousValue newInstance() {
        HpLossThenHpLossFunctionPreviousValue re;
        try {
            re = (HpLossThenHpLossFunctionPreviousValue)HpLossThenHpLossFunctionPreviousValue.m_staticPool.borrowObject();
            re.m_pool = HpLossThenHpLossFunctionPreviousValue.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossThenHpLossFunctionPreviousValue();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossThenHpLossFunctionPreviousValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossThenHpLossFunctionPreviousValue : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    protected void extractParams(final RunningEffect triggerRE) {
        this.defaultCondition();
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_percentOfPreviousValue = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        super.executeOverride(triggerRE, trigger);
        this.notifyExecution(triggerRE, trigger);
        if (!this.isValueComputationEnabled()) {
            return;
        }
        final int secondDamageValue = this.m_value * this.m_percentOfPreviousValue / 100;
        if (secondDamageValue == 0) {
            return;
        }
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.getElement(), ComputeMode.CLASSIC, secondDamageValue, this.m_target);
        hpLoss.disableValueComputation();
        ((RunningEffect<DefaultFightInstantEffectWithChatNotif, EC>)hpLoss).setGenericEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
        hpLoss.setCaster(this.m_caster);
        hpLoss.execute(null, false);
    }
    
    @Override
    public void onCheckIn() {
        this.m_percentOfPreviousValue = 0;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossThenHpLossFunctionPreviousValue>() {
            @Override
            public HpLossThenHpLossFunctionPreviousValue makeObject() {
                return new HpLossThenHpLossFunctionPreviousValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Dommages + % de d\u00e9g\u00e2ts subis", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg ou valeur %", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+% des d\u00e9g\u00e2ts inflig\u00e9s ", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
