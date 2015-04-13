package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Death extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_raiseForbidden;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Death.PARAMETERS_LIST_SET;
    }
    
    public Death() {
        super();
        this.setTriggersToExecute();
        this.m_triggers.set(192);
    }
    
    @Override
    public Death newInstance() {
        Death re;
        try {
            re = (Death)Death.m_staticPool.borrowObject();
            re.m_pool = Death.m_staticPool;
        }
        catch (Exception e) {
            re = new Death();
            re.m_isStatic = false;
            re.m_pool = null;
            Death.m_logger.error((Object)("Erreur lors d'un checkOut sur un RE:Death : " + e.getMessage()));
        }
        this.m_maxExecutionCount = 1;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(192);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified(true);
            return;
        }
        if (this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.HP).toMin();
        }
        else {
            if (!this.m_target.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
                this.setNotified();
                return;
            }
            this.m_target.getCharacteristic(FighterCharacteristicType.AREA_HP).toMin();
        }
        if (this.m_raiseForbidden) {
            this.m_target.addProperty(FightPropertyType.DONT_TRIGGER_KO);
        }
        if (this.m_target.hasCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH).setMax(0);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_raiseForbidden = false;
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_raiseForbidden = (((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
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
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_raiseForbidden = false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Death>() {
            @Override
            public Death makeObject() {
                return new Death();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de param", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Ne declenche pas de KO", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Pas de KO (non = 0 (defaut), oui = 1)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
