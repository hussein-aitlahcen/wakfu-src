package com.ankamagames.wakfu.common.constants;

public final class ItemDropParameters
{
    public static final byte PHASE_COUNT = 3;
    public static final short[] ITEM_PHASES_SEC;
    public static final short[] ITEM_PHASES_TURN;
    public static final short ITEM_SPAN_SEC = 30;
    public static final short ITEM_SPAN_TURN = 3;
    public static final int CLOCK_ID = 1;
    public static final short MAX_PICKUP_DISTANCE = 1;
    public static final int PROBABILITY_UNIT = 10000;
    public static final byte MIN_VARIATION = 50;
    public static final byte MAX_VARAIATION = 120;
    public static final int NEED_GREED_LOOT_MAX_ROLL_TIME = 300000;
    
    static {
        ITEM_PHASES_SEC = new short[] { 30, 30, 30 };
        ITEM_PHASES_TURN = new short[] { 3, 3, 3 };
    }
}
