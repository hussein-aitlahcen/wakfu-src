package com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TopologyMapDi extends TopologyMapBlockedCells
{
    private static final Logger m_logger;
    private static final TIntArrayList _LIST;
    private static final Object m_mutex;
    private static final byte MOV_MASK = 1;
    private static final byte LOS_MASK = 2;
    public static final int INDEX_OFFSET = 1;
    private byte[] m_costs;
    private byte[] m_murfins;
    private byte[] m_properties;
    private byte[] m_movLos;
    private short[] m_zs;
    private byte[] m_heights;
    private long[] m_cells;
    private int[] m_cellsWithMultiZ;
    
    public final boolean cellIsEmpty(final int x, final int y) {
        final int cellIndex = this.getIndex(x, y);
        return cellIndex != 0 && (this.m_movLos[cellIndex - 1] & 0x1) == 0x1;
    }
    
    @Override
    public final int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        assert this.checkGetPathData(x, y, cellPathData);
        final int cellIndex = this.getIndex(x, y);
        if (cellIndex != 0) {
            final CellPathData data = cellPathData[index];
            data.m_x = x;
            data.m_y = y;
            this.fillPathData(data, cellIndex - 1);
            return 1;
        }
        synchronized (TopologyMapDi.m_mutex) {
            final TIntArrayList tab = this.getMultiIndex(x - this.m_x, y - this.m_y, TopologyMapDi._LIST);
            final int zCount = tab.size();
            for (int i = 0; i < zCount; ++i) {
                final CellPathData data2 = cellPathData[index + i];
                data2.m_x = x;
                data2.m_y = y;
                this.fillPathData(data2, tab.getQuick(i));
            }
            return zCount;
        }
    }
    
    private void fillPathData(final CellPathData data, final int cellIndex) {
        data.m_z = this.m_zs[cellIndex];
        data.m_hollow = ((this.m_movLos[cellIndex] & 0x1) == 0x1);
        data.m_height = this.m_heights[cellIndex];
        data.m_cost = this.m_costs[cellIndex];
        data.m_murfinInfo = this.m_murfins[cellIndex];
        data.m_miscProperties = this.m_properties[cellIndex];
    }
    
    @Override
    public int getVisibilityData(final int x, final int y, final CellVisibilityData[] cellVisibilityData, final int index) {
        assert this.checkGetVisibilityData(x, y, cellVisibilityData);
        final int cellIndex = this.getIndex(x, y);
        if (cellIndex != 0) {
            final CellVisibilityData data = cellVisibilityData[index];
            data.m_x = x;
            data.m_y = y;
            this.fillVisibilityData(data, cellIndex - 1);
            return 1;
        }
        synchronized (TopologyMapDi.m_mutex) {
            final TIntArrayList tab = this.getMultiIndex(x - this.m_x, y - this.m_y, TopologyMapDi._LIST);
            final int zCount = tab.size();
            for (int i = 0; i < zCount; ++i) {
                final CellVisibilityData data2 = cellVisibilityData[index + i];
                data2.m_x = x;
                data2.m_y = y;
                this.fillVisibilityData(data2, tab.getQuick(i));
            }
            return zCount;
        }
    }
    
    private void fillVisibilityData(final CellVisibilityData data, final int cellIndex) {
        data.m_z = this.m_zs[cellIndex];
        data.m_hollow = ((this.m_movLos[cellIndex] & 0x2) == 0x2);
        data.m_height = this.m_heights[cellIndex];
    }
    
    @Override
    public void load(final ExtendedDataInputStream stream) throws IOException {
        super.load(stream);
        final int indexSize = stream.readByte() & 0xFF;
        this.m_costs = new byte[indexSize];
        this.m_murfins = new byte[indexSize];
        this.m_properties = new byte[indexSize];
        this.m_zs = new short[indexSize];
        this.m_heights = new byte[indexSize];
        this.m_movLos = new byte[indexSize];
        for (int i = 0; i < indexSize; ++i) {
            this.m_costs[i] = stream.readByte();
            this.m_murfins[i] = stream.readByte();
            this.m_properties[i] = stream.readByte();
            this.m_zs[i] = stream.readShort();
            this.m_heights[i] = stream.readByte();
            this.m_movLos[i] = stream.readByte();
        }
        final int cellCount = stream.readByte() & 0xFF;
        this.m_cells = TopologyIndexerHelper.createFor(this.m_cells, cellCount, stream);
        final int remainsCount = stream.readShort() & 0xFFFF;
        this.m_cellsWithMultiZ = stream.readInts(remainsCount);
    }
    
    private int getIndex(final int x, final int y) {
        final int xIndex = x - this.m_x;
        final int yIndex = y - this.m_y;
        final int cellIndex = yIndex * this.getMapWidth() + xIndex;
        return TopologyIndexerHelper.getIndex(this.m_cells, cellIndex, this.m_costs.length + 1);
    }
    
    private TIntArrayList getMultiIndex(final int x, final int y, final TIntArrayList list) {
        list.reset();
        for (int multiCount = this.m_cellsWithMultiZ.length, i = 0; i < multiCount; ++i) {
            final int cellData = this.m_cellsWithMultiZ[i];
            final int cy = cellData >> 8 & 0xFF;
            if (cy >= y) {
                if (cy > y) {
                    break;
                }
                final int cx = cellData & 0xFF;
                if (cx >= x) {
                    if (cx > x) {
                        break;
                    }
                    final int index = cellData >> 16 & 0xFFFF;
                    list.add(index);
                }
            }
        }
        assert list.size() != 1 : "nombre de z incorrect";
        return list;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TopologyMapDi.class);
        _LIST = new TIntArrayList(32);
        m_mutex = new Object();
    }
}
