package com.ankamagames.wakfu.common.game.effect.runningEffect.util;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public final class OsamodasCreatureCharacModifier
{
    private CharacteristicManager m_manager;
    private final List<FighterCharacteristicType> m_primaryCharacteristics;
    private final List<FighterCharacteristicType> m_secondaryCharacteristics;
    public static final OsamodasCreatureCharacModifier INSTANCE;
    
    private OsamodasCreatureCharacModifier() {
        super();
        (this.m_primaryCharacteristics = new ArrayList<FighterCharacteristicType>()).add(FighterCharacteristicType.HP);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.VIRTUAL_HP);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.DMG_AIR_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.DMG_FIRE_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.DMG_EARTH_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.DMG_WATER_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.DMG_IN_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.RES_AIR_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.RES_FIRE_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.RES_EARTH_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.RES_WATER_PERCENT);
        this.m_primaryCharacteristics.add(FighterCharacteristicType.RES_IN_PERCENT);
        (this.m_secondaryCharacteristics = new ArrayList<FighterCharacteristicType>()).add(FighterCharacteristicType.DODGE);
        this.m_secondaryCharacteristics.add(FighterCharacteristicType.TACKLE);
        this.m_secondaryCharacteristics.add(FighterCharacteristicType.INIT);
    }
    
    public void modifyCharac(final CharacteristicManager manager, final float primaryCharacteristicModifier, final float secondaryCharacteristicModifier) {
        this.m_manager = manager;
        for (int i = 0, n = this.m_primaryCharacteristics.size(); i < n; ++i) {
            final FighterCharacteristicType type = this.m_primaryCharacteristics.get(i);
            this.nerfCharac(type, primaryCharacteristicModifier);
        }
        for (int i = 0, n = this.m_secondaryCharacteristics.size(); i < n; ++i) {
            final FighterCharacteristicType type = this.m_secondaryCharacteristics.get(i);
            this.nerfCharac(type, secondaryCharacteristicModifier);
        }
        this.m_manager = null;
    }
    
    private void nerfCharac(final FighterCharacteristicType characType, final float modifier) {
        final AbstractCharacteristic characteristic = this.m_manager.getCharacteristic(characType);
        final int value = characteristic.value();
        characteristic.set(Math.round(value - Math.abs(value * (1.0f - modifier))));
        characteristic.setMax(Math.round(characteristic.max() * modifier));
    }
    
    public int getModifiedValue(final FighterCharacteristicType charac, final short level, final MonsterBreedCharacteristicManager characteristicManager, final float primaryCharacteristicModifier, final float secondaryCharacteristicModifier) {
        float modifier;
        if (this.m_primaryCharacteristics.contains(charac)) {
            modifier = primaryCharacteristicModifier;
        }
        else if (this.m_secondaryCharacteristics.contains(charac)) {
            modifier = secondaryCharacteristicModifier;
        }
        else {
            modifier = 1.0f;
        }
        final int characteristicValue = characteristicManager.getCharacteristicValue(charac, level);
        return Math.round(characteristicValue - Math.abs(characteristicValue * (1.0f - modifier)));
    }
    
    static {
        INSTANCE = new OsamodasCreatureCharacModifier();
    }
}
