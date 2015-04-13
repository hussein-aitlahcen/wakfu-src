package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPLeech extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_lifestolen;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPLeech.PARAMETERS_LIST_SET;
    }
    
    private HPLeech() {
        super();
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(HPLeech.this.m_lifestolen);
                buffer.putInt(HPLeech.this.m_armorLoss);
                buffer.putInt(HPLeech.this.m_barrierLoss);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                HPLeech.this.m_lifestolen = buffer.getInt();
                HPLeech.this.m_armorLoss = buffer.getInt();
                HPLeech.this.m_barrierLoss = buffer.getInt();
            }
            
            @Override
            public int expectedSize() {
                return 12;
            }
        };
    }
    
    public HPLeech(final Elements element, final ComputeMode mode) {
        super(element, mode);
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(HPLeech.this.m_lifestolen);
                buffer.putInt(HPLeech.this.m_armorLoss);
                buffer.putInt(HPLeech.this.m_barrierLoss);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                HPLeech.this.m_lifestolen = buffer.getInt();
                HPLeech.this.m_armorLoss = buffer.getInt();
                HPLeech.this.m_barrierLoss = buffer.getInt();
            }
            
            @Override
            public int expectedSize() {
                return 12;
            }
        };
        this.m_isStatic = true;
    }
    
    @Override
    public HPLeech newInstance() {
        HPLeech re;
        try {
            re = (HPLeech)HPLeech.m_staticPool.borrowObject();
            re.m_pool = HPLeech.m_staticPool;
        }
        catch (Exception e) {
            re = new HPLeech();
            re.m_pool = null;
            re.m_isStatic = false;
            HPLeech.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPLeech : " + e.getMessage()));
        }
        re.m_staticElement = this.m_staticElement;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(20);
        final Elements element = this.getElement();
        if (element != null) {
            switch (element) {
                case EARTH: {
                    this.m_triggers.set(24);
                    break;
                }
                case FIRE: {
                    this.m_triggers.set(21);
                    break;
                }
                case WATER: {
                    this.m_triggers.set(22);
                    break;
                }
                case AIR: {
                    this.m_triggers.set(23);
                    break;
                }
                case STASIS: {
                    this.m_triggers.set(25);
                    break;
                }
            }
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_caster == this.m_target) {
            this.setNotified();
            return;
        }
        super.executeOverride(linkedRE, trigger);
        if (!this.isNotified()) {
            this.notifyExecution(linkedRE, trigger);
        }
        if (this.isValueComputationEnabled() && this.m_caster != null && this.m_caster.hasCharacteristic(FighterCharacteristicType.HP)) {
            final HPGain hpGain = HPGain.checkOut((EffectContext<WakfuEffect>)this.m_context, this.getElement());
            hpGain.setTarget(this.m_caster);
            hpGain.forceValue(this.m_lifestolen);
            hpGain.setParent(this);
            hpGain.setCaster(this.m_caster);
            ((RunningEffect<DefaultEffect, EC>)hpGain).setGenericEffect(DefaultEffect.getInstance());
            hpGain.askForExecution();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_lifestolen = 0;
        final short level = this.getContainerLevel();
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            HPLeech.m_logger.error((Object)"[Effects] On a essay\u00e9 de lancer un vol de vie sans cible avec des hps");
            this.setNotified(true);
            return;
        }
        float lifeStolenRate = 0.0f;
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            lifeStolenRate = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.LIFE_STOLEN_BONUS)) {
                lifeStolenRate += this.m_caster.getCharacteristicValue(FighterCharacteristicType.LIFE_STOLEN_BONUS);
            }
        }
        this.defaultCondition();
        this.computeModificatorWithDefaults();
        final int min = (this.m_value < this.m_target.getCharacteristicValue(FighterCharacteristicType.HP)) ? this.m_value : this.m_target.getCharacteristicValue(FighterCharacteristicType.HP);
        this.m_lifestolen = ValueRounder.randomRound(lifeStolenRate / 100.0f * min);
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    public int getLifeStolen() {
        return this.m_lifestolen;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATAS;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_lifestolen = 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HPLeech>() {
            @Override
            public HPLeech makeObject() {
                return new HPLeech(null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("vol de PdV", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de l'attaque", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("pourcentage vol\u00e9", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
