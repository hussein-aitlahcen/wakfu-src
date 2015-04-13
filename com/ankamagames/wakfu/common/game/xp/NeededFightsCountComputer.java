package com.ankamagames.wakfu.common.game.xp;

final class NeededFightsCountComputer
{
    private static final int LEVEL_CAP_1 = 98;
    private static final double POW_1 = 0.8;
    private static final int LEVEL_CAP_2 = 185;
    private static final int REDUCE_2 = 81;
    private static final double POW_2 = 1.3;
    private static final int REDUCE_3 = 164;
    private static final int POW_3 = 2;
    
    public static double getNeededFightsCount(final int level) {
        if (level < 98) {
            return Math.pow(level, 0.8);
        }
        if (level < 185) {
            return Math.pow(level - 81, 1.3);
        }
        return Math.pow(level - 164, 2.0);
    }
}
