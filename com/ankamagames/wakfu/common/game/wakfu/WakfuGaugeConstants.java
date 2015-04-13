package com.ankamagames.wakfu.common.game.wakfu;

public class WakfuGaugeConstants
{
    public static final int WAKFU_HALF_LIFE = 500;
    public static final int WAKFU_HARD_CAP = 10000;
    public static final float OPPOSED_WAKFU_DECAY = 3.0f;
    static final double WAKFU_EXP_CONSTANT;
    static final float OPPOSED_WAKFU_CONSTANT = 4.0f;
    
    static {
        WAKFU_EXP_CONSTANT = 500.0 / StrictMath.log(2.0);
    }
}
