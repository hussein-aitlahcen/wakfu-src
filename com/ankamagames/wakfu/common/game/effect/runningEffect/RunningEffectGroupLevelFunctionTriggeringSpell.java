package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupLevelFunctionTriggeringSpell extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_SPELL;
    private float m_levelPerPA;
    private float m_levelPerPM;
    private float m_levelPerPW;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupLevelFunctionTriggeringSpell.PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_SPELL;
    }
    
    public RunningEffectGroupLevelFunctionTriggeringSpell() {
        super();
        this.m_levelPerPA = 0.0f;
        this.m_levelPerPM = 0.0f;
        this.m_levelPerPW = 0.0f;
        this.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    public RunningEffectGroupLevelFunctionTriggeringSpell newInstance() {
        RunningEffectGroupLevelFunctionTriggeringSpell re;
        try {
            re = (RunningEffectGroupLevelFunctionTriggeringSpell)RunningEffectGroupLevelFunctionTriggeringSpell.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionTriggeringSpell.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionTriggeringSpell();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionTriggeringSpell.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFunctionTriggeringSpell : " + e.getMessage()));
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
            RunningEffectGroupLevelFunctionTriggeringSpell.m_logger.error((Object)"Unable to execute a RunningEffectGroupLevelFunctionTriggeringSpell without triggering effect");
            params.setForcedLevel(0);
            return params;
        }
        if (linkedRE.getTriggersToExecute().get(500)) {
            this.setParamsForcedLevel(params, (AbstractSpellLevel)this.m_context.getSpellCaster().getSpellLevel(), this.m_context.getSpellCaster().getLevelOfSpell());
            return params;
        }
        final WakfuEffectContainer effectContainer = ((RunningEffect<FX, WakfuEffectContainer>)linkedRE).getEffectContainer();
        if (effectContainer == null) {
            RunningEffectGroupLevelFunctionTriggeringSpell.m_logger.error((Object)("Unable to execute a RunningEffectGroupLevelFunctionTriggeringSpell when the triggering effect has no container. Effect id : " + ((RunningEffect<WakfuEffect, EC>)linkedRE).getGenericEffect().getEffectId()));
            params.setForcedLevel(0);
            return params;
        }
        if (effectContainer.getContainerType() != 11) {
            RunningEffectGroupLevelFunctionTriggeringSpell.m_logger.error((Object)("Unable to execute a RunningEffectGroupLevelFunctionTriggeringSpell when the triggering effect container is not a spell.Effect id : " + ((RunningEffect<WakfuEffect, EC>)linkedRE).getGenericEffect().getEffectId() + " ContainerType : " + effectContainer.getContainerType() + " Container ID : " + effectContainer.getEffectContainerId()));
            params.setForcedLevel(0);
            return params;
        }
        final short containerLevel = linkedRE.getContainerLevel();
        this.setParamsForcedLevel(params, (AbstractSpellLevel)effectContainer, containerLevel);
        return params;
    }
    
    private void setParamsForcedLevel(final WakfuEffectExecutionParameters params, final AbstractSpellLevel effectContainer, final short containerLevel) {
        final AbstractSpell spell = effectContainer.getSpell();
        final int apCost = spell.getActionPoints(containerLevel);
        final int mpCost = spell.getMovementPoints(containerLevel);
        final int wpCost = spell.getWakfuPoints(containerLevel);
        final float finalLevel = apCost * this.m_levelPerPA + mpCost * this.m_levelPerPM + wpCost * this.m_levelPerPW;
        params.setForcedLevel(Math.round(finalLevel));
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionTriggeringSpell>() {
            @Override
            public RunningEffectGroupLevelFunctionTriggeringSpell makeObject() {
                return new RunningEffectGroupLevelFunctionTriggeringSpell();
            }
        });
        PARAMETERS_LIST_SET_FUNCTION_TRIGGERING_SPELL = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Calcul le niveau des effets du groupe en fonction du cout du sort d\u00e9clenchant", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau par PA", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau par PM", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau par PW", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
