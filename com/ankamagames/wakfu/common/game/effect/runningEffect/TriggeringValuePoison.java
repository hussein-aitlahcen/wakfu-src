package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class TriggeringValuePoison extends WakfuRunningEffect implements ArmorLossProvider
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    private float m_floatValue;
    private int m_armorLoss;
    private int m_barrierLoss;
    private final BinarSerialPart m_additionalData;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return TriggeringValuePoison.PARAMETERS_LIST_SET;
    }
    
    public TriggeringValuePoison() {
        super();
        this.m_floatValue = 0.0f;
        this.m_additionalData = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(TriggeringValuePoison.this.m_armorLoss);
                buffer.putInt(TriggeringValuePoison.this.m_barrierLoss);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                TriggeringValuePoison.this.m_armorLoss = buffer.getInt();
                TriggeringValuePoison.this.m_barrierLoss = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public TriggeringValuePoison newInstance() {
        TriggeringValuePoison re;
        try {
            re = (TriggeringValuePoison)TriggeringValuePoison.m_staticPool.borrowObject();
            re.m_pool = TriggeringValuePoison.m_staticPool;
        }
        catch (Exception e) {
            re = new TriggeringValuePoison();
            re.m_pool = null;
            re.m_isStatic = false;
            TriggeringValuePoison.m_logger.error((Object)("Erreur lors d'un checkOut sur un TriggeringValuePoison  : " + e.getMessage()));
        }
        re.m_armorLoss = this.m_armorLoss;
        re.m_barrierLoss = this.m_barrierLoss;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.substractHp();
        }
        else {
            this.setNotified(true);
        }
    }
    
    private void substractHp() {
        final HPLoss hpLoss = new HPLoss();
        hpLoss.substractHp(this.m_value, this.m_caster, this.m_target);
        this.m_armorLoss = hpLoss.getArmorLoss();
        this.m_barrierLoss = hpLoss.getBarrierLoss();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.extractParams();
        this.extractBaseValue(triggerRE);
        this.computeValueModifications(triggerRE);
    }
    
    private void extractParams() {
        final short level = this.getContainerLevel();
        this.m_element = null;
        if (this.m_genericEffect != null) {
            byte elementId = 0;
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 3) {
                final float nominator = ((WakfuEffect)this.m_genericEffect).getParam(0, level);
                float unitySize = ((WakfuEffect)this.m_genericEffect).getParam(1, level);
                if (unitySize == 0.0f) {
                    unitySize = 1.0f;
                }
                this.m_floatValue = nominator / unitySize;
                elementId = (byte)((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM);
            }
            else if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
                this.m_floatValue = ((WakfuEffect)this.m_genericEffect).getParam(0, level);
                elementId = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
            }
            this.m_element = Elements.getElementFromId(elementId);
        }
    }
    
    protected static int defaultCondition() {
        return 15;
    }
    
    private void computeValueModifications(final RunningEffect triggerRE) {
        final HpLossComputer computer = new HpLossComputerImpl(this.m_caster, this.m_target, this.m_element, (WakfuEffect)this.m_genericEffect);
        computer.setValue(this.m_value);
        computer.setAffectedByLocalisation(this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isAffectedByLocalisation());
        computer.setConditions(defaultCondition());
        computer.computeWithModificator();
        RunningEffectUtils.setTriggerForElement(computer.getElementForResistance(), this);
        this.m_value = computer.getValue();
    }
    
    private void extractBaseValue(final RunningEffect triggerRE) {
        if (triggerRE != null) {
            this.m_value = ValueRounder.randomRound(this.m_floatValue * triggerRE.getValue());
        }
        else {
            this.m_value = 0;
            TriggeringValuePoison.m_logger.error((Object)("Unable to compute damages for a " + this.getClass().getSimpleName() + " : no triggering effect"));
        }
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        super.update(whatToUpdate, howMuchToUpate, set);
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
                    break;
                }
                this.m_value = ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
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
        this.m_value = Math.max(0, this.m_value);
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
    public Elements getElement() {
        return this.m_element;
    }
    
    @Override
    public int getArmorLoss() {
        return this.m_armorLoss;
    }
    
    @Override
    public int getBarrierLoss() {
        return this.m_barrierLoss;
    }
    
    @Override
    public void onCheckIn() {
        this.m_armorLoss = 0;
        this.m_barrierLoss = 0;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_additionalData;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<TriggeringValuePoison>() {
            @Override
            public TriggeringValuePoison makeObject() {
                return new TriggeringValuePoison();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Poison fonction de la valeur de l'effet d\u00e9clenchant", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("PdV perdus pour une unit\u00e9", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Valeur de l'unit\u00e9", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Poison fonction de la valeur de l'effet d\u00e9clenchant (unit\u00e9 = 1)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("PdV perdus pour une unit\u00e9", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
