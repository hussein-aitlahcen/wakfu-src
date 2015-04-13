package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

abstract class SpellsWithPropertyModification extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private final BinarSerialPart m_data;
    protected int m_propertyId;
    
    SpellsWithPropertyModification() {
        super();
        this.m_data = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(SpellsWithPropertyModification.this.m_propertyId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SpellsWithPropertyModification.this.m_propertyId = buffer.getInt();
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SpellsWithPropertyModification.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_propertyId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_data;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            this.setNotified();
            return;
        }
        final SpellPropertyType propertyType = SpellPropertyType.getPropertyFromId(this.m_propertyId);
        if (propertyType == null) {
            SpellsWithPropertyModification.m_logger.error((Object)("Propri\u00e9t\u00e9 inconnue " + this.m_propertyId + " m_effectId = " + this.getEffectId()));
            this.setNotified();
            return;
        }
        this.addSpellsModification(propertyType);
    }
    
    @Override
    public void unapplyOverride() {
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        final SpellPropertyType propertyType = SpellPropertyType.getPropertyFromId(this.m_propertyId);
        if (propertyType == null) {
            SpellsWithPropertyModification.m_logger.error((Object)("Propri\u00e9t\u00e9 inconnue " + this.m_propertyId + " m_effectId = " + this.getEffectId()));
            return;
        }
        this.unapplySpellsModification(propertyType);
    }
    
    abstract void addSpellsModification(final SpellPropertyType p0);
    
    abstract void unapplySpellsModification(final SpellPropertyType p0);
    
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
        super.onCheckIn();
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Propri\u00e9t\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Propri\u00e9t\u00e9", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
