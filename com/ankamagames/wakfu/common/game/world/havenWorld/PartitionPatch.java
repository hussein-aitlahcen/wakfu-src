package com.ankamagames.wakfu.common.game.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.groundType.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.alea.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData.*;

public abstract class PartitionPatch
{
    private static final Logger m_logger;
    public static final CellDataCompressor HEIGHT_CELLS_DATA;
    public static final CellDataCompressor PROPERTIES_CELLS_DATA;
    private static final CellWithResource[] NO_CELLS_WITH_RESOURCE;
    public static final short EMPTY;
    public static final short DEFAULT = 0;
    public static final int PATCH_BY_PARTITION_W = 2;
    public static final int PATCH_BY_PARTITION_H = 2;
    public static final int PATCH_WIDTH = 9;
    public static final int PATCH_HEIGHT = 9;
    public static final int NUM_CELLS = 81;
    public final int m_id;
    private byte[] m_blockedCells;
    private CompressedCellData m_groundData;
    private CompressedCellData m_altitudes;
    private CompressedCellData m_miscProperties;
    private CompressedCellData m_murFinInfo;
    private CellWithResource[] m_cellsWithResource;
    
    protected PartitionPatch(final int id) {
        super();
        this.m_id = id;
    }
    
    public boolean isBlocked(final int x, final int y) {
        assert x >= 0 && x < 9;
        assert y >= 0 && y < 9;
        return this.m_blockedCells != null && ByteArrayBitSet.get(this.m_blockedCells, y * 9 + x);
    }
    
    public boolean hasResource(final int x, final int y) {
        assert x >= 0 && x < 9;
        assert y >= 0 && y < 9;
        this.computeCellsWithResource();
        if (this.m_cellsWithResource == PartitionPatch.NO_CELLS_WITH_RESOURCE) {
            return false;
        }
        for (int i = 0; i < this.m_cellsWithResource.length; ++i) {
            if (this.m_cellsWithResource[i].m_x == x && this.m_cellsWithResource[i].m_y == y) {
                return true;
            }
        }
        return false;
    }
    
    private void computeCellsWithResource() {
        if (this.m_cellsWithResource != null) {
            return;
        }
        final ArrayList<CellWithResource> cells = new ArrayList<CellWithResource>();
        for (int dx = 0; dx < 9; ++dx) {
            for (int dy = 0; dy < 9; ++dy) {
                final short groundTypeId = EnvironmentConstants.forPartitionPatch().getValue(dx, dy, 0, this.m_groundData);
                final GroundType groundType = GroundManager.getInstance().getGroundType(groundTypeId);
                if (groundType != null) {
                    if (!HavenWorldConstants.isGroundBase(groundTypeId)) {
                        final int[] allowedResources = groundType.getAllowedResources();
                        if (allowedResources.length != 0) {
                            final short z = getZ(this, dx, dy);
                            cells.add(new CellWithResource((byte)dx, (byte)dy, z, allowedResources[0]));
                        }
                    }
                }
            }
        }
        if (cells.isEmpty()) {
            this.m_cellsWithResource = PartitionPatch.NO_CELLS_WITH_RESOURCE;
        }
        else {
            cells.toArray(this.m_cellsWithResource = new CellWithResource[cells.size()]);
        }
    }
    
    public void foreachCellWithResource(final TObjectProcedure<CellWithResource> procedure) {
        this.computeCellsWithResource();
        if (this.m_cellsWithResource == PartitionPatch.NO_CELLS_WITH_RESOURCE) {
            return;
        }
        for (int i = 0; i < this.m_cellsWithResource.length; ++i) {
            if (!procedure.execute(this.m_cellsWithResource[i])) {
                return;
            }
        }
    }
    
    public short getMiscProperty(final int cellX, final int cellY) {
        return PartitionPatch.PROPERTIES_CELLS_DATA.getValue(cellX, cellY, 0, this.m_miscProperties);
    }
    
    public short getMurFinInfo(final int cellX, final int cellY) {
        return PartitionPatch.PROPERTIES_CELLS_DATA.getValue(cellX, cellY, 0, this.m_murFinInfo);
    }
    
    public short getAltitude(final int cellX, final int cellY) {
        return PartitionPatch.HEIGHT_CELLS_DATA.getValue(cellX, cellY, 0, this.m_altitudes);
    }
    
    public short getGroundType(final int cellX, final int cellY, final int cellZ) {
        return EnvironmentConstants.forPartitionPatch().getValue(cellX, cellY, cellZ, this.m_groundData);
    }
    
    public void read(final ExtendedDataInputStream stream) throws IOException {
        this.readTopology(stream);
        this.readGroundTypes(stream);
    }
    
    protected final void readTopology(final ExtendedDataInputStream stream) throws IOException {
        final byte dataLength = stream.readByte();
        if (dataLength <= 0) {
            this.m_blockedCells = null;
        }
        else {
            assert dataLength == ByteArrayBitSet.getDataLength(81);
            this.m_blockedCells = stream.readBytes(dataLength);
        }
        this.m_altitudes = CompressedCellData.fromStream(stream, PartitionPatch.HEIGHT_CELLS_DATA.getDefaultValue());
        this.m_murFinInfo = CompressedCellData.fromStream(stream, PartitionPatch.PROPERTIES_CELLS_DATA.getDefaultValue());
        this.m_miscProperties = CompressedCellData.fromStream(stream, PartitionPatch.PROPERTIES_CELLS_DATA.getDefaultValue());
    }
    
    protected void readGroundTypes(final ExtendedDataInputStream stream) throws IOException {
        this.m_groundData = CompressedCellData.fromStream(stream, EnvironmentConstants.forPartitionPatch().getDefaultValue());
    }
    
    public static short getPatchIdFromMapCoord(final int mapCoordX, final int mapCoordY) {
        if (mapCoordX < 0 || mapCoordX > 15) {
            throw new UnsupportedOperationException("coordon\u00e9e x de patch hors contraintes mapCoordX=" + mapCoordX);
        }
        if (mapCoordY < 0 || mapCoordY > 4095) {
            throw new UnsupportedOperationException("coordon\u00e9e y de patch hors contraintes mapCoordY=" + mapCoordY);
        }
        final short la = (short)(mapCoordX & 0xF);
        final short lb = (short)(mapCoordY & 0xFFF);
        return (short)(la << 12 | lb);
    }
    
    public static Point2i getMapCoordFromPatchId(final int patchId) {
        final int x = patchId >> 12 & 0xF;
        final int y = patchId & 0xFFF;
        return new Point2i(x, y);
    }
    
    public static CompressedCellData getMergedAltitudes(final PartitionPatch topLeft, final PartitionPatch topRight, final PartitionPatch bottomLeft, final PartitionPatch bottomRight) {
        final short[] altitude = new short[324];
        int i = 0;
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getZ(topLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getZ(topRight, x, y);
            }
        }
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getZ(bottomLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getZ(bottomRight, x, y);
            }
        }
        try {
            return TopologyMapPatch.HEIGHT_CELLS_DATA.optimize(altitude);
        }
        catch (CompressedDataException e) {
            PartitionPatch.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    public static CompressedCellData getMergedMurFinInfo(final PartitionPatch topLeft, final PartitionPatch topRight, final PartitionPatch bottomLeft, final PartitionPatch bottomRight) {
        final short[] altitude = new short[324];
        int i = 0;
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMurFinInfo(topLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMurFinInfo(topRight, x, y);
            }
        }
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMurFinInfo(bottomLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMurFinInfo(bottomRight, x, y);
            }
        }
        try {
            return TopologyMapPatch.PROPERTIES_CELLS_DATA.optimize(altitude);
        }
        catch (CompressedDataException e) {
            PartitionPatch.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    public static CompressedCellData getMergedMiscProperties(final PartitionPatch topLeft, final PartitionPatch topRight, final PartitionPatch bottomLeft, final PartitionPatch bottomRight) {
        final short[] altitude = new short[324];
        int i = 0;
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMiscProperty(topLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMiscProperty(topRight, x, y);
            }
        }
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMiscProperty(bottomLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                altitude[i++] = getMiscProperty(bottomRight, x, y);
            }
        }
        try {
            return TopologyMapPatch.PROPERTIES_CELLS_DATA.optimize(altitude);
        }
        catch (CompressedDataException e) {
            PartitionPatch.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    private static short getZ(final PartitionPatch patch, final int x, final int y) {
        return (patch != null) ? patch.getAltitude(x, y) : PartitionPatch.HEIGHT_CELLS_DATA.getDefaultValue();
    }
    
    private static short getMurFinInfo(final PartitionPatch patch, final int x, final int y) {
        return (patch != null) ? patch.getMurFinInfo(x, y) : PartitionPatch.PROPERTIES_CELLS_DATA.getDefaultValue();
    }
    
    private static short getMiscProperty(final PartitionPatch patch, final int x, final int y) {
        return (patch != null) ? patch.getMiscProperty(x, y) : PartitionPatch.PROPERTIES_CELLS_DATA.getDefaultValue();
    }
    
    private static short getGroundType(final PartitionPatch patch, final int x, final int y) {
        return (patch != null) ? patch.getGroundType(x, y, 0) : EnvironmentConstants.forPartition().getDefaultValue();
    }
    
    public static CompressedCellData getMergedGroundType(final PartitionPatch topLeft, final PartitionPatch topRight, final PartitionPatch bottomLeft, final PartitionPatch bottomRight) {
        final short[] cellsGroundType = new short[324];
        int i = 0;
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                cellsGroundType[i++] = getGroundType(topLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                cellsGroundType[i++] = getGroundType(topRight, x, y);
            }
        }
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                cellsGroundType[i++] = getGroundType(bottomLeft, x, y);
            }
            for (int x = 0; x < 9; ++x) {
                cellsGroundType[i++] = getGroundType(bottomRight, x, y);
            }
        }
        try {
            return EnvironmentConstants.forPartition().optimize(cellsGroundType);
        }
        catch (CompressedDataException e) {
            PartitionPatch.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    public static int getPatchCoordFromCellX(final int cellX) {
        int x = cellX / 9;
        if (cellX < 0 && cellX % 9 != 0) {
            --x;
        }
        assert x == Math.floor(cellX / 9.0);
        return x;
    }
    
    public static int getPatchCoordFromCellY(final int cellY) {
        int y = cellY / 9;
        if (cellY < 0 && cellY % 9 != 0) {
            --y;
        }
        assert y == Math.floor(cellY / 9.0);
        return y;
    }
    
    public static boolean isEditable(final short patchId) {
        final int x = getMapCoordFromPatchId(patchId).getX();
        return x != 1 && x != 2;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartitionPatch.class);
        HEIGHT_CELLS_DATA = new CellDataCompressor(9, 9, (short)(-32768));
        PROPERTIES_CELLS_DATA = new CellDataCompressor(9, 9, (short)0);
        NO_CELLS_WITH_RESOURCE = new CellWithResource[0];
        EMPTY = getPatchIdFromMapCoord(1, 0);
    }
    
    public static class CellWithResource
    {
        public final byte m_x;
        public final byte m_y;
        public final short m_z;
        public final int m_referenceResourceId;
        
        CellWithResource(final byte x, final byte y, final short z, final int referenceResourceId) {
            super();
            this.m_x = x;
            this.m_y = y;
            this.m_z = z;
            this.m_referenceResourceId = referenceResourceId;
        }
    }
}
