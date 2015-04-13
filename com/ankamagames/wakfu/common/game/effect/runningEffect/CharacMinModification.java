package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacMinModification extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacMinModification.PARAMETERS_LIST_SET;
    }
    
    public CharacMinModification(final FighterCharacteristicType charac) {
        super();
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    private CharacMinModification() {
        super();
    }
    
    @Override
    public CharacMinModification newInstance() {
        CharacMinModification re;
        try {
            re = (CharacMinModification)CharacMinModification.m_staticPool.borrowObject();
            re.m_pool = CharacMinModification.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacMinModification();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacMinModification.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacMinModification : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || !this.m_target.hasCharacteristic(this.m_charac)) {
            this.setNotified();
            return;
        }
        final AbstractCharacteristic targetHp = this.m_target.getCharacteristic(this.m_charac);
        final int previousMin = targetHp.min();
        if (this.isValueComputationEnabled()) {
            targetHp.setMin(this.m_value);
            this.m_value = targetHp.min() - previousMin;
        }
        else {
            targetHp.setMin(targetHp.min() + this.m_value);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target == null || !this.m_target.hasCharacteristic(this.m_charac)) {
            return;
        }
        final AbstractCharacteristic targetHp = this.m_target.getCharacteristic(this.m_charac);
        targetHp.setMin(targetHp.min() - this.m_value);
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
        this.m_charac = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacMinModification>() {
            @Override
            public CharacMinModification makeObject() {
                return new CharacMinModification((CharacMinModification$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nouveau min", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
