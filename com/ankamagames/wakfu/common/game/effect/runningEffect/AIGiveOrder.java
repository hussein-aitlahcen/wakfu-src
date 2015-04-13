package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class AIGiveOrder extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private long m_orderTargetId;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AIGiveOrder.PARAMETERS_LIST_SET;
    }
    
    public AIGiveOrder() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(AIGiveOrder.this.m_orderTargetId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                AIGiveOrder.this.m_orderTargetId = buffer.getLong();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public AIGiveOrder newInstance() {
        AIGiveOrder re;
        try {
            re = (AIGiveOrder)AIGiveOrder.m_staticPool.borrowObject();
            re.m_pool = AIGiveOrder.m_staticPool;
        }
        catch (Exception e) {
            re = new AIGiveOrder();
            re.m_pool = null;
            re.m_isStatic = false;
            AIGiveOrder.m_logger.error((Object)("Erreur lors d'un checkOut sur un OrderSummon : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null && this.m_target instanceof BasicCharacterInfo && this.m_value >= 0) {
            ((BasicCharacterInfo)this.m_target).setAIOrder(this.m_value, this.m_target);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target != null && this.m_target instanceof BasicCharacterInfo && this.m_value >= 0) {
            ((BasicCharacterInfo)this.m_target).cancelAIOrder(this.m_value, this.m_target);
        }
        super.unapplyOverride();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        else {
            this.m_value = -1;
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
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<AIGiveOrder>() {
            @Override
            public AIGiveOrder makeObject() {
                return new AIGiveOrder();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Ordre \u00e0 une cr\u00e9ature cibl\u00e9 sur une autre cr\u00e9ature", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id", WakfuRunningEffectParameterType.ID) }) });
    }
}
