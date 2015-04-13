package com.ankamagames.wakfu.client.core.world.havenWorld;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;

class BorderHelper
{
    private static final IntShortLightWeightMap BORDER_PATCHES;
    private static final int TOP_LEFT = 1;
    private static final int TOP_MIDDLE = 2;
    private static final int TOP_RIGHT = 4;
    private static final int LEFT = 8;
    private static final int RIGHT = 16;
    private static final int BOTTOM_LEFT = 32;
    private static final int BOTTOM_MIDDLE = 64;
    private static final int BOTTOM_RIGHT = 128;
    
    public static short getPatchIdForMask(final int mask) {
        if (!BorderHelper.BORDER_PATCHES.contains(mask)) {
            return PartitionPatch.getPatchIdFromMapCoord(1, 0);
        }
        return BorderHelper.BORDER_PATCHES.get(mask);
    }
    
    public static short selectBorderPatchIdFor(final short[][] patches, final int x, final int y) {
        int mask = 0;
        mask |= (isEmptyPatch(patches, x - 1, y - 1) ? 0 : 1);
        mask |= (isEmptyPatch(patches, x + 0, y - 1) ? 0 : 2);
        mask |= (isEmptyPatch(patches, x + 1, y - 1) ? 0 : 4);
        mask |= (isEmptyPatch(patches, x - 1, y + 0) ? 0 : 8);
        mask |= (isEmptyPatch(patches, x + 1, y + 0) ? 0 : 16);
        mask |= (isEmptyPatch(patches, x - 1, y + 1) ? 0 : 32);
        mask |= (isEmptyPatch(patches, x + 0, y + 1) ? 0 : 64);
        mask |= (isEmptyPatch(patches, x + 1, y + 1) ? 0 : 128);
        return getPatchIdForMask(mask);
    }
    
    public static boolean isEmptyPatch(final short[][] patches, final int x, final int y) {
        return x < 0 || x >= patches.length || (y < 0 || y >= patches[x].length) || patches[x][y] == PartitionPatch.EMPTY;
    }
    
    private static boolean isEmptyPatchOrEntryBorder(final short[][] patches, final int x, final int y) {
        return isEmptyPatch(patches, x, y) || PartitionPatch.getMapCoordFromPatchId(patches[x][y]).getX() == 2;
    }
    
    private static void insert(final int mapY, final int mustBePlain, final int... emptyOrPlain) {
        final short patchId = PartitionPatch.getPatchIdFromMapCoord(1, mapY);
        BorderHelper.BORDER_PATCHES.put(mustBePlain, patchId);
        for (final int optional : emptyOrPlain) {
            insert(mapY, mustBePlain | optional, new int[0]);
            insert(mapY, mustBePlain | optional, allExcept(emptyOrPlain, optional));
        }
    }
    
    private static int[] allExcept(final int[] array, final int remove) {
        final int[] result = new int[array.length - 1];
        int k = 0;
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != remove) {
                result[k++] = array[i];
            }
        }
        return result;
    }
    
    static {
        BORDER_PATCHES = new IntShortLightWeightMap();
        final int[] FOUR_CORNERS = { 1, 32, 4, 128 };
        final int[] THREE_CORNERS = { 32, 4, 128 };
        insert(1, 0, FOUR_CORNERS);
        insert(2, 16, FOUR_CORNERS);
        insert(3, 64, FOUR_CORNERS);
        insert(4, 9, FOUR_CORNERS);
        insert(5, 8, THREE_CORNERS);
        insert(6, 3, FOUR_CORNERS);
        insert(7, 2, THREE_CORNERS);
        insert(8, 19, FOUR_CORNERS);
        insert(9, 18, THREE_CORNERS);
        insert(10, 10, FOUR_CORNERS);
        insert(11, 73, FOUR_CORNERS);
        insert(12, 72, THREE_CORNERS);
        insert(13, 80, FOUR_CORNERS);
        insert(14, 74, FOUR_CORNERS);
        insert(15, 26, FOUR_CORNERS);
        insert(16, 83, FOUR_CORNERS);
        insert(17, 82, THREE_CORNERS);
        insert(18, 89, FOUR_CORNERS);
        insert(19, 88, THREE_CORNERS);
        insert(20, 25, FOUR_CORNERS);
        insert(21, 24, THREE_CORNERS);
        insert(22, 67, FOUR_CORNERS);
        insert(23, 66, THREE_CORNERS);
        insert(24, 90, FOUR_CORNERS);
        insert(25, 17, THREE_CORNERS);
        insert(26, 65, THREE_CORNERS);
        insert(27, 1, THREE_CORNERS);
    }
}
