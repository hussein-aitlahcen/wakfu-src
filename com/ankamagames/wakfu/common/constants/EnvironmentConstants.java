package com.ankamagames.wakfu.common.constants;

import com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData.*;

public abstract class EnvironmentConstants
{
    public static final short GROUND_TYPE_STERYL = 0;
    public static final int DISTRIB_SIZE = 3;
    private static final CellDataCompressor m_partition;
    private static final CellDataCompressor m_partitionPatch;
    
    public static CellDataCompressor forPartition() {
        return EnvironmentConstants.m_partition;
    }
    
    public static CellDataCompressor forPartitionPatch() {
        return EnvironmentConstants.m_partitionPatch;
    }
    
    public static int computeDistributionIndex(int cellX, int cellY) {
        cellX /= 3;
        cellY /= 3;
        return cellX + 6 * cellY;
    }
    
    static {
        m_partition = new CellDataCompressor(18, 18, (short)0);
        m_partitionPatch = new CellDataCompressor(9, 9, (short)0);
    }
}
