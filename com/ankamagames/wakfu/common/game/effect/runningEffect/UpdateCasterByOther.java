package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class UpdateCasterByOther extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return UpdateCasterByOther.PARAMETERS_LIST_SET;
    }
    
    @Override
    public UpdateCasterByOther newInstance() {
        UpdateCasterByOther re;
        try {
            re = (UpdateCasterByOther)UpdateCasterByOther.m_staticPool.borrowObject();
            re.m_pool = UpdateCasterByOther.m_staticPool;
        }
        catch (Exception e) {
            re = new UpdateCasterByOther();
            re.m_isStatic = false;
            re.m_pool = null;
            UpdateCasterByOther.m_logger.error((Object)("Erreur lors d'un checkOut sur un UpdateTargetByOther : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE != null && trigger && linkedRE.getTarget() != null) {
            linkedRE.setCaster(linkedRE.getTarget());
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
        m_staticPool = new MonitoredPool(new ObjectFactory<UpdateCasterByOther>() {
            @Override
            public UpdateCasterByOther makeObject() {
                return new UpdateCasterByOther();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("modifie le caster de l'effet d\u00e9clench\u00e9 par la cible (le porteur de cet effet)", new WakfuRunningEffectParameter[0]) });
    }
}
