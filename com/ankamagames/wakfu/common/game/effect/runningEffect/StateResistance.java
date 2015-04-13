package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class StateResistance extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private short m_stateId;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    public StateResistance() {
        super();
        this.ADDITIONAL_DATAS = new BinarSerialPart(2) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putShort(StateResistance.this.m_stateId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                StateResistance.this.m_stateId = buffer.getShort();
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return StateResistance.PARAMETERS_LIST_SET;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            return;
        }
        if (this.m_stateId == -1) {
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        target.addStateResistance(this.m_stateId, this.m_value);
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        StateResistance re;
        try {
            re = (StateResistance)StateResistance.m_staticPool.borrowObject();
            re.m_pool = StateResistance.m_staticPool;
        }
        catch (Exception e) {
            re = new StateResistance();
            re.m_pool = null;
            re.m_isStatic = false;
            StateResistance.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuff : " + e.getMessage()));
        }
        re.m_stateId = this.m_stateId;
        return re;
    }
    
    public short getStateId() {
        return this.m_stateId;
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
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() != 2) {
            StateResistance.m_logger.error((Object)"Pas le bon nombre de param\u00e8tres !!");
            return;
        }
        final short containerLevel = this.getContainerLevel();
        this.m_stateId = (short)((WakfuEffect)this.m_genericEffect).getParam(0, containerLevel, RoundingMethod.RANDOM);
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(1, containerLevel, RoundingMethod.RANDOM);
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATAS;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_stateId = -1;
    }
    
    @Override
    public void unapplyOverride() {
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        target.addStateResistance(this.m_stateId, -this.m_value);
        super.unapplyOverride();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<StateResistance>() {
            @Override
            public StateResistance makeObject() {
                return new StateResistance();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("State Resistance", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de l'\u00e9tat", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("nb point de res", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
