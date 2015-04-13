package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupLevelFunctionState extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_FUNCTION_STATE;
    private int m_stateId;
    private float m_stateLevelFactor;
    private boolean m_checkOnCaster;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupLevelFunctionState.PARAMETERS_LIST_SET_FUNCTION_STATE;
    }
    
    public RunningEffectGroupLevelFunctionState() {
        super();
        this.m_stateLevelFactor = 1.0f;
        this.m_checkOnCaster = false;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupLevelFunctionState newInstance() {
        RunningEffectGroupLevelFunctionState re;
        try {
            re = (RunningEffectGroupLevelFunctionState)RunningEffectGroupLevelFunctionState.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionState.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionState();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionState.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFuntionState : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        super.effectiveComputeValue(triggerRE);
        this.m_stateId = ((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 8) {
            this.m_checkOnCaster = (((WakfuEffect)this.m_genericEffect).getParam(7, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 9) {
            this.m_stateLevelFactor = ((WakfuEffect)this.m_genericEffect).getParam(8, this.getContainerLevel());
        }
        else {
            this.m_stateLevelFactor = 1.0f;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 10) {
            final boolean inverseFactor = ((WakfuEffect)this.m_genericEffect).getParam(9, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
            if (inverseFactor) {
                this.m_stateLevelFactor = 1.0f / this.m_stateLevelFactor;
            }
        }
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = super.getExecutionParameters(linkedRE, disableProbabilityComputation);
        EffectUser stateCarrier;
        if (this.m_checkOnCaster) {
            stateCarrier = this.m_caster;
        }
        else {
            stateCarrier = this.m_target;
        }
        if (stateCarrier == null) {
            return params;
        }
        final RunningEffectManager rem = stateCarrier.getRunningEffectManager();
        for (final RunningEffect re : rem) {
            if (!(re instanceof StateRunningEffect)) {
                continue;
            }
            final State state = ((StateRunningEffect)re).getState();
            if (state.getStateBaseId() != this.m_stateId) {
                continue;
            }
            final int forcedLevel = (int)Math.floor(this.m_stateLevelFactor * state.getLevel());
            params.setForcedLevel(forcedLevel);
            break;
        }
        return params;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_stateId = -1;
        this.m_checkOnCaster = false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionState>() {
            @Override
            public RunningEffectGroupLevelFunctionState makeObject() {
                return new RunningEffectGroupLevelFunctionState();
            }
        });
        PARAMETERS_LIST_SET_FUNCTION_STATE = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Transmet le niveau de l'etat (cible) comme niveau pour les effets du groupe", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de l'\u00e9tat \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Transmet le niveau de l'etat (caster) comme niveau pour les effets du groupe", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de l'\u00e9tat \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Check on Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Transmet le niveau de l'etat (caster) comme niveau pour les effets du groupe avec multiplicateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de l'\u00e9tat \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Check on Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur sur le niveau de l'\u00e9tat", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Transmet le niveau de l'etat (caster) comme niveau pour les effets du groupe avec multiplicateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de l'\u00e9tat \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Check on Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur sur le niveau de l'\u00e9tat", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur inverse (0 = non (defaut), 1 = oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
