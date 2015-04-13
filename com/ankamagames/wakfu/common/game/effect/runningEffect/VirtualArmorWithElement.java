package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class VirtualArmorWithElement extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private final BinarSerialPart ADDITIONAL_DATA;
    private int m_armorLeft;
    private int m_percentToAbsorb;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return VirtualArmorWithElement.PARAMETERS_LIST_SET;
    }
    
    public VirtualArmorWithElement() {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(VirtualArmorWithElement.this.m_armorLeft);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public VirtualArmorWithElement newInstance() {
        VirtualArmorWithElement re;
        try {
            re = (VirtualArmorWithElement)VirtualArmorWithElement.m_staticPool.borrowObject();
            re.m_pool = VirtualArmorWithElement.m_staticPool;
        }
        catch (Exception e) {
            re = new VirtualArmorWithElement();
            re.m_pool = null;
            re.m_isStatic = false;
            VirtualArmorWithElement.m_logger.error((Object)("Erreur lors d'un checkOut sur un VirutalArmorFlat : " + e.getMessage()));
        }
        re.m_armorLeft = this.m_armorLeft;
        re.m_percentToAbsorb = this.m_percentToAbsorb;
        return re;
    }
    
    private void initialiseArmor() {
        final short level = this.getContainerLevel();
        this.m_armorLeft = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.m_caster == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() <= 1) {
            return;
        }
        final byte elementId = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final Elements element = Elements.getElementFromId(elementId);
        if (element == null) {
            VirtualArmorWithElement.m_logger.error((Object)("L'element specifie n'existe pas : " + elementId));
            return;
        }
        this.m_armorLeft = ValueRounder.randomRound(this.m_armorLeft * (1.0f + this.getBonusModificator(element)));
    }
    
    private float getBonusModificator(final Elements element) {
        return (this.m_caster.getCharacteristicValue(element.getDamageBonusCharacteristic()) + this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT)) / 100.0f;
    }
    
    @Override
    public void onApplication() {
        super.onApplication();
        if (this.isValueComputationEnabled()) {
            this.initialiseArmor();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.getParent() != null && this.getParent().getId() == this.getId()) {
            this.m_armorLeft = ((VirtualArmorWithElement)this.getParent()).m_armorLeft;
        }
        this.m_percentToAbsorb = 100;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_percentToAbsorb = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
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
            ((VirtualArmorWithElement)this.getParent()).substractArmor(this.m_value);
        }
    }
    
    public void substractArmor(final int value) {
        this.m_armorLeft -= value;
        if (this.m_armorLeft <= 0) {
            this.m_maxExecutionCount = 0;
        }
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
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATA;
    }
    
    @Override
    public void onCheckIn() {
        this.m_armorLeft = 0;
        this.m_percentToAbsorb = 0;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<VirtualArmorWithElement>() {
            @Override
            public VirtualArmorWithElement makeObject() {
                return new VirtualArmorWithElement();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Bouclier en valeur fixe", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur d'armure", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Bouclier \u00e9l\u00e9mentaire", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur d'armure", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("% absorb\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur d'armure", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% absorb\u00e9 (d\u00e9faut 100%)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
