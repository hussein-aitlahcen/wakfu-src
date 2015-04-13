package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ReplaceTriggeringEffectTargetByOwner extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ReplaceTriggeringEffectTargetByOwner.PARAMETERS_LIST_SET;
    }
    
    @Override
    public ReplaceTriggeringEffectTargetByOwner newInstance() {
        ReplaceTriggeringEffectTargetByOwner re;
        try {
            re = (ReplaceTriggeringEffectTargetByOwner)ReplaceTriggeringEffectTargetByOwner.m_staticPool.borrowObject();
            re.m_pool = ReplaceTriggeringEffectTargetByOwner.m_staticPool;
        }
        catch (Exception e) {
            re = new ReplaceTriggeringEffectTargetByOwner();
            re.m_isStatic = false;
            re.m_pool = null;
            ReplaceTriggeringEffectTargetByOwner.m_logger.error((Object)("Erreur lors d'un checkOut sur un UpdateTargetByOwner : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE == null || !trigger) {
            this.setNotified(true);
            return;
        }
        final EffectUser originalTarget = linkedRE.getTarget();
        if (originalTarget == null) {
            this.setNotified(true);
            return;
        }
        final EffectUser targetOwner = this.getOwner(originalTarget);
        if (targetOwner == null || targetOwner == originalTarget) {
            this.setNotified(true);
            return;
        }
        linkedRE.setTarget(targetOwner);
    }
    
    private EffectUser getOwner(final EffectUser someUser) {
        if (someUser instanceof BasicCharacterInfo) {
            return ((BasicCharacterInfo)someUser).getController();
        }
        if (someUser instanceof AreaOwnerProvider) {
            return this.getOwner(((AreaOwnerProvider)someUser).getOwner());
        }
        return someUser;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ReplaceTriggeringEffectTargetByOwner>() {
            @Override
            public ReplaceTriggeringEffectTargetByOwner makeObject() {
                return new ReplaceTriggeringEffectTargetByOwner();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("remplace la cible de l'effet d\u00e9clencheur par son propri\u00e9taire", new WakfuRunningEffectParameter[0]) });
    }
}
