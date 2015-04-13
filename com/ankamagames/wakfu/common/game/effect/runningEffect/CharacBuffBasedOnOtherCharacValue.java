package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacBuffBasedOnOtherCharacValue extends CharacBuff
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_applyOnCaster;
    public BinarSerialPart OVERRIDEN_ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacBuffBasedOnOtherCharacValue.PARAMETERS_LIST_SET;
    }
    
    public CharacBuffBasedOnOtherCharacValue() {
        super();
        this.m_applyOnCaster = false;
        this.OVERRIDEN_ADDITIONNAL_DATAS = new BinarSerialPart() {
            private BinarSerialPart getParentPart() {
                return CharacBuffBasedOnOtherCharacValue.this.getAdditionalDatasBinarSerialPart();
            }
            
            @Override
            public int expectedSize() {
                return 2 + this.getParentPart().expectedSize();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                this.getParentPart().serialize(buffer);
                if (CharacBuffBasedOnOtherCharacValue.this.m_charac != null) {
                    buffer.put(CharacBuffBasedOnOtherCharacValue.this.m_charac.getId());
                }
                else {
                    buffer.put((byte)0);
                }
                buffer.put((byte)(CharacBuffBasedOnOtherCharacValue.this.m_applyOnCaster ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                this.getParentPart().unserialize(buffer, Version.SERIALIZATION_VERSION);
                CharacBuffBasedOnOtherCharacValue.this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(buffer.get());
                CharacBuffBasedOnOtherCharacValue.this.m_applyOnCaster = (buffer.get() == 1);
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacBuffBasedOnOtherCharacValue newInstance() {
        CharacBuffBasedOnOtherCharacValue re;
        try {
            re = (CharacBuffBasedOnOtherCharacValue)CharacBuffBasedOnOtherCharacValue.m_staticPool.borrowObject();
            re.m_pool = CharacBuffBasedOnOtherCharacValue.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacBuffBasedOnOtherCharacValue();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacBuffBasedOnOtherCharacValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuffBasedOnOtherCharacValue : " + e.getMessage()));
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
        final boolean useMax = ((WakfuEffect)this.m_genericEffect).getParamsCount() >= 5 && ((WakfuEffect)this.m_genericEffect).getParam(4, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 6) {
            this.m_addCurrentValue = true;
        }
        else {
            this.m_addCurrentValue = (((WakfuEffect)this.m_genericEffect).getParam(5, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (!this.getEffectExecutionTarget().hasCharacteristic(FighterCharacteristicType.getCharacteristicTypeFromId(targetedCharac)) || !this.m_target.hasCharacteristic(FighterCharacteristicType.getCharacteristicTypeFromId(referenceCharac))) {
            return;
        }
        this.m_charac = FighterCharacteristicType.getCharacteristicTypeFromId(targetedCharac);
        final int referenceValue = useMax ? this.m_target.getCharacteristic(FighterCharacteristicType.getCharacteristicTypeFromId(referenceCharac)).max() : this.m_target.getCharacteristicValue(FighterCharacteristicType.getCharacteristicTypeFromId(referenceCharac));
        this.m_value = referenceValue * percentOfReferenceCharac / 100;
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
        return this.OVERRIDEN_ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacBuffBasedOnOtherCharacValue>() {
            @Override
            public CharacBuffBasedOnOtherCharacValue makeObject() {
                return new CharacBuffBasedOnOtherCharacValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Buff de carac pour la cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac a booster", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac de Reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la Charac de reference", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Buff de carac poru la cible ou le caster, en fonction des caracs de la cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac a booster", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac de Reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la Charac de reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Buff pour le caster (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Utilise le maximum de la charac de reference", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac a booster", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac de Reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la Charac de reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Buff pour le caster (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Utilise le max (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Ajoute a la valeur courante", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Charac a booster", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac de Reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de la Charac de reference", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Buff pour le caster (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Utilise le max (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Ajoute a la valeur courante (0=non, 1=oui (defaut))", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
