package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveFromFightMap extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveFromFightMap.PARAMETERS_LIST_SET;
    }
    
    public RemoveFromFightMap() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveFromFightMap newInstance() {
        RemoveFromFightMap re;
        try {
            re = (RemoveFromFightMap)RemoveFromFightMap.m_staticPool.borrowObject();
            re.m_pool = RemoveFromFightMap.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveFromFightMap();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveFromFightMap.m_logger.error((Object)("Erreur lors d'un checkOut sur un RemoveFromFightMap : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.neededParamsNotCorrect()) {
            this.setNotified();
            return;
        }
        this.m_context.getFightMap().removeObstacle((FightObstacle)this.m_target);
    }
    
    private boolean neededParamsNotCorrect() {
        return this.m_target == null || this.m_context == null || this.m_context.getFightMap() == null || !(this.m_target instanceof FightObstacle);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.neededParamsNotCorrect()) {
            return;
        }
        this.m_context.getFightMap().addObstacle((FightObstacle)this.m_target);
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
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveFromFightMap>() {
            @Override
            public RemoveFromFightMap makeObject() {
                return new RemoveFromFightMap();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
