package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ChangeTriggeringEffectTarget extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_transmitToTarget;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangeTriggeringEffectTarget.PARAMETERS_LIST_SET;
    }
    
    public ChangeTriggeringEffectTarget() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ChangeTriggeringEffectTarget newInstance() {
        ChangeTriggeringEffectTarget re;
        try {
            re = (ChangeTriggeringEffectTarget)ChangeTriggeringEffectTarget.m_staticPool.borrowObject();
            re.m_pool = ChangeTriggeringEffectTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangeTriggeringEffectTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            ChangeTriggeringEffectTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un ChangeTriggeringEffectTarget : " + e.getMessage()));
        }
        re.m_transmitToTarget = this.m_transmitToTarget;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = -1;
        this.m_transmitToTarget = false;
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_transmitToTarget = (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        final RunningEffect triggeringEffect = this.getTriggeringEffect(triggerRE);
        if (triggeringEffect == null) {
            return;
        }
        if (this.m_transmitToTarget) {
            triggeringEffect.setTarget(this.m_target);
        }
        else {
            triggeringEffect.setTarget(this.m_caster);
        }
        if (this.m_value > 0) {
            triggeringEffect.update(0, this.m_value, true);
        }
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
        this.m_transmitToTarget = false;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangeTriggeringEffectTarget>() {
            @Override
            public ChangeTriggeringEffectTarget makeObject() {
                return new ChangeTriggeringEffectTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Ratio de la valeur transmise", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio (default 100%)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Transmission (target/caster)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio (default 100%)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Transmis \u00e0 la cible de cet effet (oui = 1, defaut = transmis au caster)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
