package com.ankamagames.wakfu.client.alea.graphics.tacticalView.map;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import gnu.trove.*;
import java.util.*;

public class TacticalMap
{
    private static final Logger m_logger;
    private final Cell[][] m_cells;
    private final FightMap m_fightMap;
    private final TIntArrayList m_distinctZ;
    
    public TacticalMap(final FightMap fightMap) {
        super();
        this.m_distinctZ = new TIntArrayList();
        this.m_fightMap = fightMap;
        this.m_cells = new Cell[fightMap.getWidth()][fightMap.getHeight()];
        this.fillCellsFromTopology(fightMap);
        this.calculateDistinctZ();
    }
    
    private void calculateDistinctZ() {
        this.m_distinctZ.clear();
        final int width = this.m_fightMap.getWidth();
        final int height = this.m_fightMap.getHeight();
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                for (final Block b : this.m_cells[i][j].m_blocks) {
                    if (!this.m_distinctZ.contains(b.maxZ)) {
                        this.m_distinctZ.add(b.maxZ);
                    }
                }
            }
        }
        this.m_distinctZ.sort();
    }
    
    private void fillCellsFromTopology(final FightMap fightMap) {
        final int width = fightMap.getWidth();
        final int height = fightMap.getHeight();
        final int minX = fightMap.getMinX();
        final int minY = fightMap.getMinY();
        final int minZ = fightMap.getMinZ();
        final int maxZ = fightMap.getMaxZ();
        final boolean[][] fightCells = this.computeFightCells(fightMap);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (fightCells[i][j]) {
                    final boolean insideBubble = fightMap.isInsideOrBorder(i + minX, j + minY);
                    this.m_cells[i][j] = Cell.createFromTopology(insideBubble, i + minX, j + minY, minZ, maxZ);
                }
                else {
                    this.m_cells[i][j] = Cell.EMPTY_CELL;
                }
            }
        }
    }
    
    private boolean[][] computeFightCells(final FightMap fightMap) {
        final int width = fightMap.getWidth();
        final int height = fightMap.getHeight();
        final int minX = fightMap.getMinX();
        final int minY = fightMap.getMinY();
        final boolean[][] cells = new boolean[fightMap.getWidth()][fightMap.getHeight()];
        for (int i = 0; i < width; ++i) {
            int bottom = -1;
            int top = height;
            for (int j = 0; j < height; ++j) {
                if (fightMap.isInsideOrBorder(i + minX, j + minY)) {
                    bottom = j;
                    break;
                }
            }
            for (int j = height - 1; j >= bottom; --j) {
                if (fightMap.isInsideOrBorder(i + minX, j + minY)) {
                    top = j;
                    break;
                }
            }
            top = Math.min(height - 1, top);
            for (int j = Math.max(0, bottom); j <= top; ++j) {
                cells[i][j] = true;
            }
            if (bottom != -1) {
                for (int j = bottom; j <= top; ++j) {
                    cells[i][j] = true;
                }
            }
        }
        for (int k = 0; k < height; ++k) {
            int left = -1;
            int right = width;
            for (int l = 0; l < width; ++l) {
                if (fightMap.isInsideOrBorder(l + minX, k + minY)) {
                    left = l;
                    break;
                }
            }
            for (int l = width - 1; l >= Math.max(0, left); --l) {
                if (fightMap.isInsideOrBorder(l + minX, k + minY)) {
                    right = l;
                    break;
                }
            }
            right = Math.min(width - 1, right);
            if (left != -1) {
                assert right != width;
                for (int l = left; l <= right; ++l) {
                    cells[l][k] = true;
                }
            }
        }
        return cells;
    }
    
    public DisplayBlock getDisplayBlockAt(final int x, final int y, final int z) {
        if (!this.m_fightMap.isInMap(x, y)) {
            return null;
        }
        final int i = x - this.m_fightMap.getMinX();
        final int j = y - this.m_fightMap.getMinY();
        final Block block = this.m_cells[i][j].getBlock(z);
        if (block == null) {
            return null;
        }
        final int left = this.getNeighborZ(block, i, j + 1);
        final int right = this.getNeighborZ(block, i + 1, j);
        final int top = block.maxZ;
        return new DisplayBlock(block.walkable, top, top - left, top - right, x, y, block.blockLos);
    }
    
    public int getMinZ() {
        return this.m_fightMap.getMinZ();
    }
    
    public Cell getCell(final int cellX, final int cellY) {
        if (!this.m_fightMap.isInMap(cellX, cellY)) {
            return null;
        }
        return this.m_cells[cellX - this.m_fightMap.getMinX()][cellY - this.m_fightMap.getMinY()];
    }
    
    public boolean isInMap(final int i, final int j) {
        return this.m_fightMap.isInMap(i + this.m_fightMap.getMinX(), j + this.m_fightMap.getMinY());
    }
    
    private int getNeighborZ(final Block base, final int i, final int j) {
        if (!this.isInMap(i, j)) {
            return this.m_fightMap.getMinZ();
        }
        final Cell cell = this.m_cells[i][j];
        final Block neighbor = cell.getBlockUnder(base.maxZ);
        if (neighbor == null) {
            return cell.m_blocks.isEmpty() ? this.m_fightMap.getMinZ() : base.maxZ;
        }
        return Math.max(base.minZ, neighbor.maxZ);
    }
    
    public float getZIndexRatio(final float z) {
        final int index = this.m_distinctZ.indexOf((int)z);
        return index / this.m_distinctZ.size();
    }
    
    static {
        m_logger = Logger.getLogger((Class)TacticalMap.class);
    }
}
