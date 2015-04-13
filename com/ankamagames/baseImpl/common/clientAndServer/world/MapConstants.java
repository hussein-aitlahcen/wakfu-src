package com.ankamagames.baseImpl.common.clientAndServer.world;

import com.ankamagames.framework.kernel.core.maths.*;

public class MapConstants
{
    private static final int NUM_BITS_CELL_XY = 18;
    private static final int NUM_BITS_CELL_Z = 10;
    public static final int MAP_WIDTH = 18;
    public static final int MAP_LENGTH = 18;
    public static final int NUM_CELLS = 324;
    public static final int MAX_ELEMENTS_PER_CELL = 64;
    public static final int CELL_X_MAX = 131071;
    public static final int CELL_Y_MAX = 131071;
    public static final int CELL_Z_MAX = 511;
    public static final int CELL_X_MIN = -131072;
    public static final int CELL_Y_MIN = -131072;
    public static final int CELL_Z_MIN = -512;
    public static final int MAP_X_MAX = 7281;
    public static final int MAP_Y_MAX = 7281;
    public static final int MAP_Z_MAX = 511;
    public static final int MAP_X_MIN = -7281;
    public static final int MAP_Y_MIN = -7281;
    public static final int MAP_Z_MIN = -512;
    
    public static int getMapCoordFromCellX(final int cellX) {
        return MathHelper.fastFloor(cellX / 18.0f);
    }
    
    public static int getMapCoordFromCellY(final int cellY) {
        return MathHelper.fastFloor(cellY / 18.0f);
    }
    
    public static Rect getPartitionsFromCells(final int minX, final int minY, final int width, final int height) {
        final int mapMinX = getMapCoordFromCellX(minX);
        final int mapMinY = getMapCoordFromCellY(minY);
        final int mapMaxX = getMapCoordFromCellX(minX + width) + 1;
        final int mapMaxY = getMapCoordFromCellY(minY + height) + 1;
        return new Rect(mapMinX, mapMaxX, mapMinY, mapMaxY);
    }
}
