package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPExchangePercentOfValue extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private float m_percent;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPExchangePercentOfValue.PARAMETERS_LIST_SET;
    }
    
    public HPExchangePercentOfValue() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HPExchangePercentOfValue newInstance() {
        HPExchangePercentOfValue re;
        try {
            re = (HPExchangePercentOfValue)HPExchangePercentOfValue.m_staticPool.borrowObject();
            re.m_pool = HPExchangePercentOfValue.m_staticPool;
        }
        catch (Exception e) {
            re = new HPExchangePercentOfValue();
            re.m_pool = null;
            re.m_isStatic = false;
            HPExchangePercentOfValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(1);
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        super.update(whatToUpdate, howMuchToUpate, set);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.isValueComputationEnabled() && this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            final AbstractCharacteristic charac = this.m_caster.getCharacteristic(FighterCharacteristicType.HP);
            this.m_value = ValueRounder.randomRound(charac.max() * this.m_percent / 100.0f);
            final int val = charac.value() - this.m_value;
            this.m_value += Math.min(val, charac.min());
            final AbstractCharacteristic charactarget = this.m_target.getCharacteristic(FighterCharacteristicType.HP);
            final int val2 = charactarget.value() + this.m_value;
            this.m_value = ((val2 < charactarget.max()) ? val2 : charactarget.max()) - charactarget.value();
            HPLoss re2 = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, Elements.PHYSICAL, HPLoss.ComputeMode.CLASSIC, this.m_value, this.m_caster);
            re2.setTarget(this.m_caster);
            re2.forceValue(this.m_value);
            re2.execute(null, false);
            re2 = HPGain.checkOut((EffectContext<WakfuEffect>)this.m_context, Elements.PHYSICAL);
            re2.setTarget(this.m_target);
            re2.forceValue(this.m_value);
            re2.execute(null, false);
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_percent = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
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
        m_staticPool = new MonitoredPool(new ObjectFactory<HPExchangePercentOfValue>() {
            @Override
            public HPExchangePercentOfValue makeObject() {
                return new HPExchangePercentOfValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("don de PV", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
