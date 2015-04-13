package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SetCharacToMax extends CharacGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetCharacToMax.PARAMETERS_LIST_SET;
    }
    
    private SetCharacToMax() {
        super();
    }
    
    public SetCharacToMax(final CharacteristicType charac) {
        super();
        this.m_charac = charac;
        this.m_valuePerCentOfCurrentValue = false;
        this.setTriggersToExecute();
    }
    
    @Override
    public SetCharacToMax newInstance() {
        SetCharacToMax re;
        try {
            re = (SetCharacToMax)SetCharacToMax.m_staticPool.borrowObject();
            re.m_pool = SetCharacToMax.m_staticPool;
        }
        catch (Exception e) {
            re = new SetCharacToMax();
            re.m_pool = null;
            re.m_isStatic = false;
            SetCharacToMax.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetCharacToMax : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        re.m_valuePerCentOfCurrentValue = false;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final EffectUser effectExecutionTarget = this.getEffectExecutionTarget();
        if (!effectExecutionTarget.hasCharacteristic(this.m_charac)) {
            this.m_value = 0;
            return;
        }
        final AbstractCharacteristic targetCharacteristic = effectExecutionTarget.getCharacteristic(this.m_charac);
        this.m_value = targetCharacteristic.max() - targetCharacteristic.value();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetCharacToMax>() {
            @Override
            public SetCharacToMax makeObject() {
                return new SetCharacToMax((SetCharacToMax$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[0]);
    }
}
