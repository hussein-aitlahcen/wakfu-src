package com.ankamagames.wakfu.common.game.world.havenWorld.building;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.alea.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AbstractEditorGroupMap
{
    private static final Logger m_logger;
    public static final int NOT_FOUND = -1;
    private final int m_groupId;
    private final TopologyMapBuilding m_topology;
    
    public AbstractEditorGroupMap(final int groupId) {
        super();
        this.m_topology = new TopologyMapBuilding();
        this.m_groupId = groupId;
    }
    
    public boolean isInBounds(final int cellX, final int cellY) {
        return this.m_topology.isInMap(cellX, cellY);
    }
    
    public boolean isInMap(final int cellX, final int cellY) {
        return this.m_topology.isInMap(cellX, cellY) && !this.m_topology.cellIsEmpty(cellX, cellY);
    }
    
    public final int getPathData(final int cellX, final int cellY, final CellPathData[] cellPathData, final int index) {
        if (!this.isInMap(cellX, cellY)) {
            return -1;
        }
        return this.m_topology.getPathData(cellX, cellY, cellPathData, index);
    }
    
    public int getVisibilityData(final int cellX, final int cellY, final CellVisibilityData[] cellVisibilityData, final int index) {
        if (!this.isInMap(cellX, cellY)) {
            return -1;
        }
        return this.m_topology.getVisibilityData(cellX, cellY, cellVisibilityData, index);
    }
    
    public int getWidth() {
        return this.m_topology.getMapWidth();
    }
    
    public int getHeight() {
        return this.m_topology.getMapHeight();
    }
    
    public void read(final ExtendedDataInputStream stream) throws IOException {
        final byte header = stream.readByte();
        this.m_topology.load(stream);
    }
    
    public boolean isSteryl(final int cellX, final int cellY) {
        return this.isInMap(cellX, cellY);
    }
    
    public int getGroupId() {
        return this.m_groupId;
    }
    
    public boolean isCellBlocked(final int cellX, final int cellY) {
        assert this.m_topology.isInMap(cellX, cellY);
        return this.m_topology.isCellBlocked(cellX, cellY);
    }
    
    public boolean isCellEmptyOrAltitudeEquals0(final int cellX, final int cellY) {
        assert this.m_topology.isInMap(cellX, cellY);
        return this.m_topology.cellIsEmpty(cellX, cellY) || this.m_topology.altitudeEquals0(cellX, cellY);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractEditorGroupMap.class);
    }
}
