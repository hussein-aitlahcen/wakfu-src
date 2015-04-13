package com.ankamagames.wakfu.client.alea.graphics.tacticalView.map;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;

public class Cell
{
    private static final CellPathData[] m_cellPathData;
    private static final CellVisibilityData[] m_cellVisibilityData;
    final ArrayList<Block> m_blocks;
    static final Cell EMPTY_CELL;
    
    private Cell() {
        super();
        this.m_blocks = new ArrayList<Block>(2);
    }
    
    Block getBlock(final int z) {
        for (int i = this.m_blocks.size() - 1; i >= 0; --i) {
            if (this.m_blocks.get(i).maxZ == z) {
                return this.m_blocks.get(i);
            }
        }
        return null;
    }
    
    public Block getBlockUnder(final int z) {
        for (int i = this.m_blocks.size() - 1; i >= 0; --i) {
            if (this.m_blocks.get(i).maxZ <= z) {
                return this.m_blocks.get(i);
            }
        }
        return null;
    }
    
    static Cell createFromTopology(final boolean inFight, final int worldX, final int worldY, final int minZ, final int maxZ) {
        final TopologyMapInstance map = TopologyMapManager.getMapFromCell(worldX, worldY);
        if (map == null) {
            return Cell.EMPTY_CELL;
        }
        final int numZ = map.getTopologyMap().getPathData(worldX, worldY, Cell.m_cellPathData, 0);
        if (numZ == 0) {
            return Cell.EMPTY_CELL;
        }
        final int numVisibilityZ = map.getTopologyMap().getVisibilityData(worldX, worldY, Cell.m_cellVisibilityData, 0);
        final Cell cell = new Cell();
        for (int i = 0; i < numZ; ++i) {
            final int top = Cell.m_cellPathData[i].m_z;
            int bottom = top - Cell.m_cellPathData[i].m_height;
            if (top >= minZ) {
                if (bottom <= maxZ) {
                    if (bottom < minZ) {
                        bottom = minZ;
                    }
                    boolean walkable = inFight && !map.isBlocked(worldX, worldY, (short)top);
                    if (top > maxZ) {
                        walkable = false;
                    }
                    if (Cell.m_cellPathData[i].m_cost == -1) {
                        walkable = false;
                    }
                    cell.m_blocks.add(new Block(bottom, top, walkable, Cell.m_cellVisibilityData[i].m_hollow));
                }
            }
        }
        return cell;
    }
    
    static {
        m_cellPathData = CellPathData.createCellPathDataTab();
        m_cellVisibilityData = new CellVisibilityData[32];
        for (int i = 0; i < Cell.m_cellVisibilityData.length; ++i) {
            Cell.m_cellVisibilityData[i] = new CellVisibilityData();
        }
        EMPTY_CELL = new Cell();
    }
}
