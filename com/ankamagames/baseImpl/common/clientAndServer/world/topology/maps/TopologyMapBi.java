package com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps;

import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TopologyMapBi extends TopologyMapBlockedCells
{
    private byte[] m_costs;
    private byte[] m_murfins;
    private byte[] m_properties;
    private int[] m_cells;
    
    @Override
    public final int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        assert this.checkGetPathData(x, y, cellPathData);
        final CellPathData data = cellPathData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.m_z;
        data.m_hollow = false;
        data.m_height = 0;
        final int tab = this.getIndex(x, y);
        data.m_cost = this.m_costs[tab];
        data.m_murfinInfo = this.m_murfins[tab];
        data.m_miscProperties = this.m_properties[tab];
        return 1;
    }
    
    private int getIndex(final int x, final int y) {
        final int xIndex = x - this.m_x;
        final int yIndex = y - this.m_y;
        final int cellIndex = yIndex * 18 + xIndex;
        return TopologyIndexerHelper.getIndex(this.m_cells, cellIndex, this.m_costs.length);
    }
    
    @Override
    public int getVisibilityData(final int x, final int y, final CellVisibilityData[] cellVisibilityData, final int index) {
        assert this.checkGetVisibilityData(x, y, cellVisibilityData);
        final CellVisibilityData data = cellVisibilityData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.m_z;
        data.m_hollow = false;
        data.m_height = 0;
        return 1;
    }
    
    @Override
    public void load(final ExtendedDataInputStream stream) throws IOException {
        super.load(stream);
        final int indexSize = stream.readByte();
        this.m_costs = new byte[indexSize];
        this.m_murfins = new byte[indexSize];
        this.m_properties = new byte[indexSize];
        for (int i = 0; i < indexSize; ++i) {
            this.m_costs[i] = stream.readByte();
            this.m_murfins[i] = stream.readByte();
            this.m_properties[i] = stream.readByte();
        }
        final int cellSize = stream.readByte() & 0xFF;
        this.m_cells = TopologyIndexerHelper.createFor(this.m_cells, cellSize, stream);
    }
}
