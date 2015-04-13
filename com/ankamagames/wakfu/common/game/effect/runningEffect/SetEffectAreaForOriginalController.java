package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class SetEffectAreaForOriginalController extends SetEffectArea
{
    private static final ObjectPool m_setEffectAreaForControllerPool;
    
    @Override
    protected ObjectPool getPool() {
        return SetEffectAreaForOriginalController.m_setEffectAreaForControllerPool;
    }
    
    public SetEffectAreaForOriginalController() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    protected EffectUser getOwner() {
        if (this.m_caster instanceof BasicCharacterInfo) {
            return ((BasicCharacterInfo)this.m_caster).getOriginalController();
        }
        return this.m_caster;
    }
    
    static {
        m_setEffectAreaForControllerPool = new MonitoredPool(new ObjectFactory<SetEffectAreaForOriginalController>() {
            @Override
            public SetEffectAreaForOriginalController makeObject() {
                return new SetEffectAreaForOriginalController();
            }
        });
    }
}
