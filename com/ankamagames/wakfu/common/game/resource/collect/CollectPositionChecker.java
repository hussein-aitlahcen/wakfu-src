package com.ankamagames.wakfu.common.game.resource.collect;

public class CollectPositionChecker
{
    private static final int FISHING = 75;
    private static final int MAX_FISHING_DISTANCE = 3;
    private static final int MAX_DEFAULT_DISTANCE = 1;
    
    public static boolean isAltitudeValid(final short jumpCapacity, final short playerAltitude, final short collectibleAltitude, final int actionId) {
        return actionId == 75 || Math.abs(playerAltitude - collectibleAltitude) <= jumpCapacity;
    }
    
    public static boolean isDistanceValid(final int distance, final int craftId) {
        if (craftId == 75) {
            return distance <= 3;
        }
        return distance == 1;
    }
    
    public static int getMaxCollectDistance(final int craftId) {
        if (craftId == 75) {
            return 3;
        }
        return 1;
    }
}
