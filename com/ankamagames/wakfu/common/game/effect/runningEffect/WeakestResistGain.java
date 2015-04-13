package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class WeakestResistGain extends CharacGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return WeakestResistGain.PARAMETERS_LIST_SET;
    }
    
    public WeakestResistGain() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public WeakestResistGain newInstance() {
        WeakestResistGain re;
        try {
            re = (WeakestResistGain)WeakestResistGain.m_staticPool.borrowObject();
            re.m_pool = WeakestResistGain.m_staticPool;
        }
        catch (Exception e) {
            re = new WeakestResistGain();
            re.m_pool = null;
            re.m_isStatic = false;
            WeakestResistGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un WeakestResistGain : " + e.getMessage()));
        }
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
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final Elements weakestResistElement = RunningEffectUtils.getTargetWeakestResistElement(this);
        if (weakestResistElement == null) {
            this.setNotified();
            return;
        }
        this.m_charac = weakestResistElement.getResistanceBonusCharacteristic();
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    public void onCheckIn() {
        this.m_charac = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<WeakestResistGain>() {
            @Override
            public WeakestResistGain makeObject() {
                return new WeakestResistGain();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur du gain", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
