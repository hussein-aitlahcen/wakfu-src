package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public enum Elements implements ExportableEnum
{
    PHYSICAL((byte)0, (FighterCharacteristicType)null, (FighterCharacteristicType)null, (FighterCharacteristicType)null, 17, false), 
    FIRE((byte)1, FighterCharacteristicType.FIRE_MASTERY, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, 14, true), 
    WATER((byte)2, FighterCharacteristicType.WATER_MASTERY, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, 13, true), 
    EARTH((byte)3, FighterCharacteristicType.EARTH_MASTERY, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, 15, true), 
    AIR((byte)4, FighterCharacteristicType.AIR_MASTERY, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, 16, true), 
    STASIS((byte)5, FighterCharacteristicType.STASIS_MASTERY, FighterCharacteristicType.DMG_STASIS_PERCENT, FighterCharacteristicType.RES_STASIS_PERCENT, 19, true), 
    SUPPORT((byte)9, (FighterCharacteristicType)null, (FighterCharacteristicType)null, (FighterCharacteristicType)null, 18, false);
    
    public static final byte PHYSICAL_DEF = 0;
    public static final byte FIRE_DEF = 1;
    public static final byte WATER_DEF = 2;
    public static final byte EARTH_DEF = 3;
    public static final byte AIR_DEF = 4;
    public static final byte STASIS_DEF = 5;
    public static final byte SUPPORT_DEF = 9;
    private final byte m_id;
    private final FighterCharacteristicType m_masteryCharacteristic;
    private final FighterCharacteristicType m_damageBonusCharacteristic;
    private final FighterCharacteristicType m_resistanceBonusCharacteristic;
    private final boolean m_isElemental;
    private final int m_effectTrigger;
    
    public static Elements getElementFromId(final byte id) {
        for (final Elements element : values()) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }
    
    private Elements(final byte id, final FighterCharacteristicType masteryCharacteristic, final FighterCharacteristicType damageBonusCharacteristic, final FighterCharacteristicType resistanceBonusCharacteristic, final int effectTrigger, final boolean elemental) {
        this.m_id = id;
        this.m_masteryCharacteristic = masteryCharacteristic;
        this.m_damageBonusCharacteristic = damageBonusCharacteristic;
        if (this.m_damageBonusCharacteristic != null) {
            this.m_damageBonusCharacteristic.setRelatedElement(this);
        }
        this.m_resistanceBonusCharacteristic = resistanceBonusCharacteristic;
        if (this.m_resistanceBonusCharacteristic != null) {
            this.m_resistanceBonusCharacteristic.setRelatedElement(this);
        }
        this.m_effectTrigger = effectTrigger;
        this.m_isElemental = elemental;
    }
    
    @Override
    public String getEnumId() {
        return Byte.toString(this.getId());
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public FighterCharacteristicType getMasteryCharacteristic() {
        return this.m_masteryCharacteristic;
    }
    
    public FighterCharacteristicType getDamageBonusCharacteristic() {
        return this.m_damageBonusCharacteristic;
    }
    
    public FighterCharacteristicType getResistanceBonusCharacteristic() {
        return this.m_resistanceBonusCharacteristic;
    }
    
    public boolean hasMasteryCharacteristic() {
        return this.m_masteryCharacteristic != null;
    }
    
    public boolean isElemental() {
        return this.m_isElemental;
    }
    
    public int getEffectTrigger() {
        return this.m_effectTrigger;
    }
}
