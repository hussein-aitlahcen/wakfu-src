package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

abstract class EffectValueFunctionPaPm extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected Elements m_element;
    protected int m_remainingAP;
    protected int m_remainingMP;
    protected boolean m_doubleHPLoss;
    private boolean m_executeCost;
    
    EffectValueFunctionPaPm() {
        super();
        this.m_executeCost = true;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_caster == null) {
            return;
        }
        final short containerLevel = this.getContainerLevel();
        this.computeElement(containerLevel);
        this.m_remainingAP = this.m_caster.getCharacteristicValue(FighterCharacteristicType.AP);
        this.m_remainingMP = this.m_caster.getCharacteristicValue(FighterCharacteristicType.MP);
        if (this.m_remainingAP == 0 && this.m_remainingMP == 0) {
            return;
        }
        this.computeRemainingAp(containerLevel);
        this.computeRemainingMp(containerLevel);
        final float dmgPerAP = this.computeDmgPerAp(containerLevel);
        final float dmgPerMP = this.computeDmgPerMp(containerLevel);
        if (dmgPerAP == 0.0f) {
            this.m_remainingAP = 0;
        }
        if (dmgPerMP == 0.0f) {
            this.m_remainingMP = 0;
        }
        this.m_value = Math.round(dmgPerAP * this.m_remainingAP + dmgPerMP * this.m_remainingMP);
        if (this.m_value == 0) {
            return;
        }
        this.computeDoubleHpLoss(containerLevel);
        this.computeExecuteCost(containerLevel);
    }
    
    private void computeExecuteCost(final short containerLevel) {
        this.m_executeCost = true;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 7) {
            this.m_executeCost = (((WakfuEffect)this.m_genericEffect).getParam(6, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    protected void computeDoubleHpLoss(final short containerLevel) {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 4) {
            final float chancesToDoubleHPLoss = (this.m_remainingAP + this.m_remainingMP) * ((WakfuEffect)this.m_genericEffect).getParam(3, containerLevel);
            this.m_doubleHPLoss = (MathHelper.random(0.0f, 100.0f) <= chancesToDoubleHPLoss);
        }
        else {
            this.m_doubleHPLoss = false;
        }
    }
    
    protected void computeRemainingMp(final short containerLevel) {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 6) {
            final int maxMP = ((WakfuEffect)this.m_genericEffect).getParam(5, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (maxMP > 0) {
                this.m_remainingMP = Math.min(this.m_remainingMP, maxMP);
            }
        }
    }
    
    protected void computeRemainingAp(final short containerLevel) {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 5) {
            final int maxAP = ((WakfuEffect)this.m_genericEffect).getParam(4, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (maxAP > 0) {
                this.m_remainingAP = Math.min(this.m_remainingAP, maxAP);
            }
        }
    }
    
    protected float computeDmgPerMp(final short containerLevel) {
        return ((WakfuEffect)this.m_genericEffect).getParam(2, containerLevel);
    }
    
    protected float computeDmgPerAp(final short containerLevel) {
        return ((WakfuEffect)this.m_genericEffect).getParam(1, containerLevel);
    }
    
    protected abstract void computeElement(final short p0);
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified(true);
        if (this.m_caster == null || this.m_target == null) {
            return;
        }
        if (this.m_executeCost && this.isValueComputationEnabled()) {
            this.executeCost();
        }
        this.executeSubEffect();
        if (this.m_doubleHPLoss) {
            this.executeSubEffect();
        }
    }
    
    private void executeCost() {
        final ActionCost actionCost = ActionCost.checkOut((EffectContext<WakfuEffect>)this.m_context, new SpellCost((byte)this.m_remainingAP, (byte)this.m_remainingMP, (byte)0), this.m_caster);
        actionCost.setCaster(this.m_caster);
        actionCost.setRunningEffectStatus(RunningEffectStatus.NEUTRAL);
        actionCost.execute(null, false);
    }
    
    protected abstract void executeSubEffect();
    
    @Override
    public boolean dontTriggerAnything() {
        return true;
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
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_executeCost = true;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return EffectValueFunctionPaPm.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Element, valeur par PA, valeur par PM", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("valeur par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur par PM", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Element, valeur par PA, valeur par PM, et chance de doubler les d\u00e9gats en fonction des PA/PM", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("valeur par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur par PM", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("% de chances de doubler par PA/PM utilis\u00e9s", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Element, valeur par PA, valeur par PM, chance de doubler les d\u00e9gats en fonction des PA/PM, limite de PA/PM", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("valeur par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur par PM", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("% de chances de doubler par PA/PM utilis\u00e9s", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Max PA \u00e0 utiliser (-1 = tous)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Max PM \u00e0 utiliser (-1 = tous)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Element, valeur par PA, valeur par PM, chance de doubler les d\u00e9gats en fonction des PA/PM, limite de PA/PM, consomme les pa/pm", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("valeur par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur par PM", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("% de chances de doubler par PA/PM utilis\u00e9s", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Max PA \u00e0 utiliser (-1 = tous)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Max PM \u00e0 utiliser (-1 = tous)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Consomme les pa/pm (0 = non, 1 = oui (default))", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
