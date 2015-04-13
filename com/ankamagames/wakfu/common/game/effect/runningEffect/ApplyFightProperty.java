package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ApplyFightProperty extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApplyFightProperty.PARAMETERS_LIST_SET;
    }
    
    public ApplyFightProperty() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        ApplyFightProperty re;
        try {
            re = (ApplyFightProperty)ApplyFightProperty.m_staticPool.borrowObject();
            re.m_pool = ApplyFightProperty.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyFightProperty();
            re.m_pool = null;
            re.m_isStatic = false;
            ApplyFightProperty.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApplyFightProperty : " + e.getMessage()));
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
        final FightPropertyType property = FightPropertyType.getPropertyFromId(this.m_value);
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
        final FightPropertyType property = FightPropertyType.getPropertyFromId(this.m_value);
        if (property == null) {
            return;
        }
        this.m_target.removeProperty(property);
        super.unapplyOverride();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyFightProperty>() {
            @Override
            public ApplyFightProperty makeObject() {
                return new ApplyFightProperty();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("D\u00e9faut", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la propri\u00e9t\u00e9", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
