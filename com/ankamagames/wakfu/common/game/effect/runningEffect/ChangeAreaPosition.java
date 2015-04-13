package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ChangeAreaPosition extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangeAreaPosition.PARAMETERS_LIST_SET;
    }
    
    public ChangeAreaPosition() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ChangeAreaPosition newInstance() {
        ChangeAreaPosition re;
        try {
            re = (ChangeAreaPosition)ChangeAreaPosition.m_staticPool.borrowObject();
            re.m_pool = ChangeAreaPosition.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangeAreaPosition();
            re.m_pool = null;
            re.m_isStatic = false;
            ChangeAreaPosition.m_logger.error((Object)("Erreur lors d'un checkOut sur un ChangeAreaPosition : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified();
            return;
        }
        if (!(this.m_target instanceof BasicEffectArea)) {
            this.setNotified();
            return;
        }
        final BasicEffectArea area = (BasicEffectArea)this.m_target;
        area.setPosition(this.m_targetCell);
        area.computeZone();
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
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangeAreaPosition>() {
            @Override
            public ChangeAreaPosition makeObject() {
                return new ChangeAreaPosition();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
