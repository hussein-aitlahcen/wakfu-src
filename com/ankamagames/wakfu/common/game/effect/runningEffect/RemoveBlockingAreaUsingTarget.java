package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveBlockingAreaUsingTarget extends ReplaceAreaByAnother
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveBlockingAreaUsingTarget.PARAMETERS_LIST_SET;
    }
    
    public RemoveBlockingAreaUsingTarget() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveBlockingAreaUsingTarget newInstance() {
        RemoveBlockingAreaUsingTarget re;
        try {
            re = (RemoveBlockingAreaUsingTarget)RemoveBlockingAreaUsingTarget.m_staticPool.borrowObject();
            re.m_pool = RemoveBlockingAreaUsingTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveBlockingAreaUsingTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveBlockingAreaUsingTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un ReplaceAreaByAnotherUsingTarget : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected boolean cannotBeRemoved(final BasicEffectArea area) {
        return !area.isBlockingMovement() || super.cannotBeRemoved(area);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveBlockingAreaUsingTarget>() {
            @Override
            public RemoveBlockingAreaUsingTarget makeObject() {
                return new RemoveBlockingAreaUsingTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Retrait uniquement", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Zone a retirer (-1 pour tout retirer)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
