package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SetAuraOnTarget extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private BasicEffectArea m_aura;
    private long m_newTargetId;
    public BinarSerialPart ADDITIONALDATAS;
    
    public SetAuraOnTarget() {
        super();
        this.ADDITIONALDATAS = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SetAuraOnTarget.this.m_newTargetId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetAuraOnTarget.this.m_newTargetId = buffer.getLong();
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetAuraOnTarget.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SetAuraOnTarget newInstance() {
        SetAuraOnTarget re;
        try {
            re = (SetAuraOnTarget)SetAuraOnTarget.m_staticPool.borrowObject();
            re.m_pool = SetAuraOnTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new SetAuraOnTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            SetAuraOnTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_newTargetId = this.m_newTargetId;
        re.m_aura = this.m_aura;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final AbstractAuraEffectArea auraEffectArea = StaticEffectAreaManager.getInstance().getAura(this.m_value);
        if (auraEffectArea != null && this.m_target != null && this.m_targetCell != null) {
            final short level = this.getContainerLevel();
            this.m_aura = auraEffectArea.instanceAnother(new EffectAreaParameters(this.m_newTargetId, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_context, this.m_target, level, this.m_target.getDirection()));
            this.notifyExecution(linkedRE, trigger);
            if (this.m_context.getEffectAreaManager() != null) {
                this.m_context.getEffectAreaManager().addEffectArea(this.m_aura);
            }
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)2);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_aura != null) {
            this.m_context.getEffectAreaManager().removeEffectArea(this.m_aura);
        }
        super.unapplyOverride();
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
        return true;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONALDATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetAuraOnTarget>() {
            @Override
            public SetAuraOnTarget makeObject() {
                return new SetAuraOnTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard (le level de l'aura = level du sort)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de l'aura", WakfuRunningEffectParameterType.ID) }) });
    }
}
