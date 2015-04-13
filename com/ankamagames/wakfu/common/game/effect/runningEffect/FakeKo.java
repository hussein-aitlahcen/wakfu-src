package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class FakeKo extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return FakeKo.PARAMETERS_LIST_SET;
    }
    
    public FakeKo() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public FakeKo newInstance() {
        FakeKo re;
        try {
            re = (FakeKo)FakeKo.m_staticPool.borrowObject();
            re.m_pool = FakeKo.m_staticPool;
        }
        catch (Exception e) {
            re = new FakeKo();
            re.m_pool = null;
            re.m_isStatic = false;
            FakeKo.m_logger.error((Object)("Erreur lors d'un checkOut sur un FakeKo : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.m_value = this.m_target.getCharacteristicValue(FighterCharacteristicType.HP);
        }
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
    public void unapplyOverride() {
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.HP).set(this.m_value);
            ((BasicCharacterInfo)this.m_target).setHasBeenRaisedByTrigger(true);
        }
        this.setNotifyUnapplicationForced(true);
        super.unapplyOverride();
    }
    
    @Override
    protected boolean canBeExecutedOnKO() {
        return true;
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
        m_staticPool = new MonitoredPool(new ObjectFactory<FakeKo>() {
            @Override
            public FakeKo makeObject() {
                return new FakeKo();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default", new WakfuRunningEffectParameter[0]) });
    }
}
