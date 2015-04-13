package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class HPGainForUndead extends HPGain
{
    private static final ObjectPool m_staticPool;
    
    public HPGainForUndead() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HPGainForUndead newInstance() {
        HPGainForUndead re;
        try {
            re = (HPGainForUndead)HPGainForUndead.m_staticPool.borrowObject();
            re.m_pool = HPGainForUndead.m_staticPool;
        }
        catch (Exception e) {
            re = new HPGainForUndead();
            re.m_pool = null;
            re.m_isStatic = false;
            HPGainForUndead.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPGainForUndead : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOnAuthorizedTarget() {
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.setNotified(true);
            return;
        }
        this.addHp();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HPGainForUndead>() {
            @Override
            public HPGainForUndead makeObject() {
                return new HPGainForUndead();
            }
        });
    }
}
