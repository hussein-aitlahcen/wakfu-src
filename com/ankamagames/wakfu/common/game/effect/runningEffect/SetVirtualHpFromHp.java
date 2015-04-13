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

public final class SetVirtualHpFromHp extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_baseVirtualHp;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetVirtualHpFromHp.PARAMETERS_LIST_SET;
    }
    
    public SetVirtualHpFromHp() {
        super();
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(SetVirtualHpFromHp.this.m_baseVirtualHp);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetVirtualHpFromHp.this.m_baseVirtualHp = buffer.getInt();
            }
            
            @Override
            public int expectedSize() {
                return 4;
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public SetVirtualHpFromHp newInstance() {
        SetVirtualHpFromHp re;
        try {
            re = (SetVirtualHpFromHp)SetVirtualHpFromHp.m_staticPool.borrowObject();
            re.m_pool = SetVirtualHpFromHp.m_staticPool;
        }
        catch (Exception e) {
            re = new SetVirtualHpFromHp();
            re.m_pool = null;
            re.m_isStatic = false;
            SetVirtualHpFromHp.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetVirtualHpFromHp : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int referenceValue = this.m_target.getCharacteristic(FighterCharacteristicType.HP).max();
        this.m_value = referenceValue * this.m_value / 100;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.HP) || !this.m_target.hasCharacteristic(FighterCharacteristicType.VIRTUAL_HP)) {
            this.setNotified();
            return;
        }
        final AbstractCharacteristic virtualHp = this.m_target.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP);
        virtualHp.updateMaxValue(this.m_value);
        if (this.isValueComputationEnabled()) {
            final AbstractCharacteristic hp = this.m_target.getCharacteristic(FighterCharacteristicType.HP);
            final float percent = hp.value() / hp.max();
            virtualHp.set((int)(virtualHp.max() * percent));
            this.m_baseVirtualHp = virtualHp.value();
        }
        else {
            virtualHp.set(this.m_baseVirtualHp);
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
    public void onCheckIn() {
        this.m_baseVirtualHp = 0;
        super.onCheckIn();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetVirtualHpFromHp>() {
            @Override
            public SetVirtualHpFromHp makeObject() {
                return new SetVirtualHpFromHp();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de vie max en vie virtuelle", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
