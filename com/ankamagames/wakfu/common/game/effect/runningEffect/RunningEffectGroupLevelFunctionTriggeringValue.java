package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupLevelFunctionTriggeringValue extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_VALUE;
    private float m_ratio;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupLevelFunctionTriggeringValue.PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_VALUE;
    }
    
    public RunningEffectGroupLevelFunctionTriggeringValue() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupLevelFunctionTriggeringValue newInstance() {
        RunningEffectGroupLevelFunctionTriggeringValue re;
        try {
            re = (RunningEffectGroupLevelFunctionTriggeringValue)RunningEffectGroupLevelFunctionTriggeringValue.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionTriggeringValue.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionTriggeringValue();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionTriggeringValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFunctionTriggeringValue : " + e.getMessage()));
        }
        re.m_ratio = this.m_ratio;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        super.effectiveComputeValue(triggerRE);
        this.m_ratio = 1.0f;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 6) {
            this.m_ratio = ((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel());
        }
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = super.getExecutionParameters(linkedRE, disableProbabilityComputation);
        if (linkedRE == null) {
            RunningEffectGroupLevelFunctionTriggeringValue.m_logger.error((Object)"Unable to execute a RunningEffectGroupLevelFunctionTriggeringActionCost without triggering effect");
            return params;
        }
        params.setForcedLevel((int)(linkedRE.getValue() * this.m_ratio));
        return params;
    }
    
    @Override
    public void onCheckIn() {
        this.m_ratio = 1.0f;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionTriggeringValue>() {
            @Override
            public RunningEffectGroupLevelFunctionTriggeringValue makeObject() {
                return new RunningEffectGroupLevelFunctionTriggeringValue();
            }
        });
        PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_VALUE = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("groupe d'effet standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("groupe de r\u00e9ussite", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 r\u00e9ussir", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("groupe d'effet cibl\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("groupe d'effet cibl\u00e9, probabilit\u00e9 relative", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Gestion de la tansmission de la cible originale", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles  (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Change le caster du groupe d'effet pas sa cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Calcul le niveau des effets du groupe en fonction du cout du sort d\u00e9clenchant", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Ratio entre 0 et 1 (default = 1)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
