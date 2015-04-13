package com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TopologyMapB extends TopologyMapBlockedCells
{
    private static final Logger m_logger;
    private final byte[] m_costs;
    private final byte[] m_murfins;
    private final byte[] m_properties;
    
    public TopologyMapB() {
        super();
        this.m_costs = new byte[324];
        this.m_murfins = new byte[324];
        this.m_properties = new byte[324];
    }
    
    @Override
    public final int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        assert this.checkGetPathData(x, y, cellPathData);
        final CellPathData data = cellPathData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.m_z;
        data.m_hollow = false;
        data.m_height = 0;
        final int cellIndex = this.getIndex(x, y);
        data.m_cost = this.m_costs[cellIndex];
        data.m_murfinInfo = this.m_murfins[cellIndex];
        data.m_miscProperties = this.m_properties[cellIndex];
        return 1;
    }
    
    private int getIndex(final int x, final int y) {
        final int xIndex = x - this.m_x;
        final int yIndex = y - this.m_y;
        return yIndex * 18 + xIndex;
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
        for (int i = 0; i < 324; ++i) {
            this.m_costs[i] = stream.readByte();
            this.m_murfins[i] = stream.readByte();
            this.m_properties[i] = stream.readByte();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TopologyMapB.class);
    }
}
