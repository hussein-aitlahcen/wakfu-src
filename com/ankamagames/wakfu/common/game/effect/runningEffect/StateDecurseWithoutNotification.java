package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class StateDecurseWithoutNotification extends StateDecurse
{
    private static final ObjectPool m_staticPool;
    
    @Override
    public StateDecurseWithoutNotification newInstance() {
        StateDecurseWithoutNotification re;
        try {
            re = (StateDecurseWithoutNotification)StateDecurseWithoutNotification.m_staticPool.borrowObject();
            re.m_pool = StateDecurseWithoutNotification.m_staticPool;
        }
        catch (Exception e) {
            re = new StateDecurseWithoutNotification();
            re.m_pool = null;
            StateDecurseWithoutNotification.m_logger.error((Object)("Erreur lors d'un newInstance sur un StateDecurseWithoutNotification : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public boolean isNotified() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<StateDecurseWithoutNotification>() {
            @Override
            public StateDecurseWithoutNotification makeObject() {
                return new StateDecurseWithoutNotification();
            }
        });
    }
}
