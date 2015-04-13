package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class VirtualArmor extends WakfuRunningEffect
{
    private static final int MAX_VALUE_PERCENT_TYPE = 0;
    private static final MonitoredPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_armorLeft;
    private int m_percentToAbsorb;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return VirtualArmor.PARAMETERS_LIST_SET;
    }
    
    @Override
    public VirtualArmor newInstance() {
        VirtualArmor result;
        try {
            result = (VirtualArmor)VirtualArmor.m_staticPool.borrowObject();
            result.m_pool = VirtualArmor.m_staticPool;
        }
        catch (Exception e) {
            VirtualArmor.m_logger.warn((Object)("Erreur lors de newInstance sur un " + this.getClass().getSimpleName()));
            result = new VirtualArmor();
            result.m_pool = null;
            result.m_isStatic = false;
        }
        result.m_armorLeft = this.m_armorLeft;
        result.m_percentToAbsorb = this.m_percentToAbsorb;
        return result;
    }
    
    private void initialiseArmor() {
        final short level = this.getContainerLevel();
        this.m_armorLeft = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.m_caster == null) {
            return;
        }
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.VIRTUAL_ARMOR_BONUS)) {
            this.m_armorLeft += this.m_caster.getCharacteristicValue(FighterCharacteristicType.VIRTUAL_ARMOR_BONUS);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() <= 1) {
            return;
        }
        if (!this.m_caster.hasCharacteristic(FighterCharacteristicType.HP)) {
            return;
        }
        final int percentType = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
        if (this.isMaxValuePercentType(percentType)) {
            this.m_armorLeft = this.m_caster.getCharacteristic(FighterCharacteristicType.HP).max() * this.m_armorLeft / 100;
        }
        else {
            this.m_armorLeft = this.m_caster.getCharacteristic(FighterCharacteristicType.HP).value() * this.m_armorLeft / 100;
        }
        this.m_armorLeft = Math.max(this.m_armorLeft, 1);
    }
    
    private boolean isMaxValuePercentType(final int percentType) {
        return percentType == 0;
    }
    
    public int getArmorLeft() {
        return this.m_armorLeft;
    }
    
    public void substractArmor(final int value) {
        this.m_armorLeft -= value;
        if (this.m_armorLeft <= 0) {
            this.m_maxExecutionCount = 0;
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.getParent() != null && this.getParent().getId() == this.getId()) {
            this.m_armorLeft = ((VirtualArmor)this.getParent()).getArmorLeft();
        }
        this.m_percentToAbsorb = 100;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_percentToAbsorb = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    public void onApplication() {
        super.onApplication();
        this.initialiseArmor();
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.isValueComputationEnabled() && triggerRE != null && trigger) {
            this.updateWithArmor(triggerRE);
        }
        else {
            this.m_value = this.m_armorLeft;
        }
    }
    
    private void updateWithArmor(final RunningEffect triggerRE) {
        final EffectUser hpLossTarget = triggerRE.getTarget();
        if (hpLossTarget == null) {
            return;
        }
        final int armorLeftBefore = this.m_armorLeft;
        final int originalHpLoss = triggerRE.getValue();
        final int hpLossToAbsorb = (this.m_percentToAbsorb < 100) ? (originalHpLoss * this.m_percentToAbsorb / 100) : originalHpLoss;
        this.m_armorLeft -= hpLossToAbsorb;
        final int hpLossNotAbsorbed = Math.max(0, -this.m_armorLeft) + originalHpLoss - hpLossToAbsorb;
        triggerRE.update(1, hpLossNotAbsorbed, true);
        this.m_value = armorLeftBefore - Math.max(0, this.m_armorLeft);
        if (this.getParent() != null && this.getParent().getId() == this.getId()) {
            ((VirtualArmor)this.getParent()).substractArmor(this.m_value);
        }
    }
    
    @Override
    public void onCheckIn() {
        this.m_percentToAbsorb = 0;
        super.onCheckIn();
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<VirtualArmor>() {
            @Override
            public VirtualArmor makeObject() {
                return new VirtualArmor();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Absorbe une certaines quantit\u00e9s de d\u00e9gats, puis se d\u00e9sapplique", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur d'armure", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Valeur en % des Hp", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur en %", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("En fonction de : Valeur max (0) ou Courante (1)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("% de perte de pdv absorb\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur en %", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("En fonction de : Valeur max (0) ou Courante (1)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% des pertes de pdv a absorber (defaut 100%)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
