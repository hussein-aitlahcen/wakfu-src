package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossFctPaPmWithoutConsume extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossFctPaPmWithoutConsume.PARAMETERS_LIST_SET;
    }
    
    public HpLossFctPaPmWithoutConsume() {
        super();
        this.setTriggersToExecute();
    }
    
    public HpLossFctPaPmWithoutConsume(final Elements element, final ComputeMode mode) {
        super(element, mode);
    }
    
    @Override
    public HpLossFctPaPmWithoutConsume newInstance() {
        HpLossFctPaPmWithoutConsume re;
        try {
            re = (HpLossFctPaPmWithoutConsume)HpLossFctPaPmWithoutConsume.m_staticPool.borrowObject();
            re.m_pool = HpLossFctPaPmWithoutConsume.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossFctPaPmWithoutConsume();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossFctPaPmWithoutConsume.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossFctPaPmWithoutConsume : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.defaultCondition();
        this.m_value = 0;
        if (this.m_caster == null) {
            return;
        }
        final short containerLevel = this.getContainerLevel();
        int remainingAP = this.m_caster.getCharacteristicValue(FighterCharacteristicType.AP);
        int remainingMP = this.m_caster.getCharacteristicValue(FighterCharacteristicType.MP);
        if (remainingAP == 0 && remainingMP == 0) {
            return;
        }
        final float dmgPerAP = ((WakfuEffect)this.m_genericEffect).getParam(0, containerLevel);
        final float dmgPerMP = ((WakfuEffect)this.m_genericEffect).getParam(1, containerLevel);
        if (dmgPerAP == 0.0f) {
            remainingAP = 0;
        }
        if (dmgPerMP == 0.0f) {
            remainingMP = 0;
        }
        this.m_value = Math.round(dmgPerAP * remainingAP + dmgPerMP * remainingMP);
        this.computeModificator(this.m_condition);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossFctPaPmWithoutConsume>() {
            @Override
            public HpLossFctPaPmWithoutConsume makeObject() {
                return new HpLossFctPaPmWithoutConsume();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Dmg par PA, Dmg par PM", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Dmg par PM", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
