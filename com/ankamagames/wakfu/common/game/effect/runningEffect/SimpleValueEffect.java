package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SimpleValueEffect extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SimpleValueEffect.PARAMETERS_LIST_SET;
    }
    
    public SimpleValueEffect() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SimpleValueEffect newInstance() {
        SimpleValueEffect re;
        try {
            re = (SimpleValueEffect)SimpleValueEffect.m_staticPool.borrowObject();
            re.m_pool = SimpleValueEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new SimpleValueEffect();
            re.m_pool = null;
            re.m_isStatic = false;
            SimpleValueEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un SimpleValueEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
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
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SimpleValueEffect>() {
            @Override
            public SimpleValueEffect makeObject() {
                return new SimpleValueEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
