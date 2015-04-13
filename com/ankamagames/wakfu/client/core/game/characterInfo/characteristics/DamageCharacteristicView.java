package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss.*;

public class DamageCharacteristicView extends CharacteristicView
{
    private final CharacterInfo m_info;
    private final FighterCharacteristic m_reverseCharac;
    private final boolean m_isDamage;
    
    public DamageCharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider, final CharacterInfo info, final FighterCharacteristic reverseCharac, final boolean damage) {
        super(charac, provider);
        this.m_info = info;
        this.m_reverseCharac = reverseCharac;
        this.m_isDamage = damage;
    }
    
    @Override
    protected Object getPercentageDescription() {
        return this.getPercentageDescription(false);
    }
    
    @Override
    protected Object getFormattedPercentageDescription() {
        return this.getPercentageDescription(true);
    }
    
    private String getPercentageDescription(final boolean formatted) {
        int value;
        int max;
        if (!this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
            final int added = this.m_isDamage ? this.m_info.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT) : this.m_info.getCharacteristicValue(FighterCharacteristicType.RES_IN_PERCENT);
            value = this.m_charac.value() + added;
            max = this.m_charac.max() + added;
        }
        else {
            final int added = this.m_isDamage ? this.m_info.getCharacteristicValue(FighterCharacteristicType.RES_IN_PERCENT) : this.m_info.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
            value = this.m_reverseCharac.value() + added;
            max = this.m_reverseCharac.max() + added;
        }
        if (!this.m_isDamage && SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.NEW_HP_LOSS_FORMULA)) {
            final int damageReduction = (int)Math.round(100.0 - HpLossComputerImpl.computeReduction(value) * 100.0);
            return CharacteristicsUtil.displayPercentageAndBaseValue(damageReduction, max, value);
        }
        return CharacteristicsUtil.displayPercentage(value, max, formatted);
    }
}
