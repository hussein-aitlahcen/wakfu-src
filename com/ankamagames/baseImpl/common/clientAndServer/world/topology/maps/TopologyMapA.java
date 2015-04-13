package com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps;

import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TopologyMapA extends TopologyMap
{
    private byte m_cost;
    private byte m_murfin;
    private byte m_property;
    
    @Override
    public final boolean isCellBlocked(final int x, final int y) {
        return this.m_cost == -1;
    }
    
    @Override
    public final int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        assert this.checkGetPathData(x, y, cellPathData);
        final CellPathData data = cellPathData[index];
        data.m_x = x;
        data.m_y = y;
        data.m_z = this.m_z;
        data.m_cost = this.m_cost;
        data.m_hollow = false;
        data.m_height = 0;
        data.m_murfinInfo = this.m_murfin;
        data.m_miscProperties = this.m_property;
        return 1;
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
        this.m_cost = stream.readByte();
        this.m_murfin = stream.readByte();
        this.m_property = stream.readByte();
    }
}
