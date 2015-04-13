package com.ankamagames.wakfu.common.game.fighter;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public enum FighterCharacteristicType implements CharacteristicType, ExportableEnum
{
    HP(1, 0, Integer.MAX_VALUE, 0, 50, 50, true, false, 3, 4, 1, 2, true, -1), 
    AP(2, 0, Integer.MAX_VALUE, 0, 6, 6, true, 53, 54, 51, 52, 12), 
    MP(3, 0, Integer.MAX_VALUE, 0, 3, 3, true, 63, 64, 61, 62, 7), 
    WP(4, 0, 20, 0, 10, 10, true, 69, 70, 67, 68), 
    TACKLE(7, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0), 
    DODGE(8, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0), 
    FEROCITY(9, 0, 100, 0, 100, 3), 
    FUMBLE_RATE(10, 0, 100, 0, 100, 0, false, true), 
    RANGE(11, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, false, 207, 208, 205, 206), 
    LEADERSHIP(12, 0, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1), 
    MECHANICS(13, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 1), 
    KO_TIME_BEFORE_DEATH(14, 0, Integer.MAX_VALUE, 0, 3, 3, true), 
    BACKSTAB_BONUS(16, -5000, 5000, -5000, 5000, 0), 
    RES_BACKSTAB(17, -5000, 5000, -5000, 5000, 0), 
    HEAL_IN_PERCENT(18, -5000, 5000, -5000, 5000, 0), 
    MECHANISM_MASTERY(19, 0, 1000, 0, 1000, 0), 
    DMG_IN_PERCENT(20, -5000, 5000, -5000, 5000, 0, false, -1, -1, 121, 122), 
    DMG_FIRE_PERCENT(21, -5000, 5000, -5000, 5000, 0, false, -1, -1, 161, 162), 
    DMG_WATER_PERCENT(22, -5000, 5000, -5000, 5000, 0, false, -1, -1, 131, 132), 
    DMG_EARTH_PERCENT(23, -5000, 5000, -5000, 5000, 0, false, -1, -1, 141, 142), 
    DMG_AIR_PERCENT(24, -5000, 5000, -5000, 5000, 0, false, -1, -1, 151, 152), 
    DMG_REBOUND(25, 0, 99, 0, 99, 0), 
    DMG_ABSORB(26, 0, 99, 0, 99, 0), 
    RES_IN_PERCENT(27, -5000, 5000, -5000, 5000, 0, false, -1, -1, 71, 72), 
    RES_FIRE_PERCENT(28, -5000, 5000, -5000, 5000, 0, false, -1, -1, 111, 112), 
    RES_WATER_PERCENT(29, -5000, 5000, -5000, 5000, 0, false, -1, -1, 81, 82), 
    RES_EARTH_PERCENT(30, -5000, 5000, -5000, 5000, 0, false, -1, -1, 91, 92), 
    RES_AIR_PERCENT(31, -5000, 5000, -5000, 5000, 0, false, -1, -1, 101, 102), 
    RES_HEAL_PERCENT(32, -1000, 1000, -1000, 1000, 0), 
    PROSPECTION(33, -1000, 1000, -1000, 1000, 0), 
    INIT(35, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, false, 171, 172, 173, 174), 
    WISDOM(36, -500, 150, -500, 150, 0), 
    WATER_MASTERY(37, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    AIR_MASTERY(38, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    EARTH_MASTERY(39, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    FIRE_MASTERY(40, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    VITALITY(41, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    MP_DEBUFF_RES(42, -1000, 1000, -1000, 1000, 0), 
    AP_DEBUFF_RES(43, -1000, 1000, -1000, 1000, 0), 
    EQUIPMENT_KNOWLEDGE(45, 0, 10, 0, 10, 0), 
    AP_DEBUFF_POWER(46, 0, 1000, 0, 1000, 0), 
    MP_DEBUFF_POWER(47, 0, 1000, 0, 1000, 0), 
    FINAL_DMG_IN_PERCENT(48, -1000, 1000, -1000, 1000, 0), 
    FINAL_RES_IN_PERCENT(49, -1000, 1000, -1000, 1000, 0), 
    CHRAGE(50, 0, 100, 0, 100, 0, false, -1, -1, 2141, 2142), 
    CRITICAL_BONUS(52, -5000, 5000, -5000, 5000, 0), 
    LIFE_STOLEN_BONUS(53, -100, 100, -100, 100, 0), 
    VOODOOL_HP_LOSS_BONUS(54, -1000, 1000, -1000, 1000, 0), 
    SUMMONING_MASTERY(55, 0, 1000, 0, 1000, 0), 
    DMG_VS_SUMMMONS(56, -1000, 1000, -1000, 1000, 0), 
    OCCUPATION_RESOURCEFULNESS(65, -1000, Integer.MAX_VALUE, -1000, Integer.MAX_VALUE, 0), 
    OCCUPATION_HARVEST_QUICKNESS(66, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    OCCUPATION_CRAFT_QUICKNESS(67, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    OCCUPATION_QUICK_LEARNER_HARVEST(70, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    OCCUPATION_QUICK_LEARNER_CRAFT(71, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0), 
    OCCUPATION_GREEN_THUMBS(72, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    ECAFLIP_DOUBLE_OR_QUITS_BUFF(73, -10, 100, -100, 100, 0), 
    BOMB_COOLDOWN(74, 0, Integer.MAX_VALUE, 0, 10, 0), 
    ARMOR_PLATE(75, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    ARMOR_PLATE_BONUS(76, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    STATE_APPLICATION_BONUS(77, Integer.MIN_VALUE, 100, Integer.MIN_VALUE, 100, 0), 
    STATE_RESISTANCE_BONUS(78, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0), 
    FECA_GLYPH_CHARGE_BONUS(79, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    XELORS_DIAL_CHARGE(80, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0, false, -1, -1, -1, 2150), 
    PERCEPTION(81, -500, 500, -500, 500, 0, false, 2154, 2155, 2152, 2153), 
    STRENGTH(82, 0, 250, 0, 250, 0), 
    INTELLIGENCE(83, 0, 250, 0, 250, 0), 
    AGILITY(84, 0, 250, 0, 250, 0), 
    LUCK(85, 0, 250, 0, 250, 0), 
    WILLPOWER(86, 0, 100, 0, 100, 0), 
    BLOCK(87, 0, 100, 0, 100, 0, false), 
    VIRTUAL_ARMOR_BONUS(88, 0, 100, 0, 100, 0, false), 
    AREA_HP(89, 0, Integer.MAX_VALUE, 0, 10, 0, true, -1, -1, 2167, 2168), 
    DMG_STASIS_PERCENT(90, -5000, 5000, -5000, 5000, 0, false, -1, -1, 165, 166), 
    RES_STASIS_PERCENT(91, -5000, 5000, -5000, 5000, 0, false, -1, -1, 115, 116), 
    VIRTUAL_HP(92, 0, Integer.MAX_VALUE, 0, 0, 0, true), 
    STEAMER_MICROBOT_MAX_DISTANCE(93, 0, 100, 0, 100, 0, false), 
    TOTAL_HP(94, 0, 0, 0, 0, 0, false), 
    CRITICAL_RES(95, -5000, 5000, -5000, 5000, 0), 
    OSA_INVOCATION_KNOWLEDGE(96, 0, 100, 0, 100, 0), 
    ARMOR(97, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    STASIS_MASTERY(98, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    GREED(99, -500, 500, -500, 500, 0), 
    BARRIER(100, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0), 
    NON_CRIT_DMG(102, -5000, 5000, -5000, 5000, 0), 
    MELEE_DMG(103, -5000, 5000, -5000, 5000, 0), 
    RANGED_DMG(104, -5000, 5000, -5000, 5000, 0), 
    SINGLE_TARGET_DMG(105, -5000, 5000, -5000, 5000, 0), 
    AOE_DMG(106, -5000, 5000, -5000, 5000, 0), 
    BERSERK_DMG(107, -5000, 5000, -5000, 5000, 0);
    
    private final byte m_id;
    private final int m_lowerBound;
    private final int m_upperBound;
    private final int m_defaultMin;
    private final int m_defaultMax;
    private final int m_outFightMax;
    private final int m_defaultValue;
    private final boolean m_isExpandable;
    private final int m_buffTrigger;
    private final int m_debuffTrigger;
    private final int m_gainTrigger;
    private final int m_lossTrigger;
    private Elements m_relatedElement;
    private final boolean m_negative;
    protected final boolean m_currentCanExceedMax;
    private static int MAX_INDEX;
    
    private FighterCharacteristicType(final int id, final int lowerBound, final int upperBound, final int defaultMin, final int defaultMax, final int defaultValue) {
        this(id, lowerBound, upperBound, defaultMin, defaultMax, defaultValue, false, false, -1, -1, -1, -1, false, -1);
    }
    
    private FighterCharacteristicType(final int id, final int lowerBound, final int upperBound, final int defaultMin, final int defaultMax, final int defaultValue, final boolean expandable) {
        this(id, lowerBound, upperBound, defaultMin, defaultMax, defaultValue, expandable, false, -1, -1, -1, -1, false, -1);
    }
    
    private FighterCharacteristicType(final int id, final int lowerBound, final int upperBound, final int defaultMin, final int defaultMax, final int defaultValue, final boolean expandable, final boolean negative) {
        this(id, lowerBound, upperBound, defaultMin, defaultMax, defaultValue, expandable, negative, -1, -1, -1, -1, false, -1);
    }
    
    private FighterCharacteristicType(final int id, final int lowerBound, final int upperBound, final int defaultMin, final int defaultMax, final int defaultValue, final boolean expandable, final int buffTrigger, final int debuffTrigger, final int gainTrigger, final int lossTrigger) {
        this(id, lowerBound, upperBound, defaultMin, defaultMax, defaultValue, expandable, false, buffTrigger, debuffTrigger, gainTrigger, lossTrigger, false, -1);
    }
    
    private FighterCharacteristicType(final int id, final int lowerBound, final int upperBound, final int defaultMin, final int defaultMax, final int defaultValue, final boolean expandable, final int buffTrigger, final int debuffTrigger, final int gainTrigger, final int lossTrigger, final int outFightMax) {
        this(id, lowerBound, upperBound, defaultMin, defaultMax, defaultValue, expandable, false, buffTrigger, debuffTrigger, gainTrigger, lossTrigger, false, outFightMax);
    }
    
    private FighterCharacteristicType(final int id, final int lowerBound, final int upperBound, final int defaultMin, final int defaultMax, final int defaultValue, final boolean expandable, final boolean negative, final int buffTrigger, final int debuffTrigger, final int gainTrigger, final int lossTrigger, final boolean currentCanExceedMax, final int outFightMax) {
        this.m_relatedElement = null;
        this.m_currentCanExceedMax = currentCanExceedMax;
        this.m_id = (byte)id;
        this.m_lowerBound = lowerBound;
        this.m_upperBound = upperBound;
        this.m_defaultMin = defaultMin;
        this.m_defaultMax = defaultMax;
        this.m_defaultValue = defaultValue;
        this.m_isExpandable = expandable;
        this.m_buffTrigger = buffTrigger;
        this.m_debuffTrigger = debuffTrigger;
        this.m_gainTrigger = gainTrigger;
        this.m_lossTrigger = lossTrigger;
        this.m_negative = negative;
        this.m_outFightMax = outFightMax;
    }
    
    @Override
    public byte getId() {
        return this.m_id;
    }
    
    @Override
    public byte getCharacteristicType() {
        return 0;
    }
    
    public void setRelatedElement(final Elements element) {
        if (this.m_relatedElement != null) {
            throw new RuntimeException("Trying to associate element " + element + " with carac " + this + " but charac is already associated to " + this.m_relatedElement);
        }
        this.m_relatedElement = element;
    }
    
    @Override
    public int getLowerBound() {
        return this.m_lowerBound;
    }
    
    @Override
    public int getUpperBound() {
        return this.m_upperBound;
    }
    
    @Override
    public int getDefaultMin() {
        return this.m_defaultMin;
    }
    
    @Override
    public int getDefaultMax() {
        return this.m_defaultMax;
    }
    
    @Override
    public int getDefaultValue() {
        return this.m_defaultValue;
    }
    
    @Override
    public boolean isExpandable() {
        return this.m_isExpandable;
    }
    
    @Override
    public boolean isNegative() {
        return this.m_negative;
    }
    
    public int getBuffTrigger() {
        return this.m_buffTrigger;
    }
    
    public int getDebuffTrigger() {
        return this.m_debuffTrigger;
    }
    
    public int getGainTrigger() {
        return this.m_gainTrigger;
    }
    
    public int getLossTrigger() {
        return this.m_lossTrigger;
    }
    
    public boolean hasBuffTrigger() {
        return this.m_buffTrigger != -1;
    }
    
    public boolean hasDebuffTrigger() {
        return this.m_debuffTrigger != -1;
    }
    
    public boolean hasGainTrigger() {
        return this.m_gainTrigger != -1;
    }
    
    public boolean hasLossTrigger() {
        return this.m_lossTrigger != -1;
    }
    
    public boolean hasRelatedElement() {
        return this.m_relatedElement != null;
    }
    
    public Elements getRelatedElement() {
        return this.m_relatedElement;
    }
    
    public boolean isCurrentCanExceedMax() {
        return this.m_currentCanExceedMax;
    }
    
    @Override
    public int getOutFightMax() {
        return this.m_outFightMax;
    }
    
    public static FighterCharacteristicType getCharacteristicTypeFromId(final Byte id) {
        for (final FighterCharacteristicType characteristicType : values()) {
            if (characteristicType.getId() == id) {
                return characteristicType;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    static int getCharacteristicMaxIndex() {
        if (FighterCharacteristicType.MAX_INDEX == Integer.MIN_VALUE) {
            final FighterCharacteristicType[] values = values();
            int max = 0;
            for (final FighterCharacteristicType charac : values) {
                if (charac.getId() < 0) {
                    throw new AssertionError((Object)"Utiliser une hashMap plut\u00f4t (id <0)");
                }
                if (charac.getId() > max) {
                    max = charac.getId();
                }
            }
            if (max > values.length * 1.5f) {
                throw new AssertionError((Object)"Utiliser une hashMap plut\u00f4t (tableau avec trop de vide)");
            }
            FighterCharacteristicType.MAX_INDEX = max;
        }
        return FighterCharacteristicType.MAX_INDEX;
    }
    
    static {
        FighterCharacteristicType.MAX_INDEX = Integer.MIN_VALUE;
    }
}
