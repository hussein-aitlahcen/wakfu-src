package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class TeleportTarget extends Teleport
{
    private static final ObjectPool m_staticPool;
    
    @Override
    public TeleportTarget newInstance() {
        TeleportTarget re;
        try {
            re = (TeleportTarget)TeleportTarget.m_staticPool.borrowObject();
            re.m_pool = TeleportTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new TeleportTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            TeleportTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un Push : " + e.getMessage()));
        }
        re.m_canBeExecuted = true;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(198);
    }
    
    @Override
    protected EffectUser getCharacterToTeleport() {
        return this.m_target;
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
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<TeleportTarget>() {
            @Override
            public TeleportTarget makeObject() {
                return new TeleportTarget();
            }
        });
    }
}
