package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class UpdateCharacMaxPercentModifier extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private final BinarSerialPart m_additionalData;
    private CharacteristicType m_charac;
    private boolean m_modifyValueOnExecute;
    private boolean m_modifyValueOnUnapply;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return UpdateCharacMaxPercentModifier.PARAMETERS_LIST_SET;
    }
    
    private UpdateCharacMaxPercentModifier() {
        super();
        this.m_additionalData = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(UpdateCharacMaxPercentModifier.this.m_modifyValueOnExecute ? 1 : 0));
                buffer.put((byte)(UpdateCharacMaxPercentModifier.this.m_modifyValueOnUnapply ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                UpdateCharacMaxPercentModifier.this.m_modifyValueOnExecute = (buffer.get() == 1);
                UpdateCharacMaxPercentModifier.this.m_modifyValueOnUnapply = (buffer.get() == 1);
            }
            
            @Override
            public int expectedSize() {
                return 2;
            }
        };
        this.m_modifyValueOnExecute = true;
        this.m_modifyValueOnUnapply = false;
        this.setTriggersToExecute();
    }
    
    public UpdateCharacMaxPercentModifier(final CharacteristicType charac) {
        super();
        this.m_additionalData = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(UpdateCharacMaxPercentModifier.this.m_modifyValueOnExecute ? 1 : 0));
                buffer.put((byte)(UpdateCharacMaxPercentModifier.this.m_modifyValueOnUnapply ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                UpdateCharacMaxPercentModifier.this.m_modifyValueOnExecute = (buffer.get() == 1);
                UpdateCharacMaxPercentModifier.this.m_modifyValueOnUnapply = (buffer.get() == 1);
            }
            
            @Override
            public int expectedSize() {
                return 2;
            }
        };
        this.m_modifyValueOnExecute = true;
        this.m_modifyValueOnUnapply = false;
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public UpdateCharacMaxPercentModifier newInstance() {
        UpdateCharacMaxPercentModifier re;
        try {
            re = (UpdateCharacMaxPercentModifier)UpdateCharacMaxPercentModifier.m_staticPool.borrowObject();
            re.m_pool = UpdateCharacMaxPercentModifier.m_staticPool;
        }
        catch (Exception e) {
            re = new UpdateCharacMaxPercentModifier();
            re.m_pool = null;
            re.m_isStatic = false;
            UpdateCharacMaxPercentModifier.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetCharacMaxDebuff : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_modifyValueOnExecute = (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
            this.m_modifyValueOnUnapply = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified();
            return;
        }
        final AbstractCharacteristic targetCharac = this.m_target.getCharacteristic(this.m_charac);
        if (targetCharac == null) {
            this.setNotified();
            return;
        }
        final int maxBefore = targetCharac.max();
        this.m_value = targetCharac.updateMaxPercentModifier(this.m_value);
        final int maxDiff = targetCharac.max() - maxBefore;
        if (this.m_modifyValueOnExecute) {
            int valueToAdd = maxDiff;
            if (targetCharac.getType() == FighterCharacteristicType.HP) {
                valueToAdd = ((-maxDiff > targetCharac.value()) ? (-(targetCharac.value() - 1)) : maxDiff);
            }
            targetCharac.add(valueToAdd);
        }
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
        if (this.m_target == null) {
            return;
        }
        final AbstractCharacteristic targetCharac = this.m_target.getCharacteristic(this.m_charac);
        if (targetCharac == null) {
            return;
        }
        if (!this.m_executed) {
            return;
        }
        final int maxBefore = targetCharac.max();
        targetCharac.updateMaxPercentModifier(-this.m_value);
        if (!this.m_modifyValueOnUnapply) {
            return;
        }
        final int maxDiff = targetCharac.max() - maxBefore;
        if (targetCharac.value() <= 0) {
            return;
        }
        int valueToSubstract = maxDiff;
        if (targetCharac.getType() == FighterCharacteristicType.HP) {
            valueToSubstract = ((maxDiff > targetCharac.value()) ? (targetCharac.value() - 1) : maxDiff);
        }
        targetCharac.substract(valueToSubstract);
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
        this.m_modifyValueOnExecute = true;
        this.m_modifyValueOnUnapply = false;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_additionalData;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<UpdateCharacMaxPercentModifier>() {
            @Override
            public UpdateCharacMaxPercentModifier makeObject() {
                return new UpdateCharacMaxPercentModifier((UpdateCharacMaxPercentModifier$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Modif valeur courante", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur en %", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Modifie la valeur courante (0 non 1 oui (defaut))", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Diminue \u00e0 la d\u00e9sapplication (0 g\u00e9n\u00e9ralement. Si vous h\u00e9sitez, demandez ! Bug \u00e0 l'horizon)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
