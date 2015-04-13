package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpGainRandomPercentOfValue extends HPGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpGainRandomPercentOfValue.PARAMETERS_LIST_SET;
    }
    
    public HpGainRandomPercentOfValue() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpGainRandomPercentOfValue newInstance() {
        HpGainRandomPercentOfValue re;
        try {
            re = (HpGainRandomPercentOfValue)HpGainRandomPercentOfValue.m_staticPool.borrowObject();
            re.m_pool = HpGainRandomPercentOfValue.m_staticPool;
        }
        catch (Exception e) {
            re = new HpGainRandomPercentOfValue();
            re.m_pool = null;
            re.m_isStatic = false;
            HpGainRandomPercentOfValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpGainRandomPercentOfValue : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null && this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            return;
        }
        final short level = this.getContainerLevel();
        final int nbDices = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int diceValue = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int constant = ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_value = 0;
        for (int i = 0; i < nbDices; ++i) {
            this.m_value += DiceRoll.roll(diceValue);
        }
        this.m_value += constant;
        this.m_value = ValueRounder.randomRound(this.m_target.getCharacteristic(FighterCharacteristicType.HP).max() * this.m_value / 100.0f);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpGainRandomPercentOfValue>() {
            @Override
            public HpGainRandomPercentOfValue makeObject() {
                return new HpGainRandomPercentOfValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("x dy + z", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("x", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("dy", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+ z", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
