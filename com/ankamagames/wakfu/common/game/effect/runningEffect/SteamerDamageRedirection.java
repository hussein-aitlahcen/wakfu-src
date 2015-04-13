package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SteamerDamageRedirection extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SteamerDamageRedirection.PARAMETERS_LIST_SET;
    }
    
    public SteamerDamageRedirection() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SteamerDamageRedirection newInstance() {
        SteamerDamageRedirection re;
        try {
            re = (SteamerDamageRedirection)SteamerDamageRedirection.m_staticPool.borrowObject();
            re.m_pool = SteamerDamageRedirection.m_staticPool;
        }
        catch (Exception e) {
            re = new SteamerDamageRedirection();
            re.m_pool = null;
            re.m_isStatic = false;
            SteamerDamageRedirection.m_logger.error((Object)("Erreur lors d'un checkOut sur un SteamerDamageRedirection : " + e.getMessage()));
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
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        this.setNotified();
        if (linkedRE == null) {
            return;
        }
        if (!this.m_caster.isInPlay()) {
            return;
        }
        final WakfuRunningEffect onSteamerEffect = (WakfuRunningEffect)linkedRE.newParameterizedInstance();
        onSteamerEffect.setTarget(this.m_caster);
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(true, true, (WakfuRunningEffect)this.getParent());
        final int percentValue = this.m_value;
        final int originalValue = linkedRE.getValue();
        final int redirectedValue = Math.min(originalValue * percentValue / 100, originalValue);
        final int modifiedValue = originalValue - redirectedValue;
        params.setForcedValue(redirectedValue);
        onSteamerEffect.setExecutionParameters(params);
        onSteamerEffect.computeValue(linkedRE);
        onSteamerEffect.askForExecution();
        linkedRE.forceValue(modifiedValue);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<SteamerDamageRedirection>() {
            @Override
            public SteamerDamageRedirection makeObject() {
                return new SteamerDamageRedirection();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur de la redirection", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Redirection en %", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
