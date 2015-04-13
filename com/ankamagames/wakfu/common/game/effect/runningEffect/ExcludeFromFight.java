package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ExcludeFromFight extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ExcludeFromFight.PARAMETERS_LIST_SET;
    }
    
    public ExcludeFromFight() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ExcludeFromFight newInstance() {
        ExcludeFromFight re;
        try {
            re = (ExcludeFromFight)ExcludeFromFight.m_staticPool.borrowObject();
            re.m_pool = ExcludeFromFight.m_staticPool;
        }
        catch (Exception e) {
            re = new ExcludeFromFight();
            re.m_pool = null;
            re.m_isStatic = false;
            ExcludeFromFight.m_logger.error((Object)("Erreur lors d'un checkOut sur un ExcludeFromFight : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            return;
        }
        this.notifyExecution(triggerRE, trigger);
        if (this.m_target instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_target).excludeFromFight();
        }
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
        m_staticPool = new MonitoredPool(new ObjectFactory<ExcludeFromFight>() {
            @Override
            public ExcludeFromFight makeObject() {
                return new ExcludeFromFight();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
