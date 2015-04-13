package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class BringBackToControllerSymbiot extends BringBackToSymbiot
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return BringBackToControllerSymbiot.PARAMETERS_LIST_SET;
    }
    
    public BringBackToControllerSymbiot() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public BringBackToControllerSymbiot newInstance() {
        BringBackToControllerSymbiot re;
        try {
            re = (BringBackToControllerSymbiot)BringBackToControllerSymbiot.m_staticPool.borrowObject();
            re.m_pool = BringBackToControllerSymbiot.m_staticPool;
        }
        catch (Exception e) {
            re = new BringBackToControllerSymbiot();
            re.m_pool = null;
            re.m_isStatic = false;
            BringBackToControllerSymbiot.m_logger.error((Object)("Erreur lors d'un checkOut sur un BringBackToControllerSymbiot : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected EffectUser getSymbiotOwner() {
        if (!(this.m_caster instanceof BasicCharacterInfo)) {
            return null;
        }
        return ((BasicCharacterInfo)this.m_caster).getController();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<BringBackToControllerSymbiot>() {
            @Override
            public BringBackToControllerSymbiot makeObject() {
                return new BringBackToControllerSymbiot();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
