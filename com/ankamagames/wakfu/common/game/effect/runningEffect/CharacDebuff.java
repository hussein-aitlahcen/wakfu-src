package com.ankamagames.wakfu.common.game.effect.runningEffect;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacDebuff extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected CharacteristicType m_charac;
    protected boolean m_raiseCurrentValueOnUnapplication;
    private boolean m_decreaseCurrentValueOnApplication;
    private final BitSet m_executionnaltriggers;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacDebuff.PARAMETERS_LIST_SET;
    }
    
    protected CharacDebuff() {
        super();
        this.m_raiseCurrentValueOnUnapplication = true;
        this.m_executionnaltriggers = new BitSet();
        this.ADDITIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 2;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacDebuff.this.m_raiseCurrentValueOnUnapplication ? 1 : 0));
                buffer.put((byte)(CharacDebuff.this.m_decreaseCurrentValueOnApplication ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacDebuff.this.m_raiseCurrentValueOnUnapplication = (buffer.get() == 1);
                CharacDebuff.this.m_decreaseCurrentValueOnApplication = (buffer.get() == 1);
            }
        };
    }
    
    public CharacDebuff(final CharacteristicType charac) {
        super();
        this.m_raiseCurrentValueOnUnapplication = true;
        this.m_executionnaltriggers = new BitSet();
        this.ADDITIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 2;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacDebuff.this.m_raiseCurrentValueOnUnapplication ? 1 : 0));
                buffer.put((byte)(CharacDebuff.this.m_decreaseCurrentValueOnApplication ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacDebuff.this.m_raiseCurrentValueOnUnapplication = (buffer.get() == 1);
                CharacDebuff.this.m_decreaseCurrentValueOnApplication = (buffer.get() == 1);
            }
        };
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacDebuff newInstance() {
        CharacDebuff wre;
        try {
            wre = (CharacDebuff)CharacDebuff.m_staticPool.borrowObject();
            wre.m_pool = CharacDebuff.m_staticPool;
        }
        catch (Exception e) {
            wre = new CharacDebuff();
            wre.m_pool = null;
            CharacDebuff.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacDebuff : " + e.getMessage()));
        }
        wre.m_charac = this.m_charac;
        wre.m_raiseCurrentValueOnUnapplication = this.m_raiseCurrentValueOnUnapplication;
        wre.m_decreaseCurrentValueOnApplication = this.m_decreaseCurrentValueOnApplication;
        wre.m_executionnaltriggers.clear();
        wre.m_executionnaltriggers.or(super.getTriggersToExecute());
        return wre;
    }
    
    @Override
    public void release() {
        super.release();
    }
    
    @Override
    public BitSet getTriggersToExecute() {
        return this.m_executionnaltriggers;
    }
    
    public CharacteristicType getCharacteristicType() {
        return this.m_charac;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        if (this.m_charac == null) {
            return;
        }
        Label_0116: {
            switch (this.m_charac.getCharacteristicType()) {
                case 0: {
                    final FighterCharacteristicType charac = (FighterCharacteristicType)this.m_charac;
                    if (charac.hasDebuffTrigger()) {
                        this.m_triggers.set(charac.getDebuffTrigger());
                    }
                    break;
                }
                case 1: {
                    switch ((ItemCharacteristicType)this.m_charac) {
                        case DURABILITY: {
                            this.m_triggers.set(4);
                            break Label_0116;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean canBeExecuted() {
        if (this.isValueComputationEnabled()) {
            final int applicationProbability = this.getApplicationProbability();
            if (applicationProbability < 100) {
                final int roll = DiceRoll.roll(100);
                if (applicationProbability < roll) {
                    this.m_value = 0;
                    return false;
                }
            }
        }
        return super.canBeExecuted();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.getExecutionStatus() > 0) {
            return;
        }
        if (this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
            final byte characId = this.m_charac.getId();
            if (this.m_value == -1 && linkedRE != null) {
                this.m_value = linkedRE.getValue();
            }
            this.checkHpDebuffToAvoidPrematureDeath(characId);
            this.setCharacToNewMax();
            if (this.isValueComputationEnabled() && EffectProbabilityModificators.isEffectConcerned(this)) {
                this.notifyExecution(linkedRE, trigger);
                EffectProbabilityModificators.applyProbabilityManagementState(this);
            }
        }
        else {
            this.setNotified(true);
        }
    }
    
    private int getApplicationProbability() {
        int initialProbability;
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            initialProbability = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.RANDOM);
        }
        else {
            initialProbability = 100;
        }
        if (initialProbability < 0) {
            return 100;
        }
        if (!EffectProbabilityModificators.isEffectConcerned(this)) {
            return initialProbability;
        }
        final int probability = initialProbability + EffectProbabilityModificators.getProbabilityModif(this);
        return MathHelper.clamp(probability, 0, 100);
    }
    
    private void checkHpDebuffToAvoidPrematureDeath(final byte characId) {
        if (this.isValueComputationEnabled() && characId == FighterCharacteristicType.HP.getId() && ((RunningEffect<WakfuEffect, EC>)this).getGenericEffect() != null && ((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().getEffectType() != 2) {
            final AbstractCharacteristic characteristic = this.m_target.getCharacteristic(this.m_charac);
            final int diff = characteristic.max() - this.m_value;
            if (diff <= 0) {
                this.m_value += diff - 1;
            }
        }
    }
    
    private void setCharacToNewMax() {
        final AbstractCharacteristic characteristic = this.m_target.getCharacteristic(this.m_charac);
        final int valueBeforeMaxUpdate = characteristic.value();
        if (characteristic.max() != Integer.MAX_VALUE) {
            characteristic.updateMinValue(-this.m_value);
            characteristic.updateMaxValue(-this.m_value);
        }
        int valueAfterMaxUpdate = characteristic.value();
        if (this.m_decreaseCurrentValueOnApplication) {
            final int remainingToRemove = this.m_value - (valueBeforeMaxUpdate - valueAfterMaxUpdate);
            characteristic.substract(remainingToRemove);
            valueAfterMaxUpdate = characteristic.value();
        }
        if (this.m_charac.getCharacteristicType() == 0) {
            final FighterCharacteristicType charac = (FighterCharacteristicType)this.m_charac;
            if (valueAfterMaxUpdate != valueBeforeMaxUpdate && charac.hasLossTrigger()) {
                this.m_executionnaltriggers.set(charac.getLossTrigger());
            }
            if (charac.hasDebuffTrigger()) {
                if (this.m_value != 0) {
                    this.m_executionnaltriggers.set(charac.getDebuffTrigger());
                }
                else {
                    this.m_triggers.clear(charac.getDebuffTrigger());
                }
            }
        }
        if (this.m_charac == FighterCharacteristicType.INIT && this.m_context != null && this.m_context.getTimeline() != null) {
            this.m_context.getTimeline().updateDynamicOrder();
        }
    }
    
    private void setCancelTriggers(final byte characId) {
        this.setExecutionStatus((byte)3);
        if (characId == FighterCharacteristicType.AP.getId()) {
            this.m_executionnaltriggers.set(58);
            this.m_executionnaltriggers.clear(54);
        }
        else if (characId == FighterCharacteristicType.MP.getId()) {
            this.m_executionnaltriggers.set(75);
            this.m_executionnaltriggers.clear(64);
        }
        this.m_value = 0;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_raiseCurrentValueOnUnapplication = true;
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 2) {
            this.m_raiseCurrentValueOnUnapplication = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 3) {
            this.m_decreaseCurrentValueOnApplication = (((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (this.m_value == -1 && triggerRE != null) {
            this.m_value = triggerRE.getValue();
        }
        else if (this.m_value == -2 && this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
            this.m_value = this.m_target.getCharacteristicValue(this.m_charac);
        }
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
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
        if (this.m_value == 0) {
            this.setNotified();
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed && this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
            this.m_target.getCharacteristic(this.m_charac).updateMaxValue(this.m_value);
            this.m_target.getCharacteristic(this.m_charac).updateMinValue(this.m_value);
            if (this.m_raiseCurrentValueOnUnapplication) {
                this.m_target.getCharacteristic(this.m_charac).add(this.m_value);
            }
            if (this.m_charac == FighterCharacteristicType.INIT && this.m_context != null && this.m_context.getTimeline() != null) {
                this.m_context.getTimeline().updateDynamicOrder();
            }
        }
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
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacDebuff>() {
            @Override
            public CharacDebuff makeObject() {
                return new CharacDebuff();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Debuff de Charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1 valeur du trigger, -2 vole la totalit\u00e9)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Debuff de Charac avec proba d'application", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1 valeur du trigger, -2 vole la totalit\u00e9) ", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Proba d'application en % (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Debuff de charac param\u00e9tr\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur (-1 valeur du trigger, -2 vole la totalit\u00e9) ", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Proba d'application en % (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Augmente la valeur courante \u00e0 la d\u00e9sapplication (0 non / 1 oui default", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Diminue la valeur lors de l'application", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur (-1 valeur du trigger, -2 vole la totalit\u00e9) ", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Proba d'application en % (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Augmente la valeur courante \u00e0 la d\u00e9sapplication (0 non / 1 oui default", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Diminue la valeur courante \u00e0 l'application (0 non (default) / 1 oui ", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
