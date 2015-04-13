package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class NullEffectOnCell extends NullEffect
{
    private static final ObjectPool m_staticPool;
    
    public NullEffectOnCell() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public NullEffectOnCell newInstance() {
        NullEffectOnCell re;
        try {
            re = (NullEffectOnCell)NullEffectOnCell.m_staticPool.borrowObject();
            re.m_pool = NullEffectOnCell.m_staticPool;
        }
        catch (Exception e) {
            re = new NullEffectOnCell();
            re.m_pool = null;
            re.m_isStatic = false;
            NullEffectOnCell.m_logger.error((Object)("Erreur lors d'un checkOut sur un NullEffectOnCell : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<NullEffectOnCell>() {
            @Override
            public NullEffectOnCell makeObject() {
                return new NullEffectOnCell();
            }
        });
    }
}
