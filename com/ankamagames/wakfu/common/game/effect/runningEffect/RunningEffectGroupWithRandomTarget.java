package com.ankamagames.wakfu.common.game.effect.runningEffect;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupWithRandomTarget extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_maxTargetCount;
    private boolean m_checkCriterionOnTargets;
    private boolean m_shuffleTargets;
    
    public RunningEffectGroupWithRandomTarget() {
        super();
        this.m_shuffleTargets = true;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupWithRandomTarget.PARAMETERS_LIST_SET;
    }
    
    @Override
    public RunningEffectGroup newInstance() {
        RunningEffectGroupWithRandomTarget re;
        try {
            re = (RunningEffectGroupWithRandomTarget)RunningEffectGroupWithRandomTarget.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupWithRandomTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupWithRandomTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupWithRandomTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_maxTargetCount = this.m_maxTargetCount;
        re.m_shuffleTargets = this.m_shuffleTargets;
        re.m_checkCriterionOnTargets = this.m_checkCriterionOnTargets;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (this.m_effectGroup == null && this.isValueComputationEnabled()) {
            return;
        }
        final List<EffectUser> potentialTargetList = RunningEffectUtils.getTargets(this, this.m_checkCriterionOnTargets);
        if (this.m_shuffleTargets) {
            Collections.shuffle(potentialTargetList);
        }
        this.m_maxTargetCount = Math.min(potentialTargetList.size(), this.m_maxTargetCount);
        for (int i = 0; i < this.m_maxTargetCount; ++i) {
            this.executeEffectGroupWithNewParams(this.m_effectGroup.iterator(), triggerRE, false, potentialTargetList.get(i));
        }
    }
    
    private void executeEffectGroupWithNewParams(final Iterator<WakfuEffect> effects, final RunningEffect linkedRE, final boolean disableProbabilityComputation, final EffectUser target) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, (WakfuRunningEffect)linkedRE);
        if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
            params.setForcedLevel(this.getContainerLevel());
        }
        try {
            this.executeEffectGroup(effects, params, target);
        }
        catch (Exception e) {
            RunningEffectGroupWithRandomTarget.m_logger.error((Object)("Exception levee lors de l'execution d'un groupe d'effets id " + ((WakfuEffect)this.m_genericEffect).getEffectId()), (Throwable)e);
        }
        finally {
            params.release();
        }
    }
    
    private void executeEffectGroup(final Iterator<WakfuEffect> effects, final EffectExecutionParameters params, final EffectUser target) {
        while (effects.hasNext()) {
            final WakfuEffect e = effects.next();
            e.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude(), target, params, false);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_checkCriterionOnTargets = false;
        this.m_shuffleTargets = true;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_maxTargetCount = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_checkCriterionOnTargets = (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_shuffleTargets = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup != null) {
            this.m_effectGroup = effectGroup.instanceAnother(this.getContainerLevel());
        }
    }
    
    @Override
    boolean checkConditions(final RunningEffect linkedRE) {
        return (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2 && ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1) || super.checkConditions(linkedRE);
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
        this.m_maxTargetCount = -1;
        this.m_checkCriterionOnTargets = false;
        this.m_shuffleTargets = true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupWithRandomTarget>() {
            @Override
            public RunningEffectGroupWithRandomTarget makeObject() {
                return new RunningEffectGroupWithRandomTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nb Cibles Max", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Check de critere sur les cibles", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nb Cibles Max", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Critere sur les cibles = 1, critere sur effet de base = 0 (defaut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Pas de shuffle sur la liste des cibles", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nb Cibles Max", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Critere sur les cibles = 1, critere sur effet de base = 0 (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("On m\u00e9lange les cibles avant d'appliquer la limite (oui = 1 (defaut), non = 0)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
