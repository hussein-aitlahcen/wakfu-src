package com.ankamagames.wakfu.common.constants;

import com.ankamagames.wakfu.common.configuration.*;

public class XpConstants
{
    public static final short MAX_PLAYER_LEVEL = 200;
    private static short PLAYER_CHARACTER_LEVEL_CAP;
    public static final short MAX_SPELL_LEVEL = 200;
    public static final int MINIMUM_LEVEL_NEEDED_FOR_RESTAT = 3;
    public static final boolean ENABLE_XP_TRANSFER_WHEN_CAP_REACHED = true;
    public static final boolean SPELL_LEVEL_CAPED_BY_CHARACTER_LEVEL = true;
    public static final double MAX_SPELLS_AT_MAX_LEVEL_PER_ELEMENT = 2.0;
    public static final int SPELL_XP_COST_FACTOR_IN_ELEMENT_ABOVE_A_THIRD = 2;
    public static final float CHARACTER_XP_TO_ITEM_XP_FACTOR = 0.1f;
    public static final int MAXIMUM_PERCENT_OF_SPELL_VALUE_WON_FROM_MISSING_XP = 100;
    public static final int MAXIMUM_PERCENT_OF_SPELL_VALUE_WON_FROM_EXCESSIVE_XP = 50;
    public static final float XP_OF_SUM_VS_SUM_OF_XP_WEIGHT = 0.7f;
    public static final float MAX_REROLL_XP_FACTOR = 3.0f;
    
    public static short getPlayerCharacterLevelCap() {
        final short level = (short)SystemConfiguration.INSTANCE.getIntValue(SystemConfigurationType.PLAYER_LEVEL_CAP);
        return (level < 0) ? XpConstants.PLAYER_CHARACTER_LEVEL_CAP : level;
    }
    
    static {
        XpConstants.PLAYER_CHARACTER_LEVEL_CAP = 145;
    }
}
