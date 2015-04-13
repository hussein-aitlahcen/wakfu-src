package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class ApAsMp extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private ApAsMpCharacteristicUpdateListener m_listener;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApAsMp.PARAMETERS_LIST_SET;
    }
    
    public ApAsMp() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ApAsMp newInstance() {
        ApAsMp re;
        try {
            re = (ApAsMp)ApAsMp.m_staticPool.borrowObject();
            re.m_pool = ApAsMp.m_staticPool;
        }
        catch (Exception e) {
            re = new ApAsMp();
            re.m_pool = null;
            re.m_isStatic = false;
            ApAsMp.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApAsMp : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.AP) || !this.m_target.hasCharacteristic(FighterCharacteristicType.MP)) {
            return;
        }
        final AbstractCharacteristic ap = this.m_target.getCharacteristic(FighterCharacteristicType.AP);
        this.m_value = ap.max();
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.AP) || !this.m_target.hasCharacteristic(FighterCharacteristicType.MP)) {
            this.setNotified();
            return;
        }
        final AbstractCharacteristic ap = this.m_target.getCharacteristic(FighterCharacteristicType.AP);
        final AbstractCharacteristic mp = this.m_target.getCharacteristic(FighterCharacteristicType.MP);
        final int apCurrentValue = ap.value();
        mp.setMax(mp.max() + this.m_value);
        mp.add(apCurrentValue);
        this.m_listener = new ApAsMpCharacteristicUpdateListener();
        this.m_listener.m_previousMax = ap.max();
        this.m_listener.m_previousValue = ap.value();
        this.m_listener.m_target = this.m_target;
        ap.addListener(this.m_listener);
        this.m_target.addProperty(FightPropertyType.AP_AS_MP);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.AP) || !this.m_target.hasCharacteristic(FighterCharacteristicType.MP)) {
            return;
        }
        final AbstractCharacteristic ap = this.m_target.getCharacteristic(FighterCharacteristicType.AP);
        final AbstractCharacteristic mp = this.m_target.getCharacteristic(FighterCharacteristicType.MP);
        ap.removeListener(this.m_listener);
        final int listenerValue = (this.m_listener == null) ? 0 : this.m_listener.m_maxDiff;
        mp.setMax(mp.max() - this.m_value - listenerValue);
        mp.toMax();
        this.m_target.removeProperty(FightPropertyType.AP_AS_MP);
        super.unapplyOverride();
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
        m_staticPool = new MonitoredPool(new ObjectFactory<ApAsMp>() {
            @Override
            public ApAsMp makeObject() {
                return new ApAsMp();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default", new WakfuRunningEffectParameter[0]) });
    }
    
    private static class ApAsMpCharacteristicUpdateListener implements CharacteristicUpdateListener
    {
        int m_previousMax;
        int m_previousValue;
        int m_maxDiff;
        EffectUser m_target;
        
        private ApAsMpCharacteristicUpdateListener() {
            super();
            this.m_previousMax = 0;
            this.m_previousValue = 0;
            this.m_maxDiff = 0;
        }
        
        @Override
        public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
            final int max = charac.max();
            final int value = charac.value();
            final int maxDiff = max - this.m_previousMax;
            final int valueDiff = value - this.m_previousValue;
            this.m_maxDiff += maxDiff;
            this.m_previousMax = max;
            this.m_previousValue = value;
            final AbstractCharacteristic mp = this.m_target.getCharacteristic(FighterCharacteristicType.MP);
            mp.set(mp.value() + valueDiff);
            mp.setMax(mp.max() + maxDiff);
        }
    }
}
