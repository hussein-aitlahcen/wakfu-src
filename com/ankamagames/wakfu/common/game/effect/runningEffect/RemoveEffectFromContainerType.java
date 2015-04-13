package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveEffectFromContainerType extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_containerType;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveEffectFromContainerType.PARAMETERS_LIST_SET;
    }
    
    public RemoveEffectFromContainerType() {
        super();
    }
    
    public RemoveEffectFromContainerType(final int containerType) {
        super();
        this.m_containerType = containerType;
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveEffectFromContainerType newInstance() {
        RemoveEffectFromContainerType re;
        try {
            re = (RemoveEffectFromContainerType)RemoveEffectFromContainerType.m_staticPool.borrowObject();
            re.m_pool = RemoveEffectFromContainerType.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveEffectFromContainerType();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveEffectFromContainerType.m_logger.error((Object)("Erreur lors d'un checkOut sur un RemoveEffectFromContainerType : " + e.getMessage()));
        }
        re.m_containerType = this.m_containerType;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || this.m_target.getRunningEffectManager() == null) {
            this.setNotified();
            return;
        }
        ((TimedRunningEffectManager)this.m_target.getRunningEffectManager()).removeLinkedToContainerType(this.m_containerType, true, true);
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveEffectFromContainerType>() {
            @Override
            public RemoveEffectFromContainerType makeObject() {
                return new RemoveEffectFromContainerType();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de params", new WakfuRunningEffectParameter[0]) });
    }
}
