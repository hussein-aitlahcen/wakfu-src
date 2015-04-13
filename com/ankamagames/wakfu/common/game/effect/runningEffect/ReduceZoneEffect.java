package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ReduceZoneEffect extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ReduceZoneEffect.PARAMETERS_LIST_SET;
    }
    
    public ReduceZoneEffect() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ReduceZoneEffect newInstance() {
        ReduceZoneEffect re;
        try {
            re = (ReduceZoneEffect)ReduceZoneEffect.m_staticPool.borrowObject();
            re.m_pool = ReduceZoneEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new ReduceZoneEffect();
            re.m_pool = null;
            re.m_isStatic = false;
            ReduceZoneEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un ReduceZoneEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        RunningEffect triggeringEffect = triggerRE;
        if (triggeringEffect == null && this.getParams() != null) {
            triggeringEffect = ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect();
        }
        if (triggeringEffect == null) {
            this.setNotified();
            return;
        }
        if (this.comeFromVoodool(triggeringEffect)) {
            this.setNotified();
            return;
        }
        final WakfuEffect genericEffect = triggeringEffect.getGenericEffect();
        if (genericEffect != null && (genericEffect.getAreaOfEffect().getType() != AreaOfEffectEnum.POINT || genericEffect.hasProperty(RunningEffectPropertyType.ZONE_EFFECT))) {
            triggeringEffect.update(0, -this.m_value, false);
        }
    }
    
    private boolean comeFromVoodool(final RunningEffect triggeringEffect) {
        return triggeringEffect.getTriggersToExecute() != null && triggeringEffect.getTriggersToExecute().get(2138);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<ReduceZoneEffect>() {
            @Override
            public ReduceZoneEffect makeObject() {
                return new ReduceZoneEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("% de reduction", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de r\u00e9duction", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
