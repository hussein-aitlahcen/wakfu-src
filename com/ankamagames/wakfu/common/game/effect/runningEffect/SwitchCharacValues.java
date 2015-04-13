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

public final class SwitchCharacValues extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private byte m_firstCharacId;
    private byte m_secondCharacId;
    private int m_firstCharacValue;
    private int m_secondCharacValue;
    private final BinarSerialPart m_additionalData;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SwitchCharacValues.PARAMETERS_LIST_SET;
    }
    
    public SwitchCharacValues() {
        super();
        this.m_additionalData = new BinarSerialPart(10) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put(SwitchCharacValues.this.m_firstCharacId);
                buffer.put(SwitchCharacValues.this.m_secondCharacId);
                buffer.putInt(SwitchCharacValues.this.m_firstCharacValue);
                buffer.putInt(SwitchCharacValues.this.m_secondCharacValue);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SwitchCharacValues.this.m_firstCharacId = buffer.get();
                SwitchCharacValues.this.m_secondCharacId = buffer.get();
                SwitchCharacValues.this.m_firstCharacValue = buffer.getInt();
                SwitchCharacValues.this.m_secondCharacValue = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public SwitchCharacValues newInstance() {
        SwitchCharacValues re;
        try {
            re = (SwitchCharacValues)SwitchCharacValues.m_staticPool.borrowObject();
            re.m_pool = SwitchCharacValues.m_staticPool;
        }
        catch (Exception e) {
            re = new SwitchCharacValues();
            re.m_pool = null;
            re.m_isStatic = false;
            SwitchCharacValues.m_logger.error((Object)("Erreur lors d'un checkOut sur un SwitchCharacValues : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_firstCharacId = (byte)((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_secondCharacId = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified();
            return;
        }
        final FighterCharacteristicType firstCharacType = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_firstCharacId);
        final FighterCharacteristicType secondCharacType = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_secondCharacId);
        if (firstCharacType == null || secondCharacType == null) {
            SwitchCharacValues.m_logger.error((Object)"Impossible d'executer l'effet, l'une des carac est inconnue");
            this.setNotified();
            return;
        }
        final AbstractCharacteristic firstCharac = this.m_target.getCharacteristic(firstCharacType);
        final AbstractCharacteristic secondCharac = this.m_target.getCharacteristic(secondCharacType);
        if (firstCharac == null || secondCharac == null) {
            SwitchCharacValues.m_logger.error((Object)"Impossible d'executer l'effet, l'une des carac est inconnue");
            this.setNotified();
            return;
        }
        if (this.isValueComputationEnabled()) {
            this.m_firstCharacValue = firstCharac.value();
            this.m_secondCharacValue = secondCharac.value();
        }
        firstCharac.substract(this.m_firstCharacValue);
        firstCharac.add(this.m_secondCharacValue);
        secondCharac.substract(this.m_secondCharacValue);
        secondCharac.add(this.m_firstCharacValue);
    }
    
    @Override
    public void unapplyOverride() {
        final FighterCharacteristicType firstCharacType = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_firstCharacId);
        final FighterCharacteristicType secondCharacType = FighterCharacteristicType.getCharacteristicTypeFromId(this.m_secondCharacId);
        if (firstCharacType == null || secondCharacType == null) {
            return;
        }
        final AbstractCharacteristic firstCharac = this.m_target.getCharacteristic(firstCharacType);
        final AbstractCharacteristic secondCharac = this.m_target.getCharacteristic(secondCharacType);
        if (firstCharac == null || secondCharac == null) {
            return;
        }
        firstCharac.substract(this.m_secondCharacValue);
        firstCharac.add(this.m_firstCharacValue);
        secondCharac.substract(this.m_firstCharacValue);
        secondCharac.add(this.m_secondCharacValue);
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
        return this.m_additionalData;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_firstCharacId = -1;
        this.m_secondCharacId = -2;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SwitchCharacValues>() {
            @Override
            public SwitchCharacValues makeObject() {
                return new SwitchCharacValues();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Carac \u00e0 inverser", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Carac 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Carac 2", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
