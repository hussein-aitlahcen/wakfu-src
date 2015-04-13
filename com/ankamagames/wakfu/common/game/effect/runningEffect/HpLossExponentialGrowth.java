package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossExponentialGrowth extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossExponentialGrowth.PARAMETERS_LIST_SET;
    }
    
    public HpLossExponentialGrowth() {
        super();
        this.setTriggersToExecute();
    }
    
    public HpLossExponentialGrowth(final Elements element) {
        super();
        this.setTriggersToExecute();
        this.m_staticElement = element;
    }
    
    @Override
    public HpLossExponentialGrowth newInstance() {
        HpLossExponentialGrowth re;
        try {
            re = (HpLossExponentialGrowth)HpLossExponentialGrowth.m_staticPool.borrowObject();
            re.m_pool = HpLossExponentialGrowth.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossExponentialGrowth();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossExponentialGrowth.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossExponentialGrowth : " + e.getMessage()));
        }
        re.m_staticElement = this.m_staticElement;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() != 3) {
            return;
        }
        final short containerLevel = this.getContainerLevel();
        final double a = ((WakfuEffect)this.m_genericEffect).getParam(0);
        final double b = ((WakfuEffect)this.m_genericEffect).getParam(1);
        final double c = ((WakfuEffect)this.m_genericEffect).getParam(2);
        final double hpLoss = a + b * Math.pow(containerLevel, c);
        this.m_value = Math.max(0, ValueRounder.randomRound((float)hpLoss));
        this.computeModificator(this.defaultCondition());
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        super.executeOverride(triggerRE, trigger);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossExponentialGrowth>() {
            @Override
            public HpLossExponentialGrowth makeObject() {
                return new HpLossExponentialGrowth();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("perte de PdV, \u00e9volution exponentielle hpLoss = a + b * level^c", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("a", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("b", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("c", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
