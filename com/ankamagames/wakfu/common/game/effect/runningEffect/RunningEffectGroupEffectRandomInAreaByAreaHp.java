package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class RunningEffectGroupEffectRandomInAreaByAreaHp extends RunningEffectGroupEffectRandomInArea
{
    private static final ObjectPool m_staticPool;
    
    public RunningEffectGroupEffectRandomInAreaByAreaHp() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupEffectRandomInAreaByAreaHp newInstance() {
        RunningEffectGroupEffectRandomInAreaByAreaHp re;
        try {
            re = (RunningEffectGroupEffectRandomInAreaByAreaHp)RunningEffectGroupEffectRandomInAreaByAreaHp.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupEffectRandomInAreaByAreaHp.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupEffectRandomInAreaByAreaHp();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupEffectRandomInAreaByAreaHp.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupEffectRandomInAreaByCharac : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
            this.m_maxEffectToExecute = this.m_caster.getCharacteristicValue(FighterCharacteristicType.AREA_HP);
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupEffectRandomInAreaByAreaHp>() {
            @Override
            public RunningEffectGroupEffectRandomInAreaByAreaHp makeObject() {
                return new RunningEffectGroupEffectRandomInAreaByAreaHp();
            }
        });
    }
}
