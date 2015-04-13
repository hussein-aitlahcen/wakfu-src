package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;

public class TopologyChecker
{
    public static final short NO_VALID_Z_FOUND = Short.MIN_VALUE;
    protected static final Logger m_logger;
    
    public static short getHighestWalkableZ(final int cellFirstDataIndex, final int cellDataCount, @NotNull final CellPathData[] cellData, final short maxZ, final int heightNeeded) {
        if (heightNeeded <= 0) {
            TopologyChecker.m_logger.error((Object)"no moverHeight defined");
        }
        if (cellDataCount <= 0) {
            TopologyChecker.m_logger.error((Object)"can't get highest height : no data for this element");
        }
        if (cellFirstDataIndex + cellDataCount > cellData.length) {
            TopologyChecker.m_logger.error((Object)"cell elements index and count are out of bounds");
        }
        if (cellDataCount != 1) {
            short latestHighestBlockingZ = 32767;
            for (int i = cellFirstDataIndex + cellDataCount - 1; i >= cellFirstDataIndex; --i) {
                final CellPathData data = cellData[i];
                if (!data.m_hollow) {
                    if (data.m_cost == -1) {
                        latestHighestBlockingZ = (short)(data.m_z - data.m_height);
                    }
                    else if (data.m_z > maxZ) {
                        latestHighestBlockingZ = (short)(data.m_z - data.m_height);
                    }
                    else {
                        if (heightNeeded <= latestHighestBlockingZ - data.m_z) {
                            return data.m_z;
                        }
                        latestHighestBlockingZ = (short)(data.m_z - data.m_height);
                    }
                }
            }
            return -32768;
        }
        final CellPathData data2 = cellData[cellFirstDataIndex];
        if (data2.m_hollow) {
            TopologyChecker.m_logger.error((Object)"data invalid : we can move through, but this element is the only one");
            return -32768;
        }
        if (data2.m_cost == -1) {
            return -32768;
        }
        if (data2.m_z > maxZ) {
            return -32768;
        }
        return data2.m_z;
    }
    
    public static boolean checkHeightIndexValidity(final int moverZIndex, final int firstZIndex, final int indexesCount, final CellPathData[] cellData, final int heightNeeded) {
        assert heightNeeded > 0 : "no moverHeight defined";
        assert cellData != null : "cellData can't be null";
        assert firstZIndex >= 0 && indexesCount > 0 && firstZIndex + indexesCount <= cellData.length : "invalid bounds : [" + firstZIndex + ", " + (firstZIndex + indexesCount) + "]";
        assert moverZIndex >= firstZIndex && moverZIndex < firstZIndex + indexesCount : "moverZIndex not within the given bounds";
        if (cellData[moverZIndex].m_cost == -1 || cellData[moverZIndex].m_hollow) {
            return false;
        }
        if (moverZIndex == firstZIndex + indexesCount - 1 && !cellData[moverZIndex].m_hollow) {
            return true;
        }
        final int moverHeadZ = cellData[moverZIndex].m_z + heightNeeded;
        for (int currentIndex = moverZIndex + 1; currentIndex < firstZIndex + indexesCount; ++currentIndex) {
            final CellPathData data = cellData[currentIndex];
            final int elementBottomZ = data.m_z - data.m_height;
            if (elementBottomZ >= moverHeadZ) {
                return true;
            }
            if (!data.m_hollow) {
                return false;
            }
        }
        return true;
    }
    
    public static short getIndexFromZ(final int cellFirstDataIndex, final int cellDataCount, final CellPathData[] cellData, final short zSearched) {
        assert cellDataCount > 0 : "can't get index from z : no data for this element";
        assert cellData != null : "can't get index from z : no data array provided";
        assert cellFirstDataIndex + cellDataCount <= cellData.length : "cell elements index and count are out of bounds";
        for (int index = cellFirstDataIndex + cellDataCount - 1; index >= cellFirstDataIndex; --index) {
            if (cellData[index].m_z == zSearched) {
                return (short)(index - cellFirstDataIndex);
            }
        }
        return -32768;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TopologyChecker.class);
    }
}
