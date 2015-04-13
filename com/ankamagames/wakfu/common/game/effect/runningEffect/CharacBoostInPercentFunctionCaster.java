package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacBoostInPercentFunctionCaster extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private FighterCharacteristicType m_charac;
    private boolean m_addCurrentValue;
    private boolean m_substractCurrentValue;
    private int m_valueToAdd;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacBoostInPercentFunctionCaster.PARAMETERS_LIST_SET;
    }
    
    private CharacBoostInPercentFunctionCaster() {
        super();
        this.m_addCurrentValue = true;
        this.m_substractCurrentValue = true;
        this.m_valueToAdd = 0;
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacBoostInPercentFunctionCaster.this.m_addCurrentValue ? 1 : 0));
                buffer.put((byte)(CharacBoostInPercentFunctionCaster.this.m_substractCurrentValue ? 1 : 0));
                buffer.putInt(CharacBoostInPercentFunctionCaster.this.m_valueToAdd);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacBoostInPercentFunctionCaster.this.m_addCurrentValue = (buffer.get() == 1);
                CharacBoostInPercentFunctionCaster.this.m_substractCurrentValue = (buffer.get() == 1);
                CharacBoostInPercentFunctionCaster.this.m_valueToAdd = buffer.getInt();
            }
            
            @Override
            public int expectedSize() {
                return 6;
            }
        };
        this.setTriggersToExecute();
    }
    
    public CharacBoostInPercentFunctionCaster(final FighterCharacteristicType charac) {
        super();
        this.m_addCurrentValue = true;
        this.m_substractCurrentValue = true;
        this.m_valueToAdd = 0;
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(CharacBoostInPercentFunctionCaster.this.m_addCurrentValue ? 1 : 0));
                buffer.put((byte)(CharacBoostInPercentFunctionCaster.this.m_substractCurrentValue ? 1 : 0));
                buffer.putInt(CharacBoostInPercentFunctionCaster.this.m_valueToAdd);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacBoostInPercentFunctionCaster.this.m_addCurrentValue = (buffer.get() == 1);
                CharacBoostInPercentFunctionCaster.this.m_substractCurrentValue = (buffer.get() == 1);
                CharacBoostInPercentFunctionCaster.this.m_valueToAdd = buffer.getInt();
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
    public CharacBoostInPercentFunctionCaster newInstance() {
        CharacBoostInPercentFunctionCaster re;
        try {
            re = (CharacBoostInPercentFunctionCaster)CharacBoostInPercentFunctionCaster.m_staticPool.borrowObject();
            re.m_pool = CharacBoostInPercentFunctionCaster.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacBoostInPercentFunctionCaster();
            re.m_isStatic = false;
            re.m_pool = null;
            CharacBoostInPercentFunctionCaster.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuff : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(3);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null || !this.m_target.hasCharacteristic(this.m_charac)) {
            this.setNotified(true);
            return;
        }
        if (this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
            final AbstractCharacteristic charac = this.m_target.getCharacteristic(this.m_charac);
            final int currentPlainMax = charac.plainMax();
            final int currentMax = charac.max();
            charac.updateMaxValue(this.m_value);
            if (this.isValueComputationEnabled()) {
                this.m_value = charac.plainMax() - currentPlainMax;
                this.m_valueToAdd = charac.max() - currentMax;
            }
            if (this.m_addCurrentValue) {
                charac.add(this.m_valueToAdd);
                this.m_executed = true;
            }
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        final int percent = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        if (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM) == 0) {
            this.m_addCurrentValue = false;
        }
        else {
            this.m_addCurrentValue = true;
        }
        if (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM) == 0) {
            this.m_substractCurrentValue = false;
        }
        else {
            this.m_substractCurrentValue = true;
        }
        if (this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
            this.m_value = percent * this.m_caster.getCharacteristic(this.m_charac).max() / 100;
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed && this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
            final AbstractCharacteristic charac = this.m_target.getCharacteristic(this.m_charac);
            if (charac.value() > 0) {
                charac.substract((this.m_valueToAdd > charac.value()) ? (charac.value() - 1) : this.m_valueToAdd);
            }
            charac.updateMaxValue(-this.m_value);
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
    public void onCheckIn() {
        super.onCheckIn();
        this.m_valueToAdd = 0;
        this.m_addCurrentValue = true;
        this.m_substractCurrentValue = true;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacBoostInPercentFunctionCaster>() {
            @Override
            public CharacBoostInPercentFunctionCaster makeObject() {
                return new CharacBoostInPercentFunctionCaster((CharacBoostInPercentFunctionCaster$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("En fonction du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur en %", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Augmente la valeur courante (0 non 1 oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Diminue \u00e0 la d\u00e9sapplication (0 g\u00e9n\u00e9ralement. Si vous h\u00e9sitez, demandez ! Bug \u00e0 l'horizon)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
