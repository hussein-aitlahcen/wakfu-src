package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPGainPercentOfValue extends HPGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_useTriggeringEffect;
    private float m_percent;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPGainPercentOfValue.PARAMETERS_LIST_SET;
    }
    
    public HPGainPercentOfValue() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HPGainPercentOfValue newInstance() {
        HPGainPercentOfValue re;
        try {
            re = (HPGainPercentOfValue)HPGainPercentOfValue.m_staticPool.borrowObject();
            re.m_pool = HPGainPercentOfValue.m_staticPool;
        }
        catch (Exception e) {
            re = new HPGainPercentOfValue();
            re.m_pool = null;
            re.m_isStatic = false;
            HPGainPercentOfValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPGain : " + e.getMessage()));
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
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += (int)(this.m_value * howMuchToUpate / 100.0f);
                    break;
                }
                break;
            }
            case 1: {
                if (set) {
                    this.m_value = ValueRounder.randomRound(howMuchToUpate);
                    break;
                }
                this.m_value += (int)howMuchToUpate;
                break;
            }
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE != null && this.m_useTriggeringEffect) {
            this.m_value = Math.max(1, Math.round(linkedRE.getValue() * this.m_percent / 100.0f));
        }
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            this.m_value = 0;
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_percent = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_useTriggeringEffect = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        else {
            this.m_useTriggeringEffect = true;
        }
        boolean functionOfCaster = false;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            functionOfCaster = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        boolean useVirtualHp = false;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            useVirtualHp = (((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        final EffectUser reference = functionOfCaster ? this.m_caster : this.m_target;
        if (reference != null && reference.hasCharacteristic(FighterCharacteristicType.HP)) {
            int hpMax = reference.getCharacteristic(FighterCharacteristicType.HP).max();
            if (useVirtualHp) {
                hpMax += reference.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP).max();
            }
            this.m_value = ValueRounder.randomRound(hpMax * this.m_percent / 100.0f);
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HPGainPercentOfValue>() {
            @Override
            public HPGainPercentOfValue makeObject() {
                return new HPGainPercentOfValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("gain de PV", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur max ou de l'effet declencheur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Bas\u00e9 sur la valeur de l'effet declencheur ou non", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur max", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Valeur de l'effet declencheur (0:non, 1:oui(defaut))", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Soin en fonction du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur max", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Valeur de l'effet declencheur (0:non, 1:oui(defaut))", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Fonction du caster (0:non (defaut), 1:oui)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Prend en compte les pv virtuels", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur max", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Valeur de l'effet declencheur (0:non, 1:oui(defaut))", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Fonction du caster (0:non (defaut), 1:oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Prend en compte les pv virtuels (0:non (defaut), 1:oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
