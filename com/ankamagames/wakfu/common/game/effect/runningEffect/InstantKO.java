package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class InstantKO extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return InstantKO.PARAMETERS_LIST_SET;
    }
    
    public InstantKO() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public InstantKO newInstance() {
        InstantKO re;
        try {
            re = (InstantKO)InstantKO.m_staticPool.borrowObject();
            re.m_pool = InstantKO.m_staticPool;
        }
        catch (Exception e) {
            re = new InstantKO();
            re.m_pool = null;
            re.m_isStatic = false;
            InstantKO.m_logger.error((Object)("Erreur lors d'un checkOut sur un InstantKO : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.HP).toMin();
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<InstantKO>() {
            @Override
            public InstantKO makeObject() {
                return new InstantKO();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
