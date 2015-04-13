package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import java.io.*;
import com.ankamagames.wakfu.common.alea.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;

public final class FightMapSerializer
{
    private static final Logger m_logger;
    private final FightMap m_fightMap;
    
    public FightMapSerializer(final FightMap fightMap) {
        super();
        this.m_fightMap = fightMap;
    }
    
    public byte[] serialize() {
        return this.serialize(false);
    }
    
    public byte[] serialize(final boolean encodeTopology) {
        final ByteArray buffer = new ByteArray();
        buffer.putShort(this.m_fightMap.getWorldId());
        buffer.putShort(this.m_fightMap.getInstanceId());
        buffer.putInt(this.m_fightMap.getMinX());
        buffer.putInt(this.m_fightMap.getMinY());
        buffer.putInt(this.m_fightMap.getWidth());
        buffer.putInt(this.m_fightMap.getHeight());
        buffer.putShort(this.m_fightMap.getMinZ());
        buffer.putShort(this.m_fightMap.getMaxZ());
        buffer.putShort((short)this.m_fightMap.getNumCells());
        final short[] cells = this.m_fightMap.getCells();
        for (int i = 0; i < this.m_fightMap.getNumCells(); ++i) {
            buffer.putShort(cells[i]);
        }
        buffer.put((byte)(this.m_fightMap.isUseCustomTeamPosition() ? 1 : 0));
        if (this.m_fightMap.isUseCustomTeamPosition()) {
            buffer.put(this.m_fightMap.getTeamPositions());
        }
        if (encodeTopology) {
            buffer.put((byte)1);
            new TopologySerializer(this.m_fightMap).encode(buffer);
        }
        else {
            buffer.put((byte)0);
        }
        buffer.put(this.m_fightMap.getLowXTeamID());
        buffer.put(this.m_fightMap.getLowYTeamID());
        final byte[] obstaclesId = this.m_fightMap.getObstaclesIds();
        buffer.put((byte)obstaclesId.length);
        for (int j = 0; j < obstaclesId.length; ++j) {
            final byte obstacleId = obstaclesId[j];
            buffer.put(obstacleId);
            buffer.putInt(this.m_fightMap.getObstacleCellIndex(obstacleId));
        }
        return buffer.toArray();
    }
    
    public void unserialize(final ByteBuffer buffer) {
        final int position = buffer.position();
        final short worldId = buffer.getShort();
        this.m_fightMap.setWorldId(worldId);
        final short instanceId = buffer.getShort();
        this.m_fightMap.setInstanceId(instanceId);
        final int minX = buffer.getInt();
        this.m_fightMap.setMinX(minX);
        final int minY = buffer.getInt();
        this.m_fightMap.setMinY(minY);
        final int width = buffer.getInt();
        this.m_fightMap.setWidth(width);
        final int height = buffer.getInt();
        this.m_fightMap.setHeight(height);
        this.m_fightMap.setMinZ(buffer.getShort());
        this.m_fightMap.setMaxZ(buffer.getShort());
        final short numCells = buffer.getShort();
        this.m_fightMap.setNumCells(numCells);
        assert buffer.remaining() > numCells * 2;
        final short[] cells = new short[numCells];
        for (int i = 0; i < numCells; ++i) {
            cells[i] = buffer.getShort();
        }
        this.m_fightMap.setCells(cells);
        final boolean useCustomTeamPosition = buffer.get() == 1;
        this.m_fightMap.setUseCustomTeamPosition(useCustomTeamPosition);
        final byte[] teamPositions = new byte[numCells];
        if (useCustomTeamPosition) {
            buffer.get(teamPositions);
        }
        this.m_fightMap.setTeamPositions(teamPositions);
        this.prepareTopology(buffer);
        this.m_fightMap.setLowXTeamID(buffer.get());
        this.m_fightMap.setLowYTeamID(buffer.get());
        final byte obstaclesSize = buffer.get();
        for (int j = 0; j < obstaclesSize; ++j) {
            this.m_fightMap.addObstacleRawInfo(buffer.get(), buffer.getInt());
        }
    }
    
    private static Rect getFightMapPartitionBounds(final FightMap fightMap) {
        return MapConstants.getPartitionsFromCells(fightMap.getMinX(), fightMap.getMinY(), fightMap.getWidth(), fightMap.getHeight());
    }
    
    private void prepareTopology(final ByteBuffer buffer) {
        final Rect bounds = getFightMapPartitionBounds(this.m_fightMap);
        final TopologyGetter topologyGetter = getTopologyGetter(this.m_fightMap, buffer);
        for (int y = bounds.m_yMin; y < bounds.m_yMax; ++y) {
            for (int x = bounds.m_xMin; x < bounds.m_xMax; ++x) {
                final TopologyMapInstance map = topologyGetter.getMap((short)x, (short)y);
                if (map != null) {
                    this.m_fightMap.addMapWithoutControl(map);
                }
            }
        }
    }
    
    private static TopologyGetter getTopologyGetter(final FightMap fightMap, final ByteBuffer buffer) {
        final boolean withTopology = buffer.get() != 0;
        if (TopologyMapManager.useConstantWorld()) {
            assert !withTopology;
            return new TopologyGetter() {
                @Override
                public TopologyMapInstance getMap(final short x, final short y) {
                    return TopologyMapManager.getMap(x, y);
                }
            };
        }
        else {
            final short worldId = fightMap.getWorldId();
            final short instanceId = fightMap.getInstanceId();
            if (withTopology) {
                new TopologySerializer(fightMap).decode(buffer);
                return new TopologyGetter() {
                    @Override
                    public TopologyMapInstance getMap(final short x, final short y) {
                        return TopologyMapManager.getMap(worldId, x, y, instanceId);
                    }
                };
            }
            return new TopologyGetter() {
                @Override
                public TopologyMapInstance getMap(final short x, final short y) {
                    try {
                        TopologyMapManager.loadMap(worldId, x, y);
                        return TopologyMapManager.addTopologyMapInstance(worldId, x, y, instanceId);
                    }
                    catch (IOException e) {
                        FightMapSerializer.m_logger.error((Object)("Unable to load map (" + x + "; " + y + ")"));
                        return null;
                    }
                }
            };
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightMapSerializer.class);
    }
    
    private static class TopologySerializer
    {
        private static final CellPathData[] m_sourceCellPathData;
        private final FightMap m_fightMap;
        
        TopologySerializer(final FightMap fightMap) {
            super();
            this.m_fightMap = fightMap;
        }
        
        public void encode(final ByteArray buffer) {
            final short worldId = this.m_fightMap.getWorldId();
            final short instanceId = this.m_fightMap.getInstanceId();
            final Rect bounds = getFightMapPartitionBounds(this.m_fightMap);
            for (int mapY = bounds.m_yMin; mapY < bounds.m_yMax; ++mapY) {
                for (int mapX = bounds.m_xMin; mapX < bounds.m_xMax; ++mapX) {
                    final TopologyMapInstance map = TopologyMapManager.getMap(worldId, (short)mapX, (short)mapY, instanceId);
                    final FightMapTopology topology = this.convertTopology(map, mapY, mapX);
                    topology.encode(buffer);
                }
            }
        }
        
        private FightMapTopology convertTopology(final TopologyMapInstance map, final int mapY, final int mapX) {
            final FightMapTopology fightMapTopology = new FightMapTopology(mapX, mapY);
            final TopologyMapPatch mapPatch = (TopologyMapPatch)map.getTopologyMap();
            for (int dy = 0; dy < 18; ++dy) {
                for (int dx = 0; dx < 18; ++dx) {
                    final int x = fightMapTopology.m_x + dx;
                    final int y = fightMapTopology.m_y + dy;
                    final short altitude = getCellAltitude(mapPatch, x, y);
                    fightMapTopology.setCellAltitude(x, y, altitude);
                }
            }
            return fightMapTopology;
        }
        
        private static short getCellAltitude(final TopologyMapPatch mapPatch, final int x, final int y) {
            if (mapPatch.isCellBlocked(x, y)) {
                return -32768;
            }
            final int index = mapPatch.getPathData(x, y, TopologySerializer.m_sourceCellPathData, 0);
            if (index != 1) {
                return -32768;
            }
            final CellPathData data = TopologySerializer.m_sourceCellPathData[0];
            if (data.getGroupId() != 0 || data.m_cost == -1) {
                return -32768;
            }
            return data.m_z;
        }
        
        public void decode(final ByteBuffer buffer) {
            final short worldId = this.m_fightMap.getWorldId();
            final short instanceId = this.m_fightMap.getInstanceId();
            final Rect bounds = getFightMapPartitionBounds(this.m_fightMap);
            for (int mapY = bounds.m_yMin; mapY < bounds.m_yMax; ++mapY) {
                for (int mapX = bounds.m_xMin; mapX < bounds.m_xMax; ++mapX) {
                    final FightMapTopology map = new FightMapTopology(mapX, mapY);
                    map.decode(buffer);
                    TopologyMapManager.insertTopologyMapPatch(worldId, (short)mapX, (short)mapY, instanceId, map);
                }
            }
        }
        
        static {
            m_sourceCellPathData = CellPathData.createCellPathDataTab();
        }
    }
    
    private interface TopologyGetter
    {
        TopologyMapInstance getMap(short p0, short p1);
    }
}
