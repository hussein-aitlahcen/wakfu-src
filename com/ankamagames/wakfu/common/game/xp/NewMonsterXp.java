package com.ankamagames.wakfu.common.game.xp;

public final class NewMonsterXp
{
    private static final double OLD_PART_FIRST_FACTOR = 100.0;
    private static final double OLD_PART_SECOND_FACTOR = 1.5;
    private static final double OLD_PART_POWER = 2.2;
    private static final double ADELIN_FACTOR_ONE = 630.0;
    private static final double ADELIN_FACTOR_TWO = 20.0;
    private static final double ADELIN_FACTOR_THREE = 4.0;
    private static final double NEW_FIRST_FACTOR = 2.0;
    private static final double NEW_SECOND_FACTOR = 21.0;
    private static final double NEW_FINAL_FACTOR = 1.0;
    private static final double NEW_IMPORTANCE_FACTOR = 8860.0;
    
    public static long getMonsterBaseXp(final short monsterLevel) {
        final double oldXp = 100.0 * monsterLevel + 1.5 * Math.pow(monsterLevel, 2.2);
        if (monsterLevel < 57) {
            return Math.round(Math.round(oldXp + 630.0 * Math.pow(monsterLevel / 20.0, 4.0)));
        }
        return Math.round(8860.0 * Math.pow(2.0, monsterLevel / 21.0));
    }
}
