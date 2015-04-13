package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ExecuteTriggeringEffectInEffectZone extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_cancelTriggeringEffect;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ExecuteTriggeringEffectInEffectZone.PARAMETERS_LIST_SET;
    }
    
    public ExecuteTriggeringEffectInEffectZone() {
        super();
        this.m_cancelTriggeringEffect = false;
        this.setTriggersToExecute();
    }
    
    @Override
    public ExecuteTriggeringEffectInEffectZone newInstance() {
        ExecuteTriggeringEffectInEffectZone re;
        try {
            re = (ExecuteTriggeringEffectInEffectZone)ExecuteTriggeringEffectInEffectZone.m_staticPool.borrowObject();
            re.m_pool = ExecuteTriggeringEffectInEffectZone.m_staticPool;
        }
        catch (Exception e) {
            re = new ExecuteTriggeringEffectInEffectZone();
            re.m_pool = null;
            re.m_isStatic = false;
            re.m_cancelTriggeringEffect = this.m_cancelTriggeringEffect;
            ExecuteTriggeringEffectInEffectZone.m_logger.error((Object)("Erreur lors d'un checkOut sur un ExecuteTriggeringEffectInEffectZone : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() < 1) {
            this.m_cancelTriggeringEffect = false;
        }
        else {
            this.m_cancelTriggeringEffect = (1 == ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        final RunningEffect triggeringEffect = this.getTriggeringEffect(triggerRE);
        if (triggeringEffect == null) {
            ExecuteTriggeringEffectInEffectZone.m_logger.error((Object)("On ne peut pas executer un " + this.getClass().getSimpleName() + " sans effet declencheur, id = " + this.getEffectId()));
            return;
        }
        this.executeTriggeringOnThisArea((WakfuRunningEffect)triggeringEffect);
        if (this.m_cancelTriggeringEffect) {
            WakfuEffectExecutionParameters triggeringParams = (WakfuEffectExecutionParameters)triggeringEffect.getParams();
            if (triggeringParams == null) {
                triggeringParams = WakfuEffectExecutionParameters.checkOut(false, false, null);
                triggeringEffect.setExecutionParameters(triggeringParams);
            }
            triggeringParams.setDisableExecution();
        }
    }
    
    private void executeTriggeringOnThisArea(final WakfuRunningEffect triggeringEffect) {
        if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getAreaOfEffect() == null) {
            return;
        }
        final Effect triggeringGeneric = ((RunningEffect<Effect, EC>)triggeringEffect).getGenericEffect();
        if (triggeringGeneric == null) {
            return;
        }
        final AreaOfEffect area = ((WakfuEffect)this.m_genericEffect).getAreaOfEffect();
        final Iterable<EffectUser> targets = TargetFinder.getInstance().getTargets(this.m_target, this.m_context.getTargetInformationProvider(), area, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ());
        final ArrayList<Point3> cells = new ArrayList<Point3>();
        for (final EffectUser effectUser : targets) {
            final Point3 effectUserPosition = effectUser.getPosition();
            if (cells.contains(effectUserPosition)) {
                continue;
            }
            cells.add(effectUserPosition);
        }
        for (final Point3 cellToApplyEffect : cells) {
            triggeringGeneric.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), cellToApplyEffect.getX(), cellToApplyEffect.getY(), cellToApplyEffect.getZ(), null, this.getExecutionParameters(triggeringEffect, false), false);
        }
    }
    
    private WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setForcedLevel(linkedRE.getContainerLevel());
        params.setResetLimitedApplyCount(false);
        return params;
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
        this.m_cancelTriggeringEffect = false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ExecuteTriggeringEffectInEffectZone>() {
            @Override
            public ExecuteTriggeringEffectInEffectZone makeObject() {
                return new ExecuteTriggeringEffectInEffectZone();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Annulation de l'effet \u00e0 rediriger", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Annulation de l'effet d\u00e9clencheur (1=oui, non, sinon)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
