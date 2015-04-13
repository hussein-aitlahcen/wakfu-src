package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveAreaUsingTarget extends ReplaceAreaByAnother
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveAreaUsingTarget.PARAMETERS_LIST_SET;
    }
    
    public RemoveAreaUsingTarget() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveAreaUsingTarget newInstance() {
        RemoveAreaUsingTarget re;
        try {
            re = (RemoveAreaUsingTarget)RemoveAreaUsingTarget.m_staticPool.borrowObject();
            re.m_pool = RemoveAreaUsingTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveAreaUsingTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveAreaUsingTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un ReplaceAreaByAnotherUsingTarget : " + e.getMessage()));
        }
        return re;
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
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveAreaUsingTarget>() {
            @Override
            public RemoveAreaUsingTarget makeObject() {
                return new RemoveAreaUsingTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Retrait uniquement", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Zone a retirer (-1 pour tout retirer)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
