package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CopyCharacAtApplicationValue extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private final BinarSerialPart m_additionnalData;
    private byte m_characId;
    private int m_max;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CopyCharacAtApplicationValue.PARAMETERS_LIST_SET;
    }
    
    public CopyCharacAtApplicationValue() {
        super();
        this.m_additionnalData = new BinarSerialPart(5) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put(CopyCharacAtApplicationValue.this.m_characId);
                buffer.putInt(CopyCharacAtApplicationValue.this.m_max);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CopyCharacAtApplicationValue.this.m_characId = buffer.get();
                CopyCharacAtApplicationValue.this.m_max = buffer.getInt();
            }
        };
        this.m_characId = -1;
        this.setTriggersToExecute();
    }
    
    @Override
    public CopyCharacAtApplicationValue newInstance() {
        CopyCharacAtApplicationValue re;
        try {
            re = (CopyCharacAtApplicationValue)CopyCharacAtApplicationValue.m_staticPool.borrowObject();
            re.m_pool = CopyCharacAtApplicationValue.m_staticPool;
        }
        catch (Exception e) {
            re = new CopyCharacAtApplicationValue();
            re.m_pool = null;
            re.m_isStatic = false;
            CopyCharacAtApplicationValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un CopyCharacValueAtApplication : " + e.getMessage()));
        }
        re.m_characId = this.m_characId;
        re.m_max = this.m_max;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    public void onApplication() {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_characId = (byte)((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        int ratio = 100;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            ratio = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_characId);
        if (charac == null) {
            CopyCharacAtApplicationValue.m_logger.error((Object)("Charac parametree inconnue " + this.m_characId));
            return;
        }
        if (!this.m_caster.hasCharacteristic(charac) || !this.m_target.hasCharacteristic(charac)) {
            return;
        }
        final int max = this.m_caster.getCharacteristic(charac).max();
        this.m_max = ((max == Integer.MAX_VALUE) ? Integer.MAX_VALUE : ValueRounder.randomRound(max * ratio / 100.0f));
        this.m_value = ValueRounder.randomRound(this.m_caster.getCharacteristicValue(charac) * ratio / 100.0f);
        super.onApplication();
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_caster == null || this.m_target == null) {
            this.setNotified();
            return;
        }
        final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_characId);
        if (charac == null) {
            CopyCharacAtApplicationValue.m_logger.error((Object)("Charac parametree inconnue " + this.m_characId));
            this.setNotified();
            return;
        }
        if (!this.m_caster.hasCharacteristic(charac) || !this.m_target.hasCharacteristic(charac)) {
            this.setNotified();
            return;
        }
        this.m_target.getCharacteristic(charac).setMax(this.m_max);
        this.m_target.getCharacteristic(charac).set(this.m_value);
    }
    
    @Override
    public boolean useCaster() {
        return true;
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
        return this.m_additionnalData;
    }
    
    @Override
    public void onCheckIn() {
        this.m_characId = -1;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CopyCharacAtApplicationValue>() {
            @Override
            public CopyCharacAtApplicationValue makeObject() {
                return new CopyCharacAtApplicationValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Id de la charac", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Ratio", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de la charac", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Ratio \u00e0 copier (defaut = 100%)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
