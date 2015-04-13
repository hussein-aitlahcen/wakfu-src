package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CollectForController extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CollectForController.PARAMETERS_LIST_SET;
    }
    
    public CollectForController() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public CollectForController newInstance() {
        CollectForController re;
        try {
            re = (CollectForController)CollectForController.m_staticPool.borrowObject();
            re.m_pool = CollectForController.m_staticPool;
        }
        catch (Exception e) {
            re = new CollectForController();
            re.m_pool = null;
            re.m_isStatic = false;
            CollectForController.m_logger.error((Object)("Erreur lors d'un checkOut sur un CollectForController : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (this.m_targetCell == null) {
            return;
        }
        final Collection<BasicEffectArea> areas = this.m_context.getEffectAreaManager().getActiveEffectAreas();
        BasicCharacterInfo controller = null;
        if (this.m_caster instanceof BasicCharacterInfo) {
            controller = ((BasicCharacterInfo)this.m_caster).getController();
        }
        if (controller == null) {
            return;
        }
        final Collection<AbstractEffectArea> areaToTrigger = new ArrayList<AbstractEffectArea>();
        for (final BasicEffectArea area : areas) {
            if (area.contains(this.m_targetCell) && area.getType() == EffectAreaType.LOOT_AREA.getTypeId()) {
                areaToTrigger.add((AbstractEffectArea)area);
            }
        }
        for (final AbstractEffectArea area2 : areaToTrigger) {
            area2.setForcedTarget(controller);
            area2.triggers(this, controller);
        }
    }
    
    @Override
    public boolean useCaster() {
        return true;
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
        m_staticPool = new MonitoredPool(new ObjectFactory<CollectForController>() {
            @Override
            public CollectForController makeObject() {
                return new CollectForController();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
