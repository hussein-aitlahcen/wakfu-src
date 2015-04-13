package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveFightProperty extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveFightProperty.PARAMETERS_LIST_SET;
    }
    
    public RemoveFightProperty() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        RemoveFightProperty re;
        try {
            re = (RemoveFightProperty)RemoveFightProperty.m_staticPool.borrowObject();
            re.m_pool = RemoveFightProperty.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveFightProperty();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveFightProperty.m_logger.error((Object)("Erreur lors d'un checkOut sur un RemoveFightProperty : " + e.getMessage()));
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
        this.m_target.removeProperty(property);
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveFightProperty>() {
            @Override
            public RemoveFightProperty makeObject() {
                return new RemoveFightProperty();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("D\u00e9faut", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la propri\u00e9t\u00e9", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
