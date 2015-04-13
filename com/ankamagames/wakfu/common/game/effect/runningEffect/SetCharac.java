package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SetCharac extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_setMax;
    private BinarSerialPart ADDITIONAL_DATA;
    private CharacteristicType m_charac;
    private boolean m_casterValue;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetCharac.PARAMETERS_LIST_SET;
    }
    
    private SetCharac() {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart(1) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(SetCharac.this.m_setMax ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetCharac.this.m_setMax = (buffer.get() == 1);
            }
        };
        this.m_casterValue = false;
    }
    
    public SetCharac(final CharacteristicType charac) {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart(1) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(SetCharac.this.m_setMax ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetCharac.this.m_setMax = (buffer.get() == 1);
            }
        };
        this.m_casterValue = false;
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public SetCharac newInstance() {
        SetCharac re;
        try {
            re = (SetCharac)SetCharac.m_staticPool.borrowObject();
            re.m_pool = SetCharac.m_staticPool;
        }
        catch (Exception e) {
            re = new SetCharac();
            re.m_pool = null;
            SetCharac.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetCharac : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    public CharacteristicType getCharacteristicType() {
        return this.m_charac;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.isValueComputationEnabled() && this.m_casterValue && this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
            this.m_value = this.m_caster.getCharacteristicValue(this.m_charac);
        }
        switch (this.m_charac.getCharacteristicType()) {
            case 0: {
                if (this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
                    final AbstractCharacteristic charac = this.m_target.getCharacteristic(this.m_charac);
                    if (this.m_setMax) {
                        charac.setMax(this.m_value);
                    }
                    charac.set(this.m_value);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        this.m_casterValue = true;
        this.m_setMax = false;
        if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            return;
        }
        this.m_casterValue = false;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_setMax = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        if (this.m_charac.getCharacteristicType() == 0 && this.m_charac == FighterCharacteristicType.CHRAGE) {
            this.m_triggers.set(2140);
        }
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
    public void unapplyOverride() {
        super.unapplyOverride();
    }
    
    @Override
    public void onCheckIn() {
        this.m_setMax = false;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATA;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetCharac>() {
            @Override
            public SetCharac makeObject() {
                return new SetCharac((SetCharac$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Set de Charac = charac du caster (a utiliser dans certains cas particulier)", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Set de Charac = valeur (a utiliser dans certains cas particulier). Max non sett\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Set de Charac = valeur (a utiliser dans certains cas particulier). Max settable", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("set max (0 = non, 1 = oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
