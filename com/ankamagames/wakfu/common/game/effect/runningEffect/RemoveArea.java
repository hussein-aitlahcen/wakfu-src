package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveArea extends ReplaceAreaByAnother
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveArea.PARAMETERS_LIST_SET;
    }
    
    public RemoveArea() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveArea newInstance() {
        RemoveArea re;
        try {
            re = (RemoveArea)RemoveArea.m_staticPool.borrowObject();
            re.m_pool = RemoveArea.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveArea();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveArea.m_logger.error((Object)("Erreur lors d'un checkOut sur un ReplaceAreaByAnotherUsingTarget : " + e.getMessage()));
        }
        return re;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveArea>() {
            @Override
            public RemoveArea makeObject() {
                return new RemoveArea();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Retrait uniquement", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Zone a retirer (-1 pour tout retirer)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
