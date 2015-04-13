package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class ReplaceAreaByAnotherUsingTarget extends ReplaceAreaByAnother
{
    private static final ObjectPool m_staticPool;
    
    public ReplaceAreaByAnotherUsingTarget() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ReplaceAreaByAnotherUsingTarget newInstance() {
        ReplaceAreaByAnotherUsingTarget re;
        try {
            re = (ReplaceAreaByAnotherUsingTarget)ReplaceAreaByAnotherUsingTarget.m_staticPool.borrowObject();
            re.m_pool = ReplaceAreaByAnotherUsingTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new ReplaceAreaByAnotherUsingTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            ReplaceAreaByAnotherUsingTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un ReplaceAreaByAnotherUsingTarget : " + e.getMessage()));
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
        m_staticPool = new MonitoredPool(new ObjectFactory<ReplaceAreaByAnotherUsingTarget>() {
            @Override
            public ReplaceAreaByAnotherUsingTarget makeObject() {
                return new ReplaceAreaByAnotherUsingTarget();
            }
        });
    }
}
