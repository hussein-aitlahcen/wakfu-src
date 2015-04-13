package com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TopologyMapC extends TopologyMapBlockedCells
{
    private static final Logger m_logger;
    public static final byte MOV_MASK = 1;
    public static final byte LOS_MASK = 2;
    private final byte[] m_costs;
    private final byte[] m_murfins;
    private final byte[] m_properties;
    private final short[] m_zs;
    private final byte[] m_heights;
    private final byte[] m_movLos;
    
    public TopologyMapC() {
        super();
        this.m_costs = new byte[324];
        this.m_murfins = new byte[324];
        this.m_properties = new byte[324];
        this.m_zs = new short[324];
        this.m_heights = new byte[324];
        this.m_movLos = new byte[324];
    }
    
    @Override
    public final int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        assert this.checkGetPathData(x, y, cellPathData);
        final CellPathData data = cellPathData[index];
        data.m_x = x;
        data.m_y = y;
        final int cellIndex = this.getIndex(x, y);
        data.m_z = this.m_zs[cellIndex];
        data.m_hollow = ((this.m_movLos[cellIndex] & 0x1) == 0x1);
        data.m_height = this.m_heights[cellIndex];
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
        final int cellIndex = this.getIndex(x, y);
        data.m_z = this.m_zs[cellIndex];
        data.m_hollow = ((this.m_movLos[cellIndex] & 0x2) == 0x2);
        data.m_height = this.m_heights[cellIndex];
        return 1;
    }
    
    @Override
    public void load(final ExtendedDataInputStream stream) throws IOException {
        super.load(stream);
        for (int i = 0; i < 324; ++i) {
            this.m_costs[i] = stream.readByte();
            this.m_murfins[i] = stream.readByte();
            this.m_properties[i] = stream.readByte();
            this.m_zs[i] = stream.readShort();
            this.m_heights[i] = stream.readByte();
            this.m_movLos[i] = stream.readByte();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TopologyMapC.class);
    }
}
