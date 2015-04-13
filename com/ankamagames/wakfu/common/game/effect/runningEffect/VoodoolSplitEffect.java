package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class VoodoolSplitEffect extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return VoodoolSplitEffect.PARAMETERS_LIST_SET;
    }
    
    public VoodoolSplitEffect() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public VoodoolSplitEffect newInstance() {
        VoodoolSplitEffect re;
        try {
            re = (VoodoolSplitEffect)VoodoolSplitEffect.m_staticPool.borrowObject();
            re.m_pool = VoodoolSplitEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new VoodoolSplitEffect();
            re.m_pool = null;
            re.m_isStatic = false;
            VoodoolSplitEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un VoodoolSplitEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        this.setNotified(true);
        if (linkedRE == null || this.m_caster == null || this.m_target == null) {
            return;
        }
        if (linkedRE.getId() == RunningEffectConstants.DEATH.getId()) {
            return;
        }
        if (this.m_target.isActiveProperty(FightPropertyType.CANNOT_BE_EFFECT_TARGET)) {
            linkedRE.setCancelled(true);
            return;
        }
        if (this.getParent().getManagerWhereIamStored() == null) {
            return;
        }
        final FightEffectUser triggeringCaster = (FightEffectUser)linkedRE.getCaster();
        final FightEffectUser totem = (FightEffectUser)this.getParent().getManagerWhereIamStored().getOwner();
        if (triggeringCaster == null || totem == null || triggeringCaster.getTeamId() != totem.getTeamId()) {
            return;
        }
        final WakfuEffect genericEffect = linkedRE.getGenericEffect();
        if (genericEffect != null && (genericEffect.getAreaOfEffect().getType() != AreaOfEffectEnum.POINT || genericEffect.hasProperty(RunningEffectPropertyType.ZONE_EFFECT))) {
            linkedRE.update(0, 50.0f, true);
        }
        final RunningEffect applyToTarget = linkedRE.newParameterizedInstance();
        applyToTarget.setTarget(this.m_target);
        applyToTarget.setExecutionParameters(WakfuEffectExecutionParameters.checkOut(true, true, null));
        applyToTarget.disableValueComputation();
        applyToTarget.getTriggersToExecute().set(2138);
        applyToTarget.askForExecution();
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
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<VoodoolSplitEffect>() {
            @Override
            public VoodoolSplitEffect makeObject() {
                return new VoodoolSplitEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
