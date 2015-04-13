package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class NullEffect extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return NullEffect.PARAMETERS_LIST_SET;
    }
    
    @Override
    public NullEffect newInstance() {
        NullEffect re;
        try {
            re = (NullEffect)NullEffect.m_staticPool.borrowObject();
            re.m_pool = NullEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new NullEffect();
            re.m_pool = null;
            NullEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un NullEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() > 0) {
            this.setExecutionStatus((byte)((WakfuEffect)this.m_genericEffect).getParam(0, (short)0, RoundingMethod.RANDOM));
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
    
    public boolean canBeExecutedOnKO() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<NullEffect>() {
            @Override
            public NullEffect makeObject() {
                return new NullEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Standard", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Affichage flottant (ExecutionStatusConstants)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("2 params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("1", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("2", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
