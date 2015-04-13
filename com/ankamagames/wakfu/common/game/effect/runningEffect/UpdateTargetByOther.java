package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class UpdateTargetByOther extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return UpdateTargetByOther.PARAMETERS_LIST_SET;
    }
    
    @Override
    public UpdateTargetByOther newInstance() {
        UpdateTargetByOther re;
        try {
            re = (UpdateTargetByOther)UpdateTargetByOther.m_staticPool.borrowObject();
            re.m_pool = UpdateTargetByOther.m_staticPool;
        }
        catch (Exception e) {
            re = new UpdateTargetByOther();
            re.m_isStatic = false;
            re.m_pool = null;
            UpdateTargetByOther.m_logger.error((Object)("Erreur lors d'un checkOut sur un UpdateTargetByOther : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE != null && trigger && this.m_caster != null) {
            linkedRE.setTarget(this.m_caster);
        }
        this.setNotified(true);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            return;
        }
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<UpdateTargetByOther>() {
            @Override
            public UpdateTargetByOther makeObject() {
                return new UpdateTargetByOther();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("modifie la cible de l'effet d\u00e9clench\u00e9 par le caster", new WakfuRunningEffectParameter[0]) });
    }
}
