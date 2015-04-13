package com.ankamagames.wakfu.client.core.game.miniMap;

public abstract class MiniMapConstants
{
    public static final int[] WORLD_POSITION_MARKER_APS;
    public static final float DEFAULT_POI_X_HOT_POINT = 0.5f;
    public static final float DEFAULT_POI_Y_HOT_POINT = 0.66f;
    public static final float CHARACTER_POI_Y_HOT_POINT = 1.0f;
    public static final int CHARACTER_INFO_TYPE = 0;
    public static final int DIMENSIONAL_BAG_TYPE = 1;
    public static final int OTHERS_TYPE = 2;
    public static final int CHALLENGE_TYPE = 3;
    public static final int ITEM_TYPE = 4;
    public static final int COMPASS_TYPE = 5;
    public static final int PROTECTOR_TYPE = 6;
    public static final int QUEST_TYPE = 7;
    
    static {
        WORLD_POSITION_MARKER_APS = new int[] { 800015, 800016, 800016, 800016, 800016, 800016, 800016, 800016 };
    }
}
