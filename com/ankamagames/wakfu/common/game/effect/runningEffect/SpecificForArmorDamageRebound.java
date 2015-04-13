package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SpecificForArmorDamageRebound extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    private int m_baseValue;
    private float m_valueRatio;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SpecificForArmorDamageRebound.PARAMETERS_LIST_SET;
    }
    
    public SpecificForArmorDamageRebound() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SpecificForArmorDamageRebound newInstance() {
        SpecificForArmorDamageRebound re;
        try {
            re = (SpecificForArmorDamageRebound)SpecificForArmorDamageRebound.m_staticPool.borrowObject();
            re.m_pool = SpecificForArmorDamageRebound.m_staticPool;
        }
        catch (Exception e) {
            re = new SpecificForArmorDamageRebound();
            re.m_pool = null;
            re.m_isStatic = false;
            SpecificForArmorDamageRebound.m_logger.error((Object)("Erreur lors d'un checkOut sur un ElementalVirutalArmor : " + e.getMessage()));
        }
        re.m_element = this.m_element;
        re.m_baseValue = this.m_baseValue;
        re.m_valueRatio = this.m_valueRatio;
        return re;
    }
    
    @Override
    public void onApplication() {
        if (this.getParent() == null) {
            this.initialiseArmor();
        }
    }
    
    private void initialiseArmor() {
        final byte elementId = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_element = Elements.getElementFromId(elementId);
        if (this.m_element == null) {
            SpecificForArmorDamageRebound.m_logger.error((Object)("Element inconnu " + elementId));
            return;
        }
        this.m_baseValue = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.getParams() == null || !((WakfuEffectExecutionParameters)this.getParams()).isValueForced()) {
            int modificator = 0;
            if (this.m_caster.hasCharacteristic(this.m_element.getDamageBonusCharacteristic())) {
                modificator += this.m_caster.getCharacteristicValue(this.m_element.getDamageBonusCharacteristic());
            }
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
                modificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
            }
            this.m_value = this.m_baseValue + this.m_baseValue * modificator / 100;
            this.m_valueRatio = this.m_baseValue / this.m_value;
        }
        else {
            this.forceValue(((WakfuEffectExecutionParameters)this.getParams()).getForcedValue());
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.getParent() != null && this.getParent().getId() == this.getId()) {
            this.m_value = this.getParent().getValue();
        }
    }
    
    @Override
    public void forceValue(final int forcedValue) {
        if (this.getParent() != null && this.getParent().getId() == this.getId()) {
            this.m_value = this.getParent().getValue();
        }
        else {
            super.forceValue(forcedValue);
            this.m_valueRatio = this.m_baseValue / forcedValue;
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (triggerRE != null && trigger) {
            this.updateWithArmor(triggerRE);
        }
    }
    
    private void updateWithArmor(final RunningEffect triggerRE) {
        final EffectUser hpLossTarget = triggerRE.getTarget();
        if (hpLossTarget == null) {
            return;
        }
        final int originalHpLoss = triggerRE.getValue();
        this.m_value -= originalHpLoss;
        final int hpLossNotAbsorbed = Math.max(0, -this.m_value);
        final int absorbedHpLoss = originalHpLoss - hpLossNotAbsorbed;
        if (absorbedHpLoss > 0) {
            triggerRE.update(1, -absorbedHpLoss, false);
        }
        if (this.getParent() != null && this.getParent().getId() == this.getId()) {
            ((SpecificForArmorDamageRebound)this.getParent()).substractArmor(this.m_value);
        }
        if (triggerRE.dontTriggerAnything()) {
            return;
        }
        final int baseValueForDamageReturn = ValueRounder.randomRound(absorbedHpLoss * this.m_valueRatio);
        this.executeSubEffect(triggerRE, baseValueForDamageReturn);
    }
    
    private void executeSubEffect(final RunningEffect triggerRE, final int value) {
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup == null) {
            return;
        }
        if (effectGroup.getEffectsCount() != 1) {
            SpecificForArmorDamageRebound.m_logger.error((Object)("On ne peut pas qu'un seul effet dans un groupe d'effet de ce type " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            this.setNotified();
            return;
        }
        final HpLossComputer hpLossComputer = new HpLossComputerImpl(this.m_caster, this.m_target, this.m_element, (WakfuEffect)this.m_genericEffect);
        hpLossComputer.setConditions(11);
        hpLossComputer.setValue(value);
        hpLossComputer.computeWithModificator();
        RunningEffectUtils.setTriggerForElement(hpLossComputer.getElementForResistance(), this);
        final int hpLossValue = hpLossComputer.getValue();
        final WakfuEffectExecutionParameters params = this.getExecutionParameters((WakfuRunningEffect)triggerRE, true);
        params.setForcedValue(hpLossValue);
        final WakfuEffect firstEffect = effectGroup.getEffect(0);
        firstEffect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_target, params, false);
    }
    
    private WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setResetLimitedApplyCount(false);
        return params;
    }
    
    public void substractArmor(final int value) {
        this.m_value = value;
        if (this.m_value <= 0) {
            this.m_maxExecutionCount = 0;
        }
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
        this.m_element = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SpecificForArmorDamageRebound>() {
            @Override
            public SpecificForArmorDamageRebound makeObject() {
                return new SpecificForArmorDamageRebound();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur et Element", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
