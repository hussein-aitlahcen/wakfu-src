package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class AIGiveOrderOnCell extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private final BinarSerialPart ADDITIONAL_DATA;
    private Point3 m_cellForOrder;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AIGiveOrderOnCell.PARAMETERS_LIST_SET;
    }
    
    public AIGiveOrderOnCell() {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart(10) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(AIGiveOrderOnCell.this.m_cellForOrder.getX());
                buffer.putInt(AIGiveOrderOnCell.this.m_cellForOrder.getY());
                buffer.putShort(AIGiveOrderOnCell.this.m_cellForOrder.getZ());
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final int x = buffer.getInt();
                final int y = buffer.getInt();
                final short z = buffer.getShort();
                AIGiveOrderOnCell.this.m_cellForOrder = new Point3(x, y, z);
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public AIGiveOrderOnCell newInstance() {
        AIGiveOrderOnCell re;
        try {
            re = (AIGiveOrderOnCell)AIGiveOrderOnCell.m_staticPool.borrowObject();
            re.m_pool = AIGiveOrderOnCell.m_staticPool;
        }
        catch (Exception e) {
            re = new AIGiveOrderOnCell();
            re.m_pool = null;
            re.m_isStatic = false;
            AIGiveOrderOnCell.m_logger.error((Object)("Erreur lors d'un checkOut sur un OrderSummon : " + e.getMessage()));
        }
        re.m_cellForOrder = this.m_cellForOrder;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final EffectUser user = this.m_target;
        if (user != null && user instanceof BasicCharacterInfo && this.m_value >= 0) {
            ((BasicCharacterInfo)user).setAIOrder(this.m_value, this.m_cellForOrder);
        }
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
        final EffectUser user = this.m_target;
        if (user != null && user instanceof BasicCharacterInfo && this.m_value >= 0) {
            ((BasicCharacterInfo)user).cancelAIOrder(this.m_value, this.m_cellForOrder);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        this.m_value = -1;
        this.m_cellForOrder = new Point3(this.m_targetCell);
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            final int x = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final int y = ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            short z = 0;
            if (this.m_target instanceof BasicCharacterInfo) {
                final BasicCharacterInfo info = (BasicCharacterInfo)this.m_target;
                z = TopologyMapManager.getNearestWalkableZ(info.getInstanceId(), x, y, info.getPosition().getZ(), (short)0);
            }
            this.m_cellForOrder = new Point3(x, y, z);
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
        super.onCheckIn();
        this.m_cellForOrder = null;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATA;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<AIGiveOrderOnCell>() {
            @Override
            public AIGiveOrderOnCell makeObject() {
                return new AIGiveOrderOnCell();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Ordre \u00e0 une cr\u00e9ature li\u00e9 \u00e0 une cellule (cf PriorityGoalType)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id", WakfuRunningEffectParameterType.ID) }), new WakfuRunningEffectParameterList("Sur une cellule d\u00e9finie (le Z \u00e9tant d\u00e9fini par l'altitude valide la plus proche de la cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("X", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("Y", WakfuRunningEffectParameterType.ID) }) });
    }
}
