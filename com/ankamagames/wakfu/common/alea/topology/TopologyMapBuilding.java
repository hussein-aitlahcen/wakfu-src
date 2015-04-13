package com.ankamagames.wakfu.common.alea.topology;

import com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TopologyMapBuilding extends TopologyMapDi
{
    private static final Logger m_logger;
    private static final CellPathData[] CHECK_GROUP_CELLS;
    private int m_width;
    private int m_height;
    
    @Override
    public void load(final ExtendedDataInputStream stream) throws IOException {
        super.load(stream);
        this.m_width = (stream.readByte() & 0xFF);
        this.m_height = (stream.readByte() & 0xFF);
    }
    
    public int getMapWidth() {
        return this.m_width;
    }
    
    public int getMapHeight() {
        return this.m_height;
    }
    
    public boolean altitudeEquals0(final int cellX, final int cellY) {
        final int num = this.getPathData(cellX, cellY, TopologyMapBuilding.CHECK_GROUP_CELLS, 0);
        return num == 0 || (num == 1 && TopologyMapBuilding.CHECK_GROUP_CELLS[0].m_z == 0);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TopologyMapBuilding.class);
        CHECK_GROUP_CELLS = CellPathData.createCellPathDataTab();
    }
}
