package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class SetTrap extends SetEffectArea
{
    private static final ObjectPool m_trapEffectAreaPool;
    
    @Override
    protected ObjectPool getPool() {
        return SetTrap.m_trapEffectAreaPool;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        super.executeOverride(linkedRE, trigger);
        this.initializeTrapDmgCharac();
    }
    
    private void initializeTrapDmgCharac() {
        if (this.m_caster == null) {
            return;
        }
        if (this.m_area == null) {
            return;
        }
        if (!this.m_area.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            return;
        }
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.SUMMONING_MASTERY) && this.m_caster.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            this.m_area.getCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT).add(this.m_caster.getCharacteristicValue(FighterCharacteristicType.SUMMONING_MASTERY));
            this.m_area.getCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT).add(this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT));
        }
        if (this.m_area.hasCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT)) {
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT)) {
                this.m_area.getCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT).add(this.m_caster.getCharacteristicValue(FighterCharacteristicType.HEAL_IN_PERCENT));
            }
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.DMG_WATER_PERCENT)) {
                this.m_area.getCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT).add(this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_WATER_PERCENT));
            }
        }
        if (this.m_area.isActiveProperty(EffectAreaPropertyType.USE_ALL_DMG)) {
            this.m_area.initCharac(FighterCharacteristicType.DMG_FIRE_PERCENT, this.m_caster);
            this.m_area.initCharac(FighterCharacteristicType.DMG_WATER_PERCENT, this.m_caster);
            this.m_area.initCharac(FighterCharacteristicType.DMG_AIR_PERCENT, this.m_caster);
            this.m_area.initCharac(FighterCharacteristicType.DMG_EARTH_PERCENT, this.m_caster);
            this.m_area.initCharac(FighterCharacteristicType.AOE_DMG, this.m_caster);
            this.m_area.initCharac(FighterCharacteristicType.SINGLE_TARGET_DMG, this.m_caster);
            this.m_area.initCharac(FighterCharacteristicType.MELEE_DMG, this.m_caster);
            this.m_area.initCharac(FighterCharacteristicType.RANGED_DMG, this.m_caster);
        }
        else if (((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 11) {
            final AbstractSpellLevel spellContainer = (AbstractSpellLevel)this.m_effectContainer;
            final byte trapElementId = spellContainer.getSpell().getElementId();
            final Elements element = Elements.getElementFromId(trapElementId);
            if (this.m_caster.hasCharacteristic(element.getDamageBonusCharacteristic())) {
                this.m_area.getCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT).add(this.m_caster.getCharacteristicValue(element.getDamageBonusCharacteristic()));
            }
        }
    }
    
    static {
        m_trapEffectAreaPool = new MonitoredPool(new ObjectFactory<SetTrap>() {
            @Override
            public SetTrap makeObject() {
                return new SetTrap();
            }
        });
    }
}
