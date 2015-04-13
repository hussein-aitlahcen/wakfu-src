package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class ApplyStateWithoutNotification extends ApplyState
{
    private static final ObjectPool m_staticPool;
    
    @Override
    public ApplyStateWithoutNotification newInstance() {
        ApplyStateWithoutNotification re;
        try {
            re = (ApplyStateWithoutNotification)ApplyStateWithoutNotification.m_staticPool.borrowObject();
            re.m_pool = ApplyStateWithoutNotification.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyStateWithoutNotification();
            re.m_pool = null;
            ApplyStateWithoutNotification.m_logger.error((Object)("Erreur lors d'un newInstance sur un ApplyStateWithoutNotification : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    public boolean isNotified() {
        return true;
    }
    
    @Override
    public void notifyExecution(final RunningEffect triggerRE, final boolean trigger) {
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyStateWithoutNotification>() {
            @Override
            public ApplyStateWithoutNotification makeObject() {
                return new ApplyStateWithoutNotification();
            }
        });
    }
}
