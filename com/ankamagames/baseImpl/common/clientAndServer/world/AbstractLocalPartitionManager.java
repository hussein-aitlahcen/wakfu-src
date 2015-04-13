package com.ankamagames.baseImpl.common.clientAndServer.world;

import org.apache.log4j.*;
import java.util.*;

public abstract class AbstractLocalPartitionManager<P extends AbstractPartition<P>>
{
    protected static final boolean DEBUG_MODE = false;
    protected static final Logger m_logger;
    protected P m_currentPartition;
    
    protected abstract P createPartition(final int p0, final int p1, final int p2, final int p3, final boolean p4);
    
    private P createPartition(final int centerX, final int centerY) {
        return this.createPartition(centerX, centerY, 18, 18, false);
    }
    
    private P createCentralPartition(final int centerX, final int centerY) {
        return this.createPartition(centerX, centerY, 18, 18, true);
    }
    
    public void initialize(final int worldX, final int worldY) {
        final int centerPartitionX = MapConstants.getMapCoordFromCellX(worldX);
        final int centerPartitionY = MapConstants.getMapCoordFromCellY(worldY);
        final P currentPartition = this.createCentralPartition(centerPartitionX, centerPartitionY);
        currentPartition.setPartition(4, currentPartition);
        this.initializeAdjacentPartitions(currentPartition, centerPartitionX, centerPartitionY);
        this.m_currentPartition = currentPartition;
        AbstractLocalPartitionManager.m_logger.info((Object)(this.getClass().getSimpleName() + " initialis\u00e9, centr\u00e9 sur " + this.m_currentPartition));
    }
    
    private void initializeAdjacentPartitions(final AbstractPartition<P> currentPartition, final int centerPartitionX, final int centerPartitionY) {
        currentPartition.setPartition(7, this.createPartition(centerPartitionX, centerPartitionY + 1));
        currentPartition.setPartition(6, this.createPartition(centerPartitionX - 1, centerPartitionY + 1));
        currentPartition.setPartition(8, this.createPartition(centerPartitionX + 1, centerPartitionY + 1));
        currentPartition.setPartition(3, this.createPartition(centerPartitionX - 1, centerPartitionY));
        currentPartition.setPartition(5, this.createPartition(centerPartitionX + 1, centerPartitionY));
        currentPartition.setPartition(1, this.createPartition(centerPartitionX, centerPartitionY - 1));
        currentPartition.setPartition(0, this.createPartition(centerPartitionX - 1, centerPartitionY - 1));
        currentPartition.setPartition(2, this.createPartition(centerPartitionX + 1, centerPartitionY - 1));
    }
    
    public boolean isInit() {
        return this.m_currentPartition != null;
    }
    
    public final List<P> getAllLocalPartitions() {
        if (this.m_currentPartition != null) {
            return this.m_currentPartition.getLayoutList();
        }
        return null;
    }
    
    public void clear() {
    }
    
    public final P getLocalPartitionAt(final int partitionX, final int partitionY) {
        assert this.m_currentPartition != null;
        final P[] layout = this.m_currentPartition.getLayout();
        for (int i = 0, size = layout.length; i < size; ++i) {
            final P partition = layout[i];
            if (partition.getX() == partitionX && partition.getY() == partitionY) {
                return partition;
            }
        }
        return null;
    }
    
    public final P getLocalPartition(final int cellX, final int cellY) {
        assert this.m_currentPartition != null;
        final P[] layout = this.m_currentPartition.getLayout();
        for (int i = 0, size = layout.length; i < size; ++i) {
            final P partition = layout[i];
            if (partition.isUnitsWithinBounds(cellX, cellY)) {
                return partition;
            }
        }
        return null;
    }
    
    public final P getCurrentPartition() {
        return this.m_currentPartition.getPartition(4);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractLocalPartitionManager.class);
    }
}
