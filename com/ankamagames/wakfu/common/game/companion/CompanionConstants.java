package com.ankamagames.wakfu.common.game.companion;

public class CompanionConstants
{
    public static final float ITEMIZATION_XP_REDUCTION_FACTOR = 0.7f;
    public static float COMPANION_XP_FACTOR;
    public static float COMPANION_KAMA_FACTOR;
    public static boolean XP_MAX;
    
    static {
        CompanionConstants.COMPANION_XP_FACTOR = 1.5f;
        CompanionConstants.COMPANION_KAMA_FACTOR = 1.0f;
        CompanionConstants.XP_MAX = false;
    }
}
