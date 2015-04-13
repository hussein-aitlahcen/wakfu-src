package com.ankamagames.wakfu.common.alea.topology;

import com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class FightMapTopology extends TopologyMapBlockedCells
{
    private static final Logger m_logger;
    private final TShortShortHashMap m_cellsAltitude;
    
    public FightMapTopology(final int mapX, final int mapY) {
        super();
        this.m_cellsAltitude = new TShortShortHashMap();
        this.m_x = mapX * 18;
        this.m_y = mapY * 18;
    }
    
    public void setCellAltitude(final int x, final int y, final short altitude) {
        assert this.isInMap(x, y);
        final short cellIndex = this.getCellIndex(x, y);
        this.m_cellsAltitude.put(cellIndex, altitude);
    }
    
    @Override
    public int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        assert this.checkGetPathData(x, y, cellPathData);
        final CellPathData data = cellPathData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.getCellAltitude(x, y);
        final boolean cellBlocked = this.isCellBlocked(x, y);
        data.m_cost = (byte)(cellBlocked ? -1 : 7);
        data.m_hollow = false;
        data.m_height = (byte)(cellBlocked ? 127 : 0);
        data.m_murfinInfo = 0;
        data.m_miscProperties = 0;
        return 1;
    }
    
    @Override
    public int getVisibilityData(final int x, final int y, final CellVisibilityData[] cellVisibilityData, final int index) {
        assert this.checkGetVisibilityData(x, y, cellVisibilityData);
        final CellVisibilityData data = cellVisibilityData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.getCellAltitude(x, y);
        data.m_hollow = false;
        data.m_height = (byte)(this.isCellBlocked(x, y) ? 127 : 0);
        return 1;
    }
    
    public void encode(final ByteArray buffer) {
        final int numInvalidCells = this.m_cellsAltitude.size();
        buffer.putShort((short)numInvalidCells);
        this.m_cellsAltitude.forEachEntry(new TShortShortProcedure() {
            @Override
            public boolean execute(final short cellIndex, final short altitude) {
                buffer.putShort(cellIndex);
                buffer.putShort(altitude);
                return true;
            }
        });
    }
    
    public void decode(final ByteBuffer buffer) {
        assert this.m_cellsAltitude.isEmpty();
        for (int numInvalidCells = buffer.getShort(), i = 0; i < numInvalidCells; ++i) {
            final short index = buffer.getShort();
            final short altitude = buffer.getShort();
            this.m_cellsAltitude.put(index, altitude);
        }
    }
    
    @Override
    public final boolean isCellBlocked(final int x, final int y) {
        return !this.m_cellsAltitude.contains(this.getCellIndex(x, y)) || super.isCellBlocked(x, y);
    }
    
    public final short getCellAltitude(final int x, final int y) {
        final short cellIndex = this.getCellIndex(x, y);
        final short altitude = this.m_cellsAltitude.get(cellIndex);
        if (altitude != 0) {
            return altitude;
        }
        return (short)(this.m_cellsAltitude.contains(cellIndex) ? altitude : -32768);
    }
    
    private short getCellIndex(final int x, final int y) {
        final byte dx = (byte)(x - this.m_x);
        final byte dy = (byte)(y - this.m_y);
        return MathHelper.getShortFromTwoBytes(dx, dy);
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightMapTopology.class);
    }
}
