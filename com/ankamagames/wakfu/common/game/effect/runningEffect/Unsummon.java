package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Unsummon extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Unsummon.PARAMETERS_LIST_SET;
    }
    
    public Unsummon() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public Unsummon newInstance() {
        Unsummon re;
        try {
            re = (Unsummon)Unsummon.m_staticPool.borrowObject();
            re.m_pool = Unsummon.m_staticPool;
        }
        catch (Exception e) {
            re = new Unsummon();
            re.m_isStatic = false;
            re.m_pool = null;
            Unsummon.m_logger.error((Object)("Erreur lors d'un checkOut sur un RE:Unsummon : " + e.getMessage()));
        }
        this.m_maxExecutionCount = 1;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null) {
            this.m_target.addProperty(FightPropertyType.NO_KO);
            this.m_target.addProperty(FightPropertyType.NO_DEATH);
            this.notifyExecution(linkedRE, trigger);
            this.m_target.getCharacteristic(FighterCharacteristicType.HP).toMin();
            this.m_target.getCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH).setMax(0);
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
        return false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Unsummon>() {
            @Override
            public Unsummon makeObject() {
                return new Unsummon();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de param", new WakfuRunningEffectParameter[0]) });
    }
}
