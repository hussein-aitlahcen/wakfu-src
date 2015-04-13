package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupLevelFunctionCharacterLevel extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_FUNCTION_CHARACTER_LEVEL;
    private boolean m_checkOnCaster;
    private int m_ratio;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupLevelFunctionCharacterLevel.PARAMETERS_LIST_SET_FUNCTION_CHARACTER_LEVEL;
    }
    
    public RunningEffectGroupLevelFunctionCharacterLevel() {
        super();
        this.m_checkOnCaster = false;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupLevelFunctionCharacterLevel newInstance() {
        RunningEffectGroupLevelFunctionCharacterLevel re;
        try {
            re = (RunningEffectGroupLevelFunctionCharacterLevel)RunningEffectGroupLevelFunctionCharacterLevel.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionCharacterLevel.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionCharacterLevel();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionCharacterLevel.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFunctionCharacteristic : " + e.getMessage()));
        }
        re.m_ratio = this.m_ratio;
        re.m_checkOnCaster = this.m_checkOnCaster;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        super.effectiveComputeValue(triggerRE);
        this.m_checkOnCaster = (((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        this.m_ratio = ((WakfuEffect)this.m_genericEffect).getParam(7, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = super.getExecutionParameters(linkedRE, disableProbabilityComputation);
        EffectUser levelOwner;
        if (this.m_checkOnCaster) {
            levelOwner = this.m_caster;
        }
        else {
            levelOwner = this.m_target;
        }
        if (!(levelOwner instanceof BasicCharacterInfo)) {
            return params;
        }
        int forcedLevel = ((BasicCharacterInfo)levelOwner).getLevel();
        forcedLevel = forcedLevel * this.m_ratio / 100;
        params.setForcedLevel(forcedLevel);
        return params;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_ratio = 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionCharacterLevel>() {
            @Override
            public RunningEffectGroupLevelFunctionCharacterLevel makeObject() {
                return new RunningEffectGroupLevelFunctionCharacterLevel();
            }
        });
        PARAMETERS_LIST_SET_FUNCTION_CHARACTER_LEVEL = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Niveau des effets du groupe bas\u00e9 sur une caract\u00e9ristique : la carac sert de 'faux niveau'", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac prise sur Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Ratio en %", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
