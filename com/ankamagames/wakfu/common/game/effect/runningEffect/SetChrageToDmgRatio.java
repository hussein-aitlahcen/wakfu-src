package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SetChrageToDmgRatio extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private float m_ratio;
    private CharacRatioFromAnotherCharacProcedure m_chrageToDmgProcedure;
    private final BinarSerialPart ADDITIONAL_DATA;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetChrageToDmgRatio.PARAMETERS_LIST_SET;
    }
    
    public SetChrageToDmgRatio() {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putFloat(SetChrageToDmgRatio.this.m_ratio);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetChrageToDmgRatio.this.m_ratio = buffer.getFloat();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public SetChrageToDmgRatio newInstance() {
        SetChrageToDmgRatio re;
        try {
            re = (SetChrageToDmgRatio)SetChrageToDmgRatio.m_staticPool.borrowObject();
            re.m_pool = SetChrageToDmgRatio.m_staticPool;
        }
        catch (Exception e) {
            re = new SetChrageToDmgRatio();
            re.m_pool = null;
            re.m_isStatic = false;
            SetChrageToDmgRatio.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetChrageToDmgRatio : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_ratio = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel());
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_ratio == 0.0f || this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.CHRAGE) || !(this.m_target instanceof BasicCharacterInfo)) {
            this.setNotified();
            return;
        }
        this.m_chrageToDmgProcedure = new CharacRatioFromAnotherCharacProcedure(((BasicCharacterInfo)this.m_target).getCharacteristics(), FighterCharacteristicType.CHRAGE, FighterCharacteristicType.DMG_IN_PERCENT, this.m_ratio);
        ((FighterCharacteristic)this.m_target.getCharacteristic(FighterCharacteristicType.CHRAGE)).addProcedure(this.m_chrageToDmgProcedure);
        this.m_chrageToDmgProcedure.execute(FighterCharacteristicEvent.DEFAULT_SET, this.m_target.getCharacteristicValue(FighterCharacteristicType.CHRAGE));
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.CHRAGE) || this.m_chrageToDmgProcedure == null) {
            return;
        }
        ((FighterCharacteristic)this.m_target.getCharacteristic(FighterCharacteristicType.CHRAGE)).removeProcedure(this.m_chrageToDmgProcedure);
        this.m_chrageToDmgProcedure.execute(FighterCharacteristicEvent.VALUE_SUBSTRACTED, this.m_target.getCharacteristicValue(FighterCharacteristicType.CHRAGE));
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
        return this.ADDITIONAL_DATA;
    }
    
    @Override
    public void onCheckIn() {
        this.m_ratio = 0.0f;
        this.m_chrageToDmgProcedure = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetChrageToDmgRatio>() {
            @Override
            public SetChrageToDmgRatio makeObject() {
                return new SetChrageToDmgRatio();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
