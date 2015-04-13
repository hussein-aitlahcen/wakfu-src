package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupSecondValueFunctionFirst extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_percentToTransmit;
    private int m_roundingType;
    private final int[] m_firstEffectValue;
    private int m_secondEffectMinValue;
    private boolean m_useCasterForSecondEffect;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupSecondValueFunctionFirst.PARAMETERS_LIST_SET;
    }
    
    public RunningEffectGroupSecondValueFunctionFirst() {
        super();
        this.m_firstEffectValue = new int[1];
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupSecondValueFunctionFirst newInstance() {
        RunningEffectGroupSecondValueFunctionFirst re;
        try {
            re = (RunningEffectGroupSecondValueFunctionFirst)RunningEffectGroupSecondValueFunctionFirst.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupSecondValueFunctionFirst.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupSecondValueFunctionFirst();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupSecondValueFunctionFirst.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupSecondValueFunctionFirst : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_percentToTransmit = 100;
        this.m_roundingType = 0;
        this.m_secondEffectMinValue = 0;
        this.m_useCasterForSecondEffect = true;
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_percentToTransmit = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_secondEffectMinValue = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_useCasterForSecondEffect = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 0);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            this.m_roundingType = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_genericEffect == null) {
            this.setNotified();
            return;
        }
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup == null) {
            RunningEffectGroupSecondValueFunctionFirst.m_logger.error((Object)("Groupe d'effet inconnu" + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            this.setNotified();
            return;
        }
        if (effectGroup.getEffectsCount() != 2) {
            RunningEffectGroupSecondValueFunctionFirst.m_logger.error((Object)("On ne peut pas avoir plus ou moins de deux effets dans un groupe d'effet de ce type " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            this.setNotified();
            return;
        }
        this.m_firstEffectValue[0] = 0;
        final WakfuEffectExecutionParameters params = this.getExecutionParameters((WakfuRunningEffect)triggerRE, true);
        if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
            params.setForcedLevel(this.getContainerLevel());
        }
        final WakfuEffect firstEffect = effectGroup.getEffect(0);
        firstEffect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_target, params, false);
        final int firstEffectValue = this.m_firstEffectValue[0];
        if (firstEffectValue == 0) {
            return;
        }
        final WakfuEffect secondEffect = effectGroup.getEffect(1);
        if (this.m_target instanceof AbstractBarrelEffectArea && secondEffect.getEffectId() == 101395) {
            return;
        }
        int roundedValue = 0;
        switch (this.m_roundingType) {
            case -1: {
                roundedValue = MathHelper.fastFloor(firstEffectValue * this.m_percentToTransmit / 100.0f);
                break;
            }
            case 0: {
                roundedValue = ValueRounder.randomRound(firstEffectValue * this.m_percentToTransmit / 100.0f);
                break;
            }
            case 1: {
                roundedValue = MathHelper.fastCeil(firstEffectValue * this.m_percentToTransmit / 100.0f);
                break;
            }
            default: {
                roundedValue = ValueRounder.randomRound(firstEffectValue * this.m_percentToTransmit / 100.0f);
                break;
            }
        }
        final int secondEffectValue = Math.max(roundedValue, this.m_secondEffectMinValue);
        params.setForcedValue(secondEffectValue);
        final EffectUser secondEffectTarget = this.m_useCasterForSecondEffect ? this.getCaster() : this.getTarget();
        secondEffect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), secondEffectTarget, params, false);
        params.release();
    }
    
    private WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setResetLimitedApplyCount(false);
        params.addListener(new WakfuRunningEffectListener() {
            @Override
            public void onAfterExecution(final WakfuRunningEffect effect) {
                if (effect.getValue() != 0) {
                    RunningEffectGroupSecondValueFunctionFirst.this.m_firstEffectValue[0] = effect.getValue();
                }
                params.removeListener(this);
            }
            
            @Override
            public void valueComputed(final WakfuRunningEffect effect) {
            }
        });
        return params;
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
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupSecondValueFunctionFirst>() {
            @Override
            public RunningEffectGroupSecondValueFunctionFirst makeObject() {
                return new RunningEffectGroupSecondValueFunctionFirst();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Transmet 100% de la valeur", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("% Param\u00e9tr\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% transmis (defaut = 100)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Valeur min pour le second effet", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% transmis (defaut = 100)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("valeur min du second effet (defaut = 0)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Caster ou target pour le second effet", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% transmis (defaut = 100)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("valeur min du second effet (defaut = 0)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Cible du second effet, 0=caster (defaut) 1=target", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Type d'arrondi", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% transmis (defaut = 100)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("valeur min du second effet (defaut = 0)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Cible du second effet, 0=caster (defaut) 1=target", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Arrondi du second effet (-1=inferieur, 0=random (default), 1=(sup\u00e9rieur)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
