package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class TopologyMapInstance
{
    private static final CellPathData[] m_cellPathData;
    private LongLightWeightSet m_blockedCellsAltitude;
    private final ByteArrayBitSet m_entirelyBlockedCells;
    private final ByteArrayBitSet m_usedInFight;
    private final TopologyMap m_topologyMap;
    
    public TopologyMapInstance(@NotNull final TopologyMap topologyMap) {
        super();
        this.m_entirelyBlockedCells = new ByteArrayBitSet(324);
        this.m_usedInFight = new ByteArrayBitSet(324);
        this.m_topologyMap = topologyMap;
    }
    
    public boolean isCellBlocked(int x, int y) {
        if (this.m_topologyMap.isCellBlocked(x, y)) {
            return true;
        }
        x -= this.m_topologyMap.m_x;
        y -= this.m_topologyMap.m_y;
        return this.m_entirelyBlockedCells.get(y * 18 + x);
    }
    
    public boolean isBlocked(final int x, final int y, final short z) {
        return this.isCellBlocked(x, y) || (this.m_blockedCellsAltitude != null && this.m_blockedCellsAltitude.contains(PositionValue.toLong(x, y, z)));
    }
    
    public boolean isCellBlockedForFight(final int x, final int y) {
        if (this.isCellBlocked(x, y)) {
            return true;
        }
        if (this.m_blockedCellsAltitude == null) {
            return false;
        }
        for (int i = 0; i < this.m_blockedCellsAltitude.size(); ++i) {
            final Point3 pos = PositionValue.fromLong(this.m_blockedCellsAltitude.getQuickKey(i));
            if (pos.getX() == x && pos.getY() == y) {
                return true;
            }
        }
        return false;
    }
    
    public final int getMurfinType(final int x, final int y, final int z) {
        assert this.m_topologyMap.isInMap(x, y);
        final int zCount = this.m_topologyMap.getPathData(x, y, TopologyMapInstance.m_cellPathData, 0);
        if (zCount == 0) {
            return 0;
        }
        for (int i = 0; i < zCount; ++i) {
            if (TopologyMapInstance.m_cellPathData[i].m_z == z) {
                return TopologyMapInstance.m_cellPathData[i].getMurfinType();
            }
        }
        return 0;
    }
    
    public boolean isIndoor(final int x, final int y, final int z) {
        return CellPathData.isIndoor(this.getMurfinType(x, y, z));
    }
    
    public boolean isUsedInFight(int x, int y) {
        x -= this.m_topologyMap.m_x;
        y -= this.m_topologyMap.m_y;
        return this.m_usedInFight.get(y * 18 + x);
    }
    
    public final TopologyMap getTopologyMap() {
        return this.m_topologyMap;
    }
    
    public void setCellBlocked(int x, int y, final boolean blocked) {
        if (this.isCellBlocked(x, y) == blocked) {
            return;
        }
        x -= this.m_topologyMap.m_x;
        y -= this.m_topologyMap.m_y;
        this.m_entirelyBlockedCells.set(y * 18 + x, blocked);
    }
    
    public void setBlocked(final int x, final int y, final short z, final boolean blocked) {
        if (this.isBlocked(x, y, z) == blocked) {
            return;
        }
        if (this.getTopologyMap().getPathData(x, y, TopologyMapInstance.m_cellPathData, 0) == 1) {
            this.setCellBlocked(x, y, blocked);
            return;
        }
        if (!blocked) {
            if (this.m_blockedCellsAltitude != null) {
                this.m_blockedCellsAltitude.remove(PositionValue.toLong(x, y, z));
            }
        }
        else {
            if (this.m_blockedCellsAltitude == null) {
                this.m_blockedCellsAltitude = new LongLightWeightSet();
            }
            this.m_blockedCellsAltitude.add(PositionValue.toLong(x, y, z));
        }
    }
    
    public void setUsedInFight(int x, int y, final boolean usedInFight) {
        if (this.isUsedInFight(x, y) == usedInFight) {
            return;
        }
        if (usedInFight) {
            if (!this.isCellBlockedForFight(x, y)) {
                x -= this.m_topologyMap.m_x;
                y -= this.m_topologyMap.m_y;
                this.m_usedInFight.set(y * 18 + x, true);
            }
        }
        else {
            x -= this.m_topologyMap.m_x;
            y -= this.m_topologyMap.m_y;
            this.m_usedInFight.set(y * 18 + x, false);
        }
    }
    
    static {
        m_cellPathData = new CellPathData[32];
        for (int i = 0; i < TopologyMapInstance.m_cellPathData.length; ++i) {
            TopologyMapInstance.m_cellPathData[i] = new CellPathData();
        }
    }
}
