package com.ankamagames.baseImpl.common.clientAndServer.world;

public class PartitionConstants
{
    public static final int PARTITION_WIDTH = 18;
    public static final int PARTITION_HEIGHT = 18;
    private static final double CENTER_VALUE = 0.5;
    
    public static int getPartitionXFromCellX(final int cellX) {
        return MapConstants.getMapCoordFromCellX(cellX);
    }
    
    public static int getPartitionYFromCellY(final int cellY) {
        return MapConstants.getMapCoordFromCellY(cellY);
    }
    
    public static int getCellCenterXFromPartitionX(final int partitionX) {
        return (int)((partitionX + 0.5) * 18.0);
    }
    
    public static int getCellCenterYFromPartitionY(final int partitionY) {
        return (int)((partitionY + 0.5) * 18.0);
    }
}
