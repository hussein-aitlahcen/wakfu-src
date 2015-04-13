package com.ankamagames.wakfu.common.game.effect.runningEffect;

import cern.jet.random.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import cern.jet.random.engine.*;

public final class HpLossNormalDistrib extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final Normal RANDOM;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossNormalDistrib.PARAMETERS_LIST_SET;
    }
    
    public HpLossNormalDistrib() {
        super();
        this.setTriggersToExecute();
    }
    
    public HpLossNormalDistrib(final Elements element) {
        super();
        this.setTriggersToExecute();
        this.m_staticElement = element;
    }
    
    @Override
    public HpLossNormalDistrib newInstance() {
        HpLossNormalDistrib re;
        try {
            re = (HpLossNormalDistrib)HpLossNormalDistrib.m_staticPool.borrowObject();
            re.m_pool = HpLossNormalDistrib.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossNormalDistrib();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossNormalDistrib.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossNormalDistrib : " + e.getMessage()));
        }
        re.m_staticElement = this.m_staticElement;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() != 4) {
            return;
        }
        final short containerLevel = this.getContainerLevel();
        final int mean = ((WakfuEffect)this.m_genericEffect).getParam(0, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int variance = ((WakfuEffect)this.m_genericEffect).getParam(1, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int min = ((WakfuEffect)this.m_genericEffect).getParam(2, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int max = ((WakfuEffect)this.m_genericEffect).getParam(3, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final double value = HpLossNormalDistrib.RANDOM.nextDouble(mean, variance);
        this.m_value = (int)MathHelper.clamp(value, min, max);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossNormalDistrib>() {
            @Override
            public HpLossNormalDistrib makeObject() {
                return new HpLossNormalDistrib();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Parameters", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Moyenne", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Variance", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Min", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Max", WakfuRunningEffectParameterType.CONFIG) }) });
        RANDOM = new Normal(0.0, 0.0, new MersenneTwister());
    }
}
