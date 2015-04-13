package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class DecurseLinkedToCaster extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return DecurseLinkedToCaster.PARAMETERS_LIST_SET;
    }
    
    public DecurseLinkedToCaster() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public DecurseLinkedToCaster newInstance() {
        DecurseLinkedToCaster re;
        try {
            re = (DecurseLinkedToCaster)DecurseLinkedToCaster.m_staticPool.borrowObject();
            re.m_pool = DecurseLinkedToCaster.m_staticPool;
        }
        catch (Exception e) {
            re = new DecurseLinkedToCaster();
            re.m_pool = null;
            re.m_isStatic = false;
            DecurseLinkedToCaster.m_logger.error((Object)("Erreur lors d'un checkOut sur un DecurseLinkedToCaster : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer() == null || this.m_target.getRunningEffectManager() == null) {
            this.setNotified();
            return;
        }
        final TimedRunningEffectManager runningEffectManager = (TimedRunningEffectManager)this.m_target.getRunningEffectManager();
        runningEffectManager.removeLinkedToCasterIncludingState(this.m_caster);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<DecurseLinkedToCaster>() {
            @Override
            public DecurseLinkedToCaster makeObject() {
                return new DecurseLinkedToCaster();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
