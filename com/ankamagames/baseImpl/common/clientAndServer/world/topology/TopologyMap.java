package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public abstract class TopologyMap
{
    public static final int MAX_Z_PER_CELLS = 32;
    public static final byte INFINITE_COST = -1;
    public static final byte DEFAULT_COST = 7;
    public int m_x;
    public int m_y;
    public short m_z;
    
    public abstract boolean isCellBlocked(final int p0, final int p1);
    
    public boolean isInMap(final int x, final int y) {
        return x >= this.m_x && x < this.m_x + this.getMapWidth() && y >= this.m_y && y < this.m_y + this.getMapHeight();
    }
    
    protected int getMapWidth() {
        return 18;
    }
    
    protected int getMapHeight() {
        return 18;
    }
    
    public abstract int getPathData(final int p0, final int p1, final CellPathData[] p2, final int p3);
    
    public abstract int getVisibilityData(final int p0, final int p1, final CellVisibilityData[] p2, final int p3);
    
    public void load(final ExtendedDataInputStream stream) throws IOException {
        this.m_x = stream.readShort() * 18;
        this.m_y = stream.readShort() * 18;
        this.m_z = stream.readShort();
    }
    
    protected final boolean checkGetPathData(final int x, final int y, final CellPathData[] cellPathData) {
        assert cellPathData != null;
        assert cellPathData.length >= 1 : "cellPathData array must have a size at least equal to one";
        assert cellPathData[0] != null : "cellpathData array seems not to be initialized";
        assert this.isInMap(x, y) : "The cell (" + x + ", " + y + ") doesn't belong to the map. Make sure that isInMap(x, y) is true before calling getPathData";
        return true;
    }
    
    protected final boolean checkGetVisibilityData(final int x, final int y, final CellVisibilityData[] cellVisibilityData) {
        assert cellVisibilityData != null;
        assert cellVisibilityData.length >= 1 : "cellVisibilityData array must have a size at least equal to one";
        assert cellVisibilityData[0] != null : "cellVisibilityData array seems not to be initialized";
        assert this.isInMap(x, y) : "The cell (" + x + ", " + y + ") doesn't belong to the map. Make sure that isInMap(x, y) is true before calling getVisibilityData";
        return true;
    }
    
    public boolean isEmpty() {
        return false;
    }
}
