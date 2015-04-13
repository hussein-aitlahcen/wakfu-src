package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class NullEffectNotifiedToAI extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return NullEffectNotifiedToAI.PARAMETERS_LIST_SET;
    }
    
    public NullEffectNotifiedToAI() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public NullEffectNotifiedToAI newInstance() {
        NullEffectNotifiedToAI re;
        try {
            re = (NullEffectNotifiedToAI)NullEffectNotifiedToAI.m_staticPool.borrowObject();
            re.m_pool = NullEffectNotifiedToAI.m_staticPool;
        }
        catch (Exception e) {
            re = new NullEffectNotifiedToAI();
            re.m_pool = null;
            re.m_isStatic = false;
            NullEffectNotifiedToAI.m_logger.error((Object)("Erreur lors d'un checkOut sur un NullEffectNotifiedToAI : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<NullEffectNotifiedToAI>() {
            @Override
            public NullEffectNotifiedToAI makeObject() {
                return new NullEffectNotifiedToAI();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
