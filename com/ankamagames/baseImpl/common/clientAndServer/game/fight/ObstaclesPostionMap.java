package com.ankamagames.baseImpl.common.clientAndServer.game.fight;

import gnu.trove.*;

public final class ObstaclesPostionMap
{
    private final TIntObjectHashMap<TByteHashSet> m_positionsToObstaclesMap;
    private final TByteIntHashMap m_lastKnownCellIndex;
    
    public ObstaclesPostionMap() {
        super();
        this.m_positionsToObstaclesMap = new TIntObjectHashMap<TByteHashSet>();
        this.m_lastKnownCellIndex = new TByteIntHashMap();
    }
    
    void addObstacleAndSetKnownIndex(final byte obstacleId, final int cellIndex) {
        this.addObstacleToCellIndex(obstacleId, cellIndex);
        this.m_lastKnownCellIndex.put(obstacleId, cellIndex);
    }
    
    void addObstacleToCellIndex(final byte obstacleId, final int cellIndex) {
        TByteHashSet obstacleList = this.m_positionsToObstaclesMap.get(cellIndex);
        if (obstacleList == null) {
            obstacleList = new TByteHashSet();
            this.m_positionsToObstaclesMap.put(cellIndex, obstacleList);
        }
        obstacleList.add(obstacleId);
    }
    
    void removeObstacle(final byte obstacleId) {
        final TByteHashSet obstacleList = this.m_positionsToObstaclesMap.get(this.m_lastKnownCellIndex.get(obstacleId));
        this.m_lastKnownCellIndex.remove(obstacleId);
        if (obstacleList == null) {
            return;
        }
        obstacleList.remove(obstacleId);
    }
    
    void removeRadiusObstacle(final byte obstacleId) {
        this.m_lastKnownCellIndex.remove(obstacleId);
        final TIntObjectIterator<TByteHashSet> it = this.m_positionsToObstaclesMap.iterator();
        while (it.hasNext()) {
            it.advance();
            final TByteHashSet obstaclesId = it.value();
            obstaclesId.remove(obstacleId);
        }
    }
    
    TByteHashSet getObstaclesOnPosition(final int cellIndex) {
        return this.m_positionsToObstaclesMap.get(cellIndex);
    }
    
    public int getLastKnownCellIndex(final byte obstacleId) {
        return this.m_lastKnownCellIndex.get(obstacleId);
    }
    
    public boolean contains(final byte obstacleId) {
        return this.m_lastKnownCellIndex.contains(obstacleId);
    }
    
    public byte[] getObstaclesIds() {
        return this.m_lastKnownCellIndex.keys();
    }
}
