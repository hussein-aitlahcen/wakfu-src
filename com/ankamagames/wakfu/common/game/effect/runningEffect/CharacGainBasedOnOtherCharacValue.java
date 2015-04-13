package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacGainBasedOnOtherCharacValue extends CharacModification
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_applyOnCaster;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacGainBasedOnOtherCharacValue.PARAMETERS_LIST_SET;
    }
    
    public CharacGainBasedOnOtherCharacValue() {
        super();
        this.m_applyOnCaster = false;
        this.ADDITIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public int expectedSize() {
                return 2;
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (CharacGainBasedOnOtherCharacValue.this.m_charac != null) {
                    buffer.put(CharacGainBasedOnOtherCharacValue.this.m_charac.getId());
                }
                else {
                    buffer.put((byte)0);
                }
                buffer.put((byte)(CharacGainBasedOnOtherCharacValue.this.m_applyOnCaster ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacGainBasedOnOtherCharacValue.this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(buffer.get());
                CharacGainBasedOnOtherCharacValue.this.m_applyOnCaster = (buffer.get() == 1);
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacGainBasedOnOtherCharacValue newInstance() {
        CharacGainBasedOnOtherCharacValue re;
        try {
            re = (CharacGainBasedOnOtherCharacValue)CharacGainBasedOnOtherCharacValue.m_staticPool.borrowObject();
            re.m_pool = CharacGainBasedOnOtherCharacValue.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacGainBasedOnOtherCharacValue();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacGainBasedOnOtherCharacValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGainBasedOnOtherCharacValue : " + e.getMessage()));
        }
        re.m_applyOnCaster = false;
        return re;
    }
    
    @Override
    protected EffectUser getEffectExecutionTarget() {
        return this.m_applyOnCaster ? this.m_caster : this.m_target;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 3) {
            return;
        }
        if (this.getEffectExecutionTarget() == null || this.m_target == null) {
            return;
        }
        final byte targetedCharac = (byte)((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final byte referenceCharac = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int percentOfReferenceCharac = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_applyOnCaster = (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4 && ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        if (!this.getEffectExecutionTarget().hasCharacteristic(FighterCharacteristicType.getCharacteristicTypeFromId(targetedCharac)) || !this.m_target.hasCharacteristic(FighterCharacteristicType.getCharacteristicTypeFromId(referenceCharac))) {
            return;
        }
        this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(targetedCharac);
        this.m_value = this.m_target.getCharacteristicValue(FighterCharacteristicType.getCharacteristicTypeFromId(referenceCharac)) * percentOfReferenceCharac / 100;
    }
    
    @Override
    void rollbackCharacModification() {
        this.getEffectExecutionTarget().getCharacteristic(this.m_charac).substract(this.m_value);
    }
    
    @Override
    void applyValueModification(final AbstractCharacteristic charac) {
        this.m_value = charac.add(this.m_value);
    }
    
    @Override
    public final Elements getElement() {
        if (this.m_genericEffect == null) {
            return null;
        }
        if (this.m_charac == null) {
            final byte targetedCharac = (byte)((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(targetedCharac);
        }
        if (this.m_charac.getCharacteristicType() == 0) {
            final Elements element = ((FighterCharacteristicType)this.m_charac).getRelatedElement();
            if (element != null) {
                return element;
            }
        }
        return Elements.PHYSICAL;
    }
    
    @Override
    public void onCheckIn() {
        this.m_charac = null;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacGainBasedOnOtherCharacValue>() {
            @Override
            public CharacGainBasedOnOtherCharacValue makeObject() {
                return new CharacGainBasedOnOtherCharacValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Gain de carac pour la cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac a booster", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac de Reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la Charac de reference", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Gain de carac poru la cible ou le caster, en fonction des caracs de la cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac a booster", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac de Reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la Charac de reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Gain pour le caster (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
