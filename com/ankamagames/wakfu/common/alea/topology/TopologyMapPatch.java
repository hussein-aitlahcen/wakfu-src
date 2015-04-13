package com.ankamagames.wakfu.common.alea.topology;

import com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;

public class TopologyMapPatch extends TopologyMapBlockedCells implements ConvertFromPatch
{
    private static final Logger m_logger;
    public static final CellDataCompressor HEIGHT_CELLS_DATA;
    public static final CellDataCompressor PROPERTIES_CELLS_DATA;
    @NotNull
    private AbstractBuildingStruct[] m_attachedBuilding;
    private CompressedCellData m_altitudes;
    private CompressedCellData m_murFinInfo;
    private CompressedCellData m_miscProperties;
    
    public TopologyMapPatch(final int mapX, final int mapY) {
        super();
        this.m_x = mapX * 18;
        this.m_y = mapY * 18;
        this.m_z = 0;
    }
    
    @Override
    public void fromPatch(final PartitionPatch patchTopLeft, final PartitionPatch patchTopRight, final PartitionPatch patchBottomLeft, final PartitionPatch patchBottomRight) {
        final int cellOffsetX = 9;
        final int cellOffsetY = 9;
        this.setCellBlocked(patchTopLeft, 0, 0);
        this.setCellBlocked(patchTopRight, 9, 0);
        this.setCellBlocked(patchBottomLeft, 0, 9);
        this.setCellBlocked(patchBottomRight, 9, 9);
        this.m_altitudes = PartitionPatch.getMergedAltitudes(patchTopLeft, patchTopRight, patchBottomLeft, patchBottomRight);
        this.m_murFinInfo = PartitionPatch.getMergedMurFinInfo(patchTopLeft, patchTopRight, patchBottomLeft, patchBottomRight);
        this.m_miscProperties = PartitionPatch.getMergedMiscProperties(patchTopLeft, patchTopRight, patchBottomLeft, patchBottomRight);
    }
    
    @Override
    public void setAttachedBuilding(@NotNull final AbstractBuildingStruct[] attachedBuildings) {
        this.m_attachedBuilding = attachedBuildings;
    }
    
    private void setCellBlocked(final PartitionPatch patch, final int cellOffsetX, final int cellOffsetY) {
        final int dx = cellOffsetX + this.m_x;
        final int dy = cellOffsetY + this.m_y;
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                if (patch == null || patch.isBlocked(x, y)) {
                    this.setCellBlocked(x + dx, y + dy, true);
                }
            }
        }
    }
    
    @Override
    public final boolean isCellBlocked(final int x, final int y) {
        if (!super.isCellBlocked(x, y)) {
            return false;
        }
        if (this.m_attachedBuilding.length != 0) {
            for (final AbstractBuildingStruct building : this.m_attachedBuilding) {
                if (building.contains(x, y) && building.isCellBlocked(x, y)) {
                    return true;
                }
            }
        }
        return true;
    }
    
    @Override
    public void load(final ExtendedDataInputStream stream) throws IOException {
        throw new UnsupportedOperationException("Impossible sur les patch");
    }
    
    @Override
    public final int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        assert this.checkGetPathData(x, y, cellPathData);
        int numZ = 1;
        final CellPathData data = cellPathData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.getZ(x, y);
        data.m_height = (byte)Math.max(0, data.m_z);
        data.m_cost = (byte)(super.isCellBlocked(x, y) ? -1 : 7);
        data.m_hollow = false;
        data.m_murfinInfo = (byte)this.getMurFinInfo(x, y);
        data.m_miscProperties = (byte)this.getMiscProperties(x, y);
        if (this.m_attachedBuilding.length != 0) {
            for (final AbstractBuildingStruct building : this.m_attachedBuilding) {
                final int result = building.getPathData(x, y, cellPathData, index + numZ);
                if (result != -1) {
                    numZ += mergeSameAltitude(cellPathData, index, index + numZ, result);
                }
            }
        }
        return numZ;
    }
    
    @Override
    public int getVisibilityData(final int x, final int y, final CellVisibilityData[] cellVisibilityData, final int index) {
        assert this.checkGetVisibilityData(x, y, cellVisibilityData);
        int numZ = 1;
        final CellVisibilityData data = cellVisibilityData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.getZ(x, y);
        data.m_height = (byte)Math.max(0, data.m_z);
        data.m_hollow = false;
        if (this.m_attachedBuilding.length != 0) {
            for (final AbstractBuildingStruct building : this.m_attachedBuilding) {
                final int result = building.getVisibilityData(x, y, cellVisibilityData, index + numZ);
                if (result != -1) {
                    numZ += mergeSameAltitude(cellVisibilityData, index, index + numZ, result);
                }
            }
        }
        return numZ;
    }
    
    private short getZ(final int x, final int y) {
        if (this.m_altitudes == null) {
            return TopologyMapPatch.HEIGHT_CELLS_DATA.getDefaultValue();
        }
        return TopologyMapPatch.HEIGHT_CELLS_DATA.getValue(x - this.m_x, y - this.m_y, 0, this.m_altitudes);
    }
    
    private short getMiscProperties(final int x, final int y) {
        if (this.m_miscProperties == null) {
            return TopologyMapPatch.PROPERTIES_CELLS_DATA.getDefaultValue();
        }
        return TopologyMapPatch.PROPERTIES_CELLS_DATA.getValue(x - this.m_x, y - this.m_y, 0, this.m_miscProperties);
    }
    
    private short getMurFinInfo(final int x, final int y) {
        if (this.m_murFinInfo == null) {
            return TopologyMapPatch.PROPERTIES_CELLS_DATA.getDefaultValue();
        }
        return TopologyMapPatch.PROPERTIES_CELLS_DATA.getValue(x - this.m_x, y - this.m_y, 0, this.m_murFinInfo);
    }
    
    private static int mergeSameAltitude(final CellData[] cellData, final int start, final int first, int count) {
        for (int i = start; i < first; ++i) {
            int j = first;
            while (j < first + count) {
                if (!CellData.collide(cellData[i], cellData[j])) {
                    ++j;
                }
                else {
                    final CellData old = cellData[i];
                    cellData[i] = cellData[i].createMerged(cellData[j]);
                    if (--count == 0) {
                        continue;
                    }
                    System.arraycopy(cellData, j + 1, cellData, j, count);
                    cellData[j + count] = old;
                }
            }
        }
        return count;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TopologyMapPatch.class);
        HEIGHT_CELLS_DATA = new CellDataCompressor(18, 18, (short)(-32768));
        PROPERTIES_CELLS_DATA = new CellDataCompressor(18, 18, (short)0);
    }
}
