package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ApplyWorldProperty extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApplyWorldProperty.PARAMETERS_LIST_SET;
    }
    
    public ApplyWorldProperty() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        ApplyWorldProperty re;
        try {
            re = (ApplyWorldProperty)ApplyWorldProperty.m_staticPool.borrowObject();
            re.m_pool = ApplyWorldProperty.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyWorldProperty();
            re.m_pool = null;
            re.m_isStatic = false;
            ApplyWorldProperty.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApplyFightProperty : " + e.getMessage()));
        }
        re.m_value = this.m_value;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified(true);
            return;
        }
        final WorldPropertyType property = WorldPropertyType.getPropertyFromId(this.m_value);
        if (property == null) {
            this.setNotified(true);
            return;
        }
        this.m_target.addProperty(property);
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
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target == null) {
            return;
        }
        this.m_target.removeProperty(WorldPropertyType.getPropertyFromId(this.m_value));
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyWorldProperty>() {
            @Override
            public ApplyWorldProperty makeObject() {
                return new ApplyWorldProperty();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("D\u00e9faut", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la propri\u00e9t\u00e9", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
