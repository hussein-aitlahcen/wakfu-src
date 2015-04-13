package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupLevelFunctionTriggeringActionCost extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_SPELL;
    private float m_levelPerPA;
    private float m_levelPerPM;
    private float m_levelPerPW;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupLevelFunctionTriggeringActionCost.PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_SPELL;
    }
    
    public RunningEffectGroupLevelFunctionTriggeringActionCost() {
        super();
        this.m_levelPerPA = 0.0f;
        this.m_levelPerPM = 0.0f;
        this.m_levelPerPW = 0.0f;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupLevelFunctionTriggeringActionCost newInstance() {
        RunningEffectGroupLevelFunctionTriggeringActionCost re;
        try {
            re = (RunningEffectGroupLevelFunctionTriggeringActionCost)RunningEffectGroupLevelFunctionTriggeringActionCost.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionTriggeringActionCost.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionTriggeringActionCost();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionTriggeringActionCost.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFunctionTriggeringActionCost : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        super.effectiveComputeValue(triggerRE);
        this.m_levelPerPA = ((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel());
        this.m_levelPerPM = ((WakfuEffect)this.m_genericEffect).getParam(7, this.getContainerLevel());
        this.m_levelPerPW = ((WakfuEffect)this.m_genericEffect).getParam(8, this.getContainerLevel());
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = super.getExecutionParameters(linkedRE, disableProbabilityComputation);
        if (linkedRE == null) {
            RunningEffectGroupLevelFunctionTriggeringActionCost.m_logger.error((Object)"Unable to execute a RunningEffectGroupLevelFunctionTriggeringActionCost without triggering effect");
            return params;
        }
        if (!(linkedRE instanceof ActionCost)) {
            RunningEffectGroupLevelFunctionTriggeringActionCost.m_logger.error((Object)"Unable to execute a RunningEffectGroupLevelFunctionTriggeringActionCost without actionCost triggering effect");
            return params;
        }
        final ActionCost actionCost = (ActionCost)linkedRE;
        final int apCost = actionCost.getApUseFromValue();
        final int mpCost = actionCost.getMpUseFromValue();
        final int wpCost = actionCost.getWpUseFromValue();
        final float finalLevel = apCost * this.m_levelPerPA + mpCost * this.m_levelPerPM + wpCost * this.m_levelPerPW;
        params.setForcedLevel(Math.round(finalLevel));
        return params;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionTriggeringActionCost>() {
            @Override
            public RunningEffectGroupLevelFunctionTriggeringActionCost makeObject() {
                return new RunningEffectGroupLevelFunctionTriggeringActionCost();
            }
        });
        PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_SPELL = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Calcul le niveau des effets du groupe en fonction du cout du sort d\u00e9clenchant", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau par PA", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau par PM", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau par PW", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
