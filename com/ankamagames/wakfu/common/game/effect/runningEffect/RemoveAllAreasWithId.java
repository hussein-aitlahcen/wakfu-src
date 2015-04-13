package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveAllAreasWithId extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveAllAreasWithId.PARAMETERS_LIST_SET;
    }
    
    public RemoveAllAreasWithId() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveAllAreasWithId newInstance() {
        RemoveAllAreasWithId re;
        try {
            re = (RemoveAllAreasWithId)RemoveAllAreasWithId.m_staticPool.borrowObject();
            re.m_pool = RemoveAllAreasWithId.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveAllAreasWithId();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveAllAreasWithId.m_logger.error((Object)("Erreur lors d'un checkOut sur un RemoveAllAreasWithId : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 1) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_context == null) {
            return;
        }
        final BasicEffectAreaManager effectAreaManager = this.m_context.getEffectAreaManager();
        final Collection<BasicEffectArea> activeEffectAreas = effectAreaManager.getActiveEffectAreas();
        final Collection<BasicEffectArea> areaToRemove = new HashSet<BasicEffectArea>();
        for (final BasicEffectArea area : activeEffectAreas) {
            if (area.getBaseId() == this.m_value) {
                areaToRemove.add(area);
            }
        }
        for (final BasicEffectArea area : areaToRemove) {
            effectAreaManager.removeEffectArea(area, this.m_caster);
        }
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveAllAreasWithId>() {
            @Override
            public RemoveAllAreasWithId makeObject() {
                return new RemoveAllAreasWithId();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Id de la Zone a retirer", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
