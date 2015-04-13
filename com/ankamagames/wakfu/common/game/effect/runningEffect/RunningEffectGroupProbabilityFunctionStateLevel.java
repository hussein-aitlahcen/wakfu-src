package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupProbabilityFunctionStateLevel extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_PROBA_FUNCTION_STATE_LEVEL;
    private int m_stateId;
    private boolean m_onCaster;
    private float m_probaBase;
    private float m_probaInc;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupProbabilityFunctionStateLevel.PARAMETERS_LIST_SET_PROBA_FUNCTION_STATE_LEVEL;
    }
    
    public RunningEffectGroupProbabilityFunctionStateLevel() {
        super();
        this.m_stateId = 0;
        this.m_onCaster = false;
        this.m_probaBase = 0.0f;
        this.m_probaInc = 0.0f;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupProbabilityFunctionStateLevel newInstance() {
        RunningEffectGroupProbabilityFunctionStateLevel re;
        try {
            re = (RunningEffectGroupProbabilityFunctionStateLevel)RunningEffectGroupProbabilityFunctionStateLevel.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupProbabilityFunctionStateLevel.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupProbabilityFunctionStateLevel();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupProbabilityFunctionStateLevel.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFuntionState : " + e.getMessage()));
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
        this.m_onCaster = (1 == ((WakfuEffect)this.m_genericEffect).getParam(7, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
        this.m_probaBase = ((WakfuEffect)this.m_genericEffect).getParam(8, this.getContainerLevel());
        this.m_probaInc = ((WakfuEffect)this.m_genericEffect).getParam(9, this.getContainerLevel());
    }
    
    @Override
    public boolean canBeExecuted() {
        if (!super.canBeExecuted()) {
            return false;
        }
        if (!this.isValueComputationEnabled()) {
            return true;
        }
        final EffectUser stateOwner = this.m_onCaster ? this.m_caster : this.m_target;
        if (stateOwner == null) {
            return false;
        }
        int stateLevel = 0;
        final RunningEffectManager rem = stateOwner.getRunningEffectManager();
        for (final RunningEffect re : rem) {
            if (!(re instanceof StateRunningEffect)) {
                continue;
            }
            final State state = ((StateRunningEffect)re).getState();
            if (state.getStateBaseId() != this.m_stateId) {
                continue;
            }
            stateLevel = state.getLevel();
            break;
        }
        final int proba = ValueRounder.randomRound(this.m_probaBase + this.m_probaInc * stateLevel);
        return proba > 0 && (proba >= 100 || DiceRoll.roll(100) <= proba);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupProbabilityFunctionStateLevel>() {
            @Override
            public RunningEffectGroupProbabilityFunctionStateLevel makeObject() {
                return new RunningEffectGroupProbabilityFunctionStateLevel();
            }
        });
        PARAMETERS_LIST_SET_PROBA_FUNCTION_STATE_LEVEL = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Utilise le niveau d'un \u00e9tat pour calculer la proba d'ex\u00e9cution du groupe d'effet", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de l'\u00e9tat \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de proba de base", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de proba par niveau d'\u00e9tat", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
