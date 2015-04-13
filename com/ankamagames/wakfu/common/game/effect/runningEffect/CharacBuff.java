package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacBuff extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected CharacteristicType m_charac;
    protected boolean m_addCurrentValue;
    protected boolean m_substractCurrentValue;
    protected int m_valueToAdd;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacBuff.PARAMETERS_LIST_SET;
    }
    
    protected CharacBuff() {
        super();
        this.m_addCurrentValue = true;
        this.m_substractCurrentValue = true;
        this.m_valueToAdd = 0;
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacBuff.this.m_addCurrentValue ? 1 : 0));
                buffer.put((byte)(CharacBuff.this.m_substractCurrentValue ? 1 : 0));
                buffer.putInt(CharacBuff.this.m_valueToAdd);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacBuff.this.m_addCurrentValue = (buffer.get() == 1);
                CharacBuff.this.m_substractCurrentValue = (buffer.get() == 1);
                CharacBuff.this.m_valueToAdd = buffer.getInt();
            }
            
            @Override
            public int expectedSize() {
                return 6;
            }
        };
    }
    
    public CharacBuff(final CharacteristicType charac) {
        super();
        this.m_addCurrentValue = true;
        this.m_substractCurrentValue = true;
        this.m_valueToAdd = 0;
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacBuff.this.m_addCurrentValue ? 1 : 0));
                buffer.put((byte)(CharacBuff.this.m_substractCurrentValue ? 1 : 0));
                buffer.putInt(CharacBuff.this.m_valueToAdd);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacBuff.this.m_addCurrentValue = (buffer.get() == 1);
                CharacBuff.this.m_substractCurrentValue = (buffer.get() == 1);
                CharacBuff.this.m_valueToAdd = buffer.getInt();
            }
            
            @Override
            public int expectedSize() {
                return 6;
            }
        };
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacBuff newInstance() {
        CharacBuff re;
        try {
            re = (CharacBuff)CharacBuff.m_staticPool.borrowObject();
            re.m_pool = CharacBuff.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacBuff();
            re.m_isStatic = false;
            re.m_pool = null;
            CharacBuff.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuff : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
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
                    if (charac.hasBuffTrigger()) {
                        this.m_triggers.set(charac.getBuffTrigger());
                    }
                    break;
                }
                case 1: {
                    switch ((ItemCharacteristicType)this.m_charac) {
                        case DURABILITY: {
                            this.m_triggers.set(3);
                            break Label_0116;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.getEffectExecutionTarget() != null && this.getEffectExecutionTarget().hasCharacteristic(this.m_charac)) {
            final AbstractCharacteristic charac = this.getEffectExecutionTarget().getCharacteristic(this.m_charac);
            if (this.m_value == -1 && linkedRE != null) {
                this.m_value = linkedRE.getValue();
            }
            final int currentValue = charac.plainMax();
            final int previousMax = charac.max();
            final int outFightMax = charac.getType().getOutFightMax();
            this.setValueToBoundedMax(currentValue, outFightMax);
            charac.updateMaxValue(this.m_value);
            final int newMax = charac.max();
            if (this.isValueComputationEnabled()) {
                this.m_value = charac.plainMax() - currentValue;
                this.m_valueToAdd = newMax - previousMax;
            }
            if (this.m_addCurrentValue && this.m_valueToAdd != 0) {
                final int characUpdate = this.getEffectExecutionTarget().getCharacteristic(this.m_charac).add(this.m_valueToAdd);
                if (characUpdate != 0 && this.m_charac.getCharacteristicType() == 0) {
                    final FighterCharacteristicType fighterCharac = (FighterCharacteristicType)this.m_charac;
                    if (fighterCharac.hasBuffTrigger()) {
                        this.m_triggers.set(fighterCharac.getBuffTrigger());
                    }
                }
                this.m_executed = true;
            }
            if (this.m_charac == FighterCharacteristicType.INIT && this.m_context.getTimeline() != null) {
                this.m_context.getTimeline().updateDynamicOrder();
            }
        }
        else {
            this.setNotified(true);
        }
    }
    
    private void setValueToBoundedMax(final int currentValue, final int outFightMax) {
        if (!this.isValueComputationEnabled() || outFightMax == -1) {
            return;
        }
        if (((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer() == null) {
            return;
        }
        if (this.hasProperty(RunningEffectPropertyType.BUFF_BYPASS_OUT_FIGHT_MAX)) {
            return;
        }
        this.m_value = Math.min(outFightMax - currentValue, this.m_value);
    }
    
    public void setAddCurrentValue(final boolean addCurrentValue) {
        this.m_addCurrentValue = addCurrentValue;
    }
    
    public void setSubstractCurrentValue(final boolean substractCurrentValue) {
        this.m_substractCurrentValue = substractCurrentValue;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        this.m_addCurrentValue = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM) != 0);
        this.m_substractCurrentValue = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM) != 0);
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.m_value == -2 && this.getParams() != null && ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect() != null && ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect().getValue() > 0) {
            this.m_value = ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect().getValue() * 2;
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
    
    protected EffectUser getEffectExecutionTarget() {
        return this.m_target;
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed) {
            if (this.getEffectExecutionTarget() != null && this.getEffectExecutionTarget().hasCharacteristic(this.m_charac)) {
                int valueToSubstract = this.m_value;
                if (this.m_charac == FighterCharacteristicType.HP) {
                    final int hp = this.getEffectExecutionTarget().getCharacteristicValue(this.m_charac);
                    valueToSubstract = ((this.m_valueToAdd > hp) ? (hp - 1) : this.m_valueToAdd);
                }
                if (this.m_substractCurrentValue) {
                    this.getEffectExecutionTarget().getCharacteristic(this.m_charac).substract(valueToSubstract);
                }
                this.getEffectExecutionTarget().getCharacteristic(this.m_charac).updateMaxValue(-this.m_value);
            }
            if (this.m_charac == FighterCharacteristicType.INIT && this.m_context.getTimeline() != null) {
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
        return this.ADDITIONAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacBuff>() {
            @Override
            public CharacBuff makeObject() {
                return new CharacBuff();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Buff de charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur (-1 = valeur de l'effet declencheur, -2 = 2*la valeur de l'effet d\u00e9clencheur)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Augmente la valeur courante (0 non 1 oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Diminue \u00e0 la d\u00e9sapplication (0 g\u00e9n\u00e9ralement. Si vous h\u00e9sitez, demandez ! Bug \u00e0 l'horizon)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
