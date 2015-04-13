package com.ankamagames.baseImpl.common.clientAndServer.game.fight;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.byteKey.*;
import org.apache.log4j.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import java.io.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;

public class FightMap extends TopologyMapInstanceSet
{
    protected short[] m_cells;
    protected int m_numCells;
    protected int m_numBorderCells;
    protected int m_centerX;
    protected int m_centerY;
    protected short m_centerZ;
    protected boolean m_hasBorder;
    protected short m_minZ;
    protected short m_maxZ;
    protected short m_worldId;
    protected short m_instanceId;
    private final TByteHashSet m_ignoredMovementObstacles;
    private final TByteHashSet m_ignoredSightObstacles;
    private boolean m_ignoreAllMovementObstacles;
    private final FightObstacle[] m_obstacles;
    protected final ObstaclesPostionMap m_obstaclesPosition;
    private boolean m_useCustomTeamPosition;
    private byte[] m_teamPositions;
    private final TeamCellsInfos m_teamCellsInfos;
    protected byte m_lowXTeamID;
    protected byte m_lowYTeamID;
    private ByteObjectLightWeightMap<int[]> m_teamCenters;
    public static final byte MAX_OBSTACLE_ID = 62;
    protected static final int MOVEMENT_BLOCKED_MASK = 512;
    protected static final int INVALID_CELL_MASK = 256;
    protected static final int BORDER_MASK = 128;
    protected static final int CELLDATA_INDEX_MASK = 63;
    protected static final int OBSTACLE_ID_POSITION = 10;
    protected static final int CELLDATA_INDEX_POSITION = 0;
    protected static final int DEFAULT_VALUE_FOR_CELL_OUTSIDE_OF_MAP = 65279;
    protected static final boolean DEBUG_FIGHTMAP = false;
    private static final Logger m_debugLogger;
    protected static final CellPathData[] m_sourceCellPathData;
    private static final Logger m_logger;
    private List<int[]> m_insideCells;
    
    public FightMap() {
        super();
        this.m_ignoredMovementObstacles = new TByteHashSet();
        this.m_ignoredSightObstacles = new TByteHashSet();
        this.m_ignoreAllMovementObstacles = false;
        this.m_obstacles = new FightObstacle[63];
        this.m_obstaclesPosition = new ObstaclesPostionMap();
        this.m_teamCellsInfos = new TeamCellsInfos();
        this.m_lowXTeamID = 0;
        this.m_lowYTeamID = 0;
    }
    
    @Override
    public boolean isMovementBlocked(final int x, final int y, final short z) {
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        if (cellIndex < 0 || cellIndex >= this.m_numCells) {
            FightMap.m_logger.info((Object)"trying to get information in a fightmap on a out of bounds cell");
            return true;
        }
        final short cellValue = this.m_cells[cellIndex];
        if ((cellValue & 0x300) != 0x0) {
            return true;
        }
        if (this.m_ignoreAllMovementObstacles) {
            return false;
        }
        final TByteHashSet obstaclesId = this.getObstaclesIdFromPos(x, y);
        if (obstaclesId == null || obstaclesId.isEmpty()) {
            return false;
        }
        for (final byte obstacleId : obstaclesId) {
            final FightObstacle obstacle = this.m_obstacles[obstacleId];
            if (obstacle == null) {
                return false;
            }
            final boolean movementBlocked = obstacle.isBlockingMovement();
            if (movementBlocked && !this.m_ignoredMovementObstacles.contains(obstacleId)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isSightBlocked(final int x, final int y, final short z) {
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        if (cellIndex < 0 || cellIndex >= this.m_numCells) {
            FightMap.m_logger.info((Object)"trying to get information in a fightmap on a out of bounds cell");
            return true;
        }
        final short cellValue = this.m_cells[cellIndex];
        if ((cellValue & 0x100) == 0x100) {
            return true;
        }
        final TByteHashSet obstaclesId = this.getObstaclesIdFromPos(x, y);
        if (obstaclesId == null || obstaclesId.isEmpty()) {
            return false;
        }
        for (final byte obstacleId : obstaclesId) {
            final FightObstacle obstacle = this.m_obstacles[obstacleId];
            if (obstacle == null) {
                return false;
            }
            final boolean sightBlocked = obstacle.isBlockingSight();
            if (sightBlocked && !this.m_ignoredSightObstacles.contains(obstacleId)) {
                return true;
            }
        }
        return false;
    }
    
    public short[] getCells() {
        return this.m_cells;
    }
    
    public int getNumCells() {
        return this.m_numCells;
    }
    
    public int getNumBorderCells() {
        return this.m_numBorderCells;
    }
    
    public byte getLowXTeamID() {
        return this.m_lowXTeamID;
    }
    
    public byte getLowYTeamID() {
        return this.m_lowYTeamID;
    }
    
    public boolean isUseCustomTeamPosition() {
        return this.m_useCustomTeamPosition;
    }
    
    public byte[] getTeamPositions() {
        return this.m_teamPositions;
    }
    
    public boolean isAreaOfEffectCellsAllInsideOrBorderAndFree(final AreaOfEffect area, final int centerX, final int centerY, final short centerZ, final int casterX, final int casterY, final short casterZ, final Direction8 direction) {
        if (area == null) {
            return true;
        }
        final Iterable<int[]> iterable = area.getCells(centerX, centerY, centerZ, casterX, casterY, casterZ, direction);
        if (iterable == null) {
            return true;
        }
        for (final int[] cells : iterable) {
            if (!this.isInsideOrBorder(cells[0], cells[1])) {
                return false;
            }
            if (this.getObstacle(cells[0], cells[1]) != null) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isBorder(final int x, final int y) {
        if (!this.isInMap(x, y)) {
            return false;
        }
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        return this.m_cells[cellIndex] != -257 && (this.m_cells[cellIndex] & 0x80) != 0x0;
    }
    
    public boolean isInside(final int x, final int y) {
        if (!this.isInMap(x, y)) {
            return false;
        }
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        return this.isInside(cellIndex);
    }
    
    private boolean isInside(final int cellIndex) {
        return (this.m_cells[cellIndex] & 0x80) == 0x0;
    }
    
    public boolean isInsideOrBorder(final int x, final int y) {
        if (!this.isInMap(x, y)) {
            return false;
        }
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        return this.isInsideOrBorder(cellIndex);
    }
    
    private boolean isInsideOrBorder(final int cellIndex) {
        return (this.m_cells[cellIndex] & 0xFFFFFEFF) != 0xFFFFFEFF;
    }
    
    public FightObstacle[] getObstacles() {
        return this.m_obstacles;
    }
    
    public FightObstacle getObstacleFromId(final byte obstacleId) {
        if (!this.checkObstacleId(obstacleId)) {
            return null;
        }
        return this.m_obstacles[obstacleId];
    }
    
    public boolean isInsideMapShape(final int x, final int y) {
        return this.isInMap(x, y) && (this.isInsideOrBorder(x, y) || this.isInside(x - 1, y) || this.isInside(x, y - 1) || this.isInside(x + 1, y) || this.isInside(x, y + 1));
    }
    
    public byte getObstacleIdFromPos(final int x, final int y) {
        if (x < this.m_minX || x >= this.m_minX + this.m_width || y < this.m_minY || y >= this.m_minY + this.m_height) {
            return -1;
        }
        final TByteHashSet res = this.m_obstaclesPosition.getObstaclesOnPosition(this.getCellIndexFromCoords(x, y));
        if (res == null || res.isEmpty()) {
            return -1;
        }
        return res.iterator().next();
    }
    
    public TByteHashSet getObstaclesIdFromPos(final int x, final int y) {
        final TByteHashSet res = new TByteHashSet();
        if (x < this.m_minX || x >= this.m_minX + this.m_width || y < this.m_minY || y >= this.m_minY + this.m_height) {
            return res;
        }
        return this.m_obstaclesPosition.getObstaclesOnPosition(this.getCellIndexFromCoords(x, y));
    }
    
    public int getObstacleCellIndex(final byte obstacleId) {
        return this.m_obstaclesPosition.getLastKnownCellIndex(obstacleId);
    }
    
    public byte[] getObstaclesIds() {
        return this.m_obstaclesPosition.getObstaclesIds();
    }
    
    protected int getObstacleCellIndex(final FightObstacle obstacle) {
        return this.getCellIndexFromCoords(obstacle.getWorldCellX(), obstacle.getWorldCellY());
    }
    
    @Nullable
    public FightObstacle getObstacle(final int x, final int y) {
        final int id = this.getObstacleIdFromPos(x, y);
        if (id < 0) {
            return null;
        }
        return this.m_obstacles[id];
    }
    
    public void addIgnoredMovementObstacle(final FightObstacle obstacle) {
        assert obstacle != null : "can't work on a null obstacle";
        this.m_ignoredMovementObstacles.add(obstacle.getObstacleId());
    }
    
    public void removeIgnoredMovementObstacle(final FightObstacle obstacle) {
        assert obstacle != null : "can't work on a null obstacle";
        this.m_ignoredMovementObstacles.remove(obstacle.getObstacleId());
    }
    
    public void clearIgnoredMovementObstacles() {
        this.m_ignoredMovementObstacles.clear();
    }
    
    public boolean areAllMovementObstaclesIgnored() {
        return this.m_ignoreAllMovementObstacles;
    }
    
    public void setIgnoreAllMovementObstacles(final boolean ignoreAllMovementObstacles) {
        this.m_ignoreAllMovementObstacles = ignoreAllMovementObstacles;
    }
    
    public void addIgnoredSightObstacle(final FightObstacle obstacle) {
        assert obstacle != null : "can't work on a null obstacle";
        this.m_ignoredSightObstacles.add(obstacle.getObstacleId());
    }
    
    public void removeIgnoredSightObstacle(final FightObstacle obstacle) {
        assert obstacle != null : "can't work on a null obstacle";
        this.m_ignoredSightObstacles.remove(obstacle.getObstacleId());
    }
    
    public void clearIgnoredSightObstacles() {
        this.m_ignoredSightObstacles.clear();
    }
    
    public void addObstacle(final FightObstacle obstacle) {
        if (obstacle == null) {
            return;
        }
        if (!obstacle.canBlockMovementOrSight()) {
            return;
        }
        this.assignNextFreeObstacleId(obstacle);
        if (this.isInMap(obstacle.getWorldCellX(), obstacle.getWorldCellY())) {
            this.setObstacleAt(obstacle, obstacle.getWorldCellX(), obstacle.getWorldCellY());
        }
    }
    
    public void addObstacleRawInfo(final byte obstacleId, final int cellIndex) {
        this.m_obstaclesPosition.addObstacleAndSetKnownIndex(obstacleId, cellIndex);
    }
    
    public void removeObstacle(final FightObstacle obstacle) {
        if (obstacle == null) {
            return;
        }
        if (!obstacle.canBlockMovementOrSight()) {
            return;
        }
        if (!this.checkObstacleId(obstacle.getObstacleId())) {
            return;
        }
        if (this.m_obstacles[obstacle.getObstacleId()] == obstacle) {
            if (this.isInMap(obstacle.getWorldCellX(), obstacle.getWorldCellY())) {
                this.unsetObstacle(obstacle);
            }
            this.m_obstacles[obstacle.getObstacleId()] = null;
        }
        obstacle.setObstacleId((byte)(-1));
    }
    
    public void modifyObstacle(@NotNull final FightObstacle obstacle, final boolean set) {
        assert obstacle.canBlockMovementOrSight();
        assert this.m_obstacles[obstacle.getObstacleId()] == obstacle : "This obstacle must have been added with addObstacle";
        final int x = obstacle.getWorldCellX();
        final int y = obstacle.getWorldCellY();
        if (!this.isInMap(x, y)) {
            return;
        }
        if (set) {
            this.setObstacleAt(obstacle, x, y);
        }
        else {
            this.unsetObstacle(obstacle);
        }
    }
    
    public void moveObstacle(final FightObstacle obstacle, final int x, final int y) {
        assert obstacle != null : "can't work on a null obstacle";
        if (!obstacle.canBlockMovementOrSight()) {
            return;
        }
        if (this.isInMap(obstacle.getWorldCellX(), obstacle.getWorldCellY())) {
            this.unsetObstacle(obstacle);
        }
        if (this.isInMap(x, y)) {
            this.setObstacleAt(obstacle, x, y);
        }
    }
    
    private void setObstacleAt(final FightObstacle obstacle, final int x, final int y) {
        if (obstacle == null) {
            FightMap.m_logger.error((Object)"On passe un obstacle null");
            return;
        }
        if (!this.checkObstacleId(obstacle.getObstacleId())) {
            FightMap.m_logger.error((Object)(" l'id de l'obstacle est invalide : " + obstacle.getObstacleId() + " : " + obstacle));
            return;
        }
        final byte obstacleId = obstacle.getObstacleId();
        if (this.m_obstacles[obstacleId] != null && this.m_obstacles[obstacleId] != obstacle) {
            FightMap.m_logger.error((Object)("ATTENTION !!! On veut placer un obstacle dans la FightMap mais il existe deja un obstacle avec le meme ID " + obstacleId));
            return;
        }
        final byte obstacleRadius = obstacle.getPhysicalRadius();
        if (obstacleRadius <= 0 && !this.isInMap(x, y)) {
            FightMap.m_logger.error((Object)"Les coordonn\u00e9es doivent etre dans la FightMap");
            return;
        }
        if (this.m_obstacles[obstacleId] == null) {
            this.m_obstacles[obstacleId] = obstacle;
        }
        this.m_obstaclesPosition.addObstacleAndSetKnownIndex(obstacleId, this.getCellIndexFromCoords(x, y));
        if (obstacleRadius <= 0) {
            return;
        }
        for (int cellX = x - obstacleRadius; cellX <= x + obstacleRadius; ++cellX) {
            for (int cellY = y - obstacleRadius; cellY <= y + obstacleRadius; ++cellY) {
                if (this.isInMap(cellX, cellY)) {
                    if (cellX != x || cellY != y) {
                        final int cellIndex = this.getCellIndexFromCoords(cellX, cellY);
                        this.m_obstaclesPosition.addObstacleToCellIndex(obstacleId, cellIndex);
                    }
                }
            }
        }
    }
    
    private void unsetObstacle(final FightObstacle obstacle) {
        if (obstacle == null) {
            return;
        }
        if (obstacle.getPhysicalRadius() <= 0) {
            this.m_obstaclesPosition.removeObstacle(obstacle.getObstacleId());
        }
        else if (obstacle.getPhysicalRadius() > 0) {
            this.m_obstaclesPosition.removeRadiusObstacle(obstacle.getObstacleId());
        }
    }
    
    protected int getCellIndexFromCoords(final int cellX, final int cellY) {
        return (cellY - this.m_minY) * this.m_width + (cellX - this.m_minX);
    }
    
    public int getYFromCellIndex(final int cellIndex) {
        return cellIndex / this.m_width + this.m_minY;
    }
    
    public int getXFromCellIndex(final int cellIndex) {
        return cellIndex % this.m_width + this.m_minX;
    }
    
    public void assignObstacleWithId(final FightObstacle obstacle) {
        if (obstacle == null) {
            return;
        }
        final byte id = obstacle.getObstacleId();
        if (this.m_obstacles[id] != obstacle) {
            this.unsetObstacle(this.m_obstacles[id]);
            this.setObstacleAt(obstacle, obstacle.getWorldCellX(), obstacle.getWorldCellY());
        }
        this.m_obstacles[id] = obstacle;
    }
    
    private byte assignNextFreeObstacleId(final FightObstacle obstacle) {
        assert obstacle != null;
        final int len = this.m_obstacles.length;
        for (byte b = 0; b < len; ++b) {
            if (this.m_obstacles[b] == null) {
                (this.m_obstacles[b] = obstacle).setObstacleId(b);
                return b;
            }
        }
        return -1;
    }
    
    public void blockFightingGroundInTopology(final boolean usedInFight, final boolean useInstanceId) {
        for (int x = this.m_minX; x < this.m_minX + this.m_width; ++x) {
            for (int y = this.m_minY; y < this.m_minY + this.m_height; ++y) {
                if (this.isInsideOrBorder(x, y)) {
                    final TopologyMapInstance topologyMapInstance = TopologyMapManager.getMapFromCell(this.m_worldId, x, y, (short)(useInstanceId ? this.m_instanceId : 0));
                    if (topologyMapInstance != null) {
                        topologyMapInstance.setUsedInFight(x, y, usedInFight);
                    }
                }
            }
        }
    }
    
    public boolean checkObstacleId(final byte id) {
        return id >= 0 && id <= 62;
    }
    
    public short getCellHeight(final int x, final int y) {
        assert x >= this.m_minX && x < this.m_minX + this.m_width && y >= this.m_minY && y < this.m_minY + this.m_height : "Coords must be in the fightMap, you can call isInMap to do make sure that's the case";
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        final int dataIndex = (this.m_cells[cellIndex] & 0x3F) >>> 0;
        final int numMaps = this.m_maps.size();
        int i = 0;
        while (i < numMaps) {
            final TopologyMapInstance mapInstance = this.m_maps.get(i);
            final TopologyMap map = mapInstance.getTopologyMap();
            if (!map.isInMap(x, y)) {
                ++i;
            }
            else {
                final int numZ = map.getPathData(x, y, FightMap.m_sourceCellPathData, 0);
                if (dataIndex >= numZ) {
                    return -32768;
                }
                return FightMap.m_sourceCellPathData[dataIndex].m_z;
            }
        }
        return -32768;
    }
    
    public short getMinZ() {
        return this.m_minZ;
    }
    
    public short getMaxZ() {
        return this.m_maxZ;
    }
    
    public boolean hasBorder() {
        return this.m_hasBorder;
    }
    
    public short getWorldId() {
        return this.m_worldId;
    }
    
    public void setWorldId(final short worldId) {
        this.m_worldId = worldId;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public void setInstanceId(final short instanceId) {
        this.m_instanceId = instanceId;
    }
    
    public void setLowXTeamID(final byte lowXTeamID) {
        this.m_lowXTeamID = lowXTeamID;
    }
    
    public void setLowYTeamID(final byte lowYTeamID) {
        this.m_lowYTeamID = lowYTeamID;
    }
    
    public void setMaxZ(final short maxZ) {
        this.m_maxZ = maxZ;
    }
    
    public void setMinZ(final short minZ) {
        this.m_minZ = minZ;
    }
    
    public void setTeamPositions(final byte[] teamPositions) {
        this.m_teamPositions = teamPositions;
    }
    
    public void setUseCustomTeamPosition(final boolean useCustomTeamPosition) {
        this.m_useCustomTeamPosition = useCustomTeamPosition;
    }
    
    public void setMinX(final int minX) {
        this.m_minX = minX;
    }
    
    public void setMinY(final int minY) {
        this.m_minY = minY;
    }
    
    public void setNumCells(final int numCells) {
        this.m_numCells = numCells;
    }
    
    public void setCells(final short[] cells) {
        this.m_cells = cells;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
    }
    
    public TLongArrayList getTeamCells(final byte teamId) {
        return this.m_teamCellsInfos.getTeamCells(teamId);
    }
    
    public void fillTeamCellsInfos() {
        for (int x = this.m_minX; x < this.m_maxX; ++x) {
            for (int y = this.m_minY; y < this.m_maxY; ++y) {
                final byte teamId = this.getTeamValidForStartPosition(x, y);
                if (teamId != -1) {
                    this.m_teamCellsInfos.putTeamCell(teamId, x, y);
                }
            }
        }
    }
    
    public void fillWithRandomTeamCellsInfos(final int placementCellsNeededPerTeam) {
        this.m_teamPositions = new byte[this.m_numCells];
        final TIntArrayList cells = new TIntArrayList();
        for (int x = this.m_minX; x < this.m_maxX; ++x) {
            for (int y = this.m_minY; y < this.m_maxY; ++y) {
                final byte teamId = this.getTeamValidForStartPosition(x, y);
                if (teamId != -1) {
                    cells.add(this.getCellIndexFromCoords(x, y));
                }
            }
        }
        if (cells.size() < placementCellsNeededPerTeam * 2) {
            return;
        }
        final Point3[][] randomizedCells = new Point3[2][placementCellsNeededPerTeam];
        for (int i = 0; i < placementCellsNeededPerTeam; ++i) {
            int randomizedCell = cells.remove(MathHelper.random(cells.size()));
            randomizedCells[0][i] = new Point3(this.getXFromCellIndex(randomizedCell), this.getYFromCellIndex(randomizedCell));
            randomizedCell = cells.remove(MathHelper.random(cells.size()));
            randomizedCells[1][i] = new Point3(this.getXFromCellIndex(randomizedCell), this.getYFromCellIndex(randomizedCell));
        }
        for (int i = 0; i < randomizedCells.length; ++i) {
            final Point3[] randomizedCell2 = randomizedCells[i];
            this.addCustomTeamPosition(randomizedCell2, (byte)i);
        }
    }
    
    public void addCustomTeamPosition(final Point3[] positions, final byte id) {
        assert id != -1 : "Id can't be equal to -1 since this value is used when the cell is not a valid position";
        assert this.m_numCells != 0 : "The fight map must be created before custom teams are added";
        if (!this.m_useCustomTeamPosition) {
            this.m_useCustomTeamPosition = true;
            this.m_teamPositions = new byte[this.m_numCells];
            this.m_teamCenters = new ByteObjectLightWeightMap<int[]>(2);
            for (int i = 0; i < this.m_teamPositions.length; ++i) {
                this.m_teamPositions[i] = -1;
            }
        }
        int centerX = 0;
        int centerY = 0;
        int minx = 0;
        int miny = 0;
        int maxx = 0;
        int maxy = 0;
        for (int numElements = positions.length, j = 0; j < numElements; ++j) {
            final Point3 position = positions[j];
            if (position != null) {
                final int x = position.getX();
                final int y = position.getY();
                assert this.isInside(x, y) : "The start position (" + x + "; " + y + ") is not in the fightMap";
                if (j == 0) {
                    centerX = x;
                    centerY = y;
                    minx = x;
                    miny = y;
                    maxx = x;
                    maxy = y;
                }
                else {
                    if (x < minx) {
                        minx = x;
                    }
                    if (y < miny) {
                        miny = y;
                    }
                    if (x > maxx) {
                        maxx = x;
                    }
                    if (y > maxy) {
                        maxy = y;
                    }
                    final int appx = maxx - (maxx - minx) / 2;
                    final int appy = maxy - (maxy - miny) / 2;
                    if (Math.abs(x - appx) + Math.abs(y - appy) < Math.abs(centerX - appx) + Math.abs(centerY - appy)) {
                        centerX = x;
                        centerY = y;
                    }
                }
                this.m_teamPositions[this.getCellIndexFromCoords(x, y)] = id;
                this.m_teamCellsInfos.putTeamCell(id, x, y);
            }
        }
        this.m_teamCenters.put(id, new int[] { centerX, centerY });
    }
    
    public byte getTeamValidForStartPosition(final int x, final int y) {
        if (!this.isInside(x, y)) {
            return -1;
        }
        if (this.m_useCustomTeamPosition) {
            return this.m_teamPositions[this.getCellIndexFromCoords(x, y)];
        }
        if (this.m_width >= this.m_height) {
            final int positiongetXLength = this.m_width / 2;
            return (x < this.m_minX + positiongetXLength) ? this.m_lowXTeamID : ((byte)(1 - this.m_lowXTeamID));
        }
        final int positiongetYLength = this.m_height / 2;
        return (y < this.m_minY + positiongetYLength) ? this.m_lowYTeamID : ((byte)(1 - this.m_lowYTeamID));
    }
    
    public byte getPreferedTeamId(final Point3 firstPoint, final Point3 secondPoint) {
        if (this.m_width >= this.m_height) {
            return (byte)((firstPoint.getX() > secondPoint.getX()) ? 1 : 0);
        }
        return (byte)((firstPoint.getY() > secondPoint.getY()) ? 1 : 0);
    }
    
    public Point3 getApproximateBubbleCenter() {
        return new Point3(this.m_width / 2 + this.m_minX, this.m_height / 2 + this.m_minY, (short)0);
    }
    
    public final CellPathData getCellPathData(final int x, final int y, final short z) {
        if (!this.isInMap(x, y)) {
            return null;
        }
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        final int dataIndex = (this.m_cells[cellIndex] & 0x3F) >>> 0;
        final int numMaps = this.m_maps.size();
        int i = 0;
        while (i < numMaps) {
            final TopologyMapInstance mapInstance = this.m_maps.get(i);
            final TopologyMap map = mapInstance.getTopologyMap();
            if (!map.isInMap(x, y)) {
                ++i;
            }
            else {
                final int numZ = map.getPathData(x, y, FightMap.m_sourceCellPathData, 0);
                if (dataIndex >= numZ) {
                    return null;
                }
                if (z != FightMap.m_sourceCellPathData[dataIndex].m_z) {
                    return null;
                }
                return FightMap.m_sourceCellPathData[dataIndex];
            }
        }
        return null;
    }
    
    public final boolean isWalkable(final int x, final int y, final short z) {
        final CellPathData cellPathData = this.getCellPathData(x, y, z);
        return cellPathData != null && cellPathData.m_cost != -1;
    }
    
    public final boolean isBlocked(final int x, final int y) {
        final int cellIndex = this.getCellIndexFromCoords(x, y);
        if (cellIndex < 0 || cellIndex >= this.m_numCells) {
            return false;
        }
        final short cellValue = this.m_cells[cellIndex];
        return (cellValue & 0x300) != 0x0;
    }
    
    public short getDistFromBorders(final int x, final int y) {
        final int distX = (this.m_minX + this.m_width - x > x - this.m_minX) ? (this.m_minX + this.m_width - x) : (x - this.m_minX);
        final int distY = (this.m_minY + this.m_height - x > y - this.m_minY) ? (this.m_minY + this.m_height - y) : (y - this.m_minY);
        return (short)((distX > distY) ? distX : distY);
    }
    
    public Direction8 getTeamStartDirection(final byte teamId) {
        if (this.m_width >= this.m_height) {
            return (teamId == 0) ? Direction8.SOUTH_EAST : Direction8.NORTH_WEST;
        }
        return (teamId == 0) ? Direction8.SOUTH_WEST : Direction8.NORTH_EAST;
    }
    
    public void clear() {
        this.m_numCells = 0;
        this.m_maps.clear();
        this.m_teamPositions = null;
    }
    
    public void removeDynamicMapInstancesFromManager() {
        final boolean isStaticInstance = this.m_instanceId == 0;
        if (!TopologyMapManager.useConstantWorld() && !isStaticInstance) {
            final Rect rect = MapConstants.getPartitionsFromCells(this.m_minX, this.m_minY, this.m_width, this.m_height);
            for (int y = rect.m_yMin; y < rect.m_yMax; ++y) {
                for (int x = rect.m_xMin; x < rect.m_xMax; ++x) {
                    TopologyMapManager.removeTopologyMapInstance(this.m_worldId, (short)x, (short)y, this.m_instanceId);
                }
            }
        }
    }
    
    public int getDataSize() {
        int dataSize = 0;
        dataSize += 4;
        dataSize += 16;
        dataSize += 4;
        dataSize += 2;
        dataSize += this.m_numCells * 2;
        ++dataSize;
        if (this.m_useCustomTeamPosition) {
            dataSize += this.m_teamPositions.length;
        }
        dataSize += 2;
        return dataSize;
    }
    
    public void unserialize(final ByteBuffer buffer) {
        final int position = buffer.position();
        this.m_worldId = buffer.getShort();
        this.m_instanceId = buffer.getShort();
        this.m_minX = buffer.getInt();
        this.m_minY = buffer.getInt();
        this.m_width = buffer.getInt();
        this.m_height = buffer.getInt();
        this.m_minZ = buffer.getShort();
        this.m_maxZ = buffer.getShort();
        this.m_numCells = buffer.getShort();
        assert buffer.remaining() > this.m_numCells * 2;
        this.m_cells = new short[this.m_numCells];
        for (int i = 0; i < this.m_numCells; ++i) {
            this.m_cells[i] = buffer.getShort();
        }
        this.m_useCustomTeamPosition = (buffer.get() == 1);
        if (this.m_useCustomTeamPosition) {
            buffer.get(this.m_teamPositions = new byte[this.m_numCells]);
        }
        final Rect rect = MapConstants.getPartitionsFromCells(this.m_minX, this.m_minY, this.m_width, this.m_height);
        if (TopologyMapManager.useConstantWorld()) {
            for (int y = rect.m_yMin; y < rect.m_yMax; ++y) {
                for (int x = rect.m_xMin; x < rect.m_xMax; ++x) {
                    final TopologyMapInstance map = TopologyMapManager.getMap((short)x, (short)y);
                    if (map != null) {
                        this.m_maps.add(map);
                    }
                }
            }
        }
        else {
            for (int y = rect.m_yMin; y < rect.m_yMax; ++y) {
                for (int x = rect.m_xMin; x < rect.m_xMax; ++x) {
                    try {
                        TopologyMapManager.loadMap(this.m_worldId, (short)x, (short)y);
                        final TopologyMapInstance mapInstance = TopologyMapManager.addTopologyMapInstance(this.m_worldId, (short)x, (short)y, this.m_instanceId);
                        if (mapInstance != null) {
                            this.m_maps.add(mapInstance);
                        }
                    }
                    catch (IOException e) {
                        FightMap.m_logger.error((Object)("Unable to load map (" + (short)x + "; " + (short)y + ')'));
                    }
                }
            }
        }
        this.m_lowXTeamID = buffer.get();
        this.m_lowYTeamID = buffer.get();
        assert buffer.position() - position == this.getDataSize() : "Unserialized data don't have the right size";
    }
    
    public boolean checkPosition(final Target target, final Point3 position) {
        final int posX = position.getX();
        final int posY = position.getY();
        if (!this.isInMap(posX, posY)) {
            return false;
        }
        final byte physicalRadius = target.getPhysicalRadius();
        final short posZ = position.getZ();
        if (physicalRadius == 0) {
            if (target instanceof FightObstacle) {
                this.addIgnoredMovementObstacle((FightObstacle)target);
            }
            final boolean movementBlocked = this.isMovementBlocked(posX, posY, posZ);
            if (target instanceof FightObstacle) {
                this.clearIgnoredMovementObstacles();
            }
            return !movementBlocked;
        }
        if (target instanceof FightObstacle) {
            this.addIgnoredMovementObstacle((FightObstacle)target);
        }
        for (int x = posX - physicalRadius; x <= posX + physicalRadius; ++x) {
            for (int y = posY - physicalRadius; y <= posY + physicalRadius; ++y) {
                if (!this.isInMap(posX, posY) || this.isMovementBlocked(x, y, posZ)) {
                    if (target instanceof FightObstacle) {
                        this.clearIgnoredMovementObstacles();
                    }
                    return false;
                }
            }
        }
        if (target instanceof FightObstacle) {
            this.addIgnoredMovementObstacle((FightObstacle)target);
        }
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("boundingBox={(");
        str.append(this.m_minX).append(", ").append(this.m_minY).append(") => (");
        str.append(this.m_maxX).append(", ").append(this.m_maxY).append(')');
        str.append("}, numCells=");
        str.append(this.m_numCells);
        str.append(", width=").append(this.m_width).append(", height=").append(this.m_height);
        str.append(", center=(").append(this.m_centerX).append(", ").append(this.m_centerY).append(", ").append(this.m_centerZ).append(')');
        return str.toString();
    }
    
    @Override
    protected int getMapIndex(final int x, final int y) {
        for (int numMaps = this.m_maps.size(), i = 0; i < numMaps; ++i) {
            final TopologyMap map = this.m_maps.get(i).getTopologyMap();
            final int minX = map.m_x;
            final int minY = map.m_y;
            if (x >= minX && x < minX + 18 && y >= minY && y < minY + 18) {
                return i;
            }
        }
        return -1;
    }
    
    public void addMapWithoutControl(final TopologyMapInstance map) {
        this.m_maps.add(map);
    }
    
    public Point3 getInsideRandomCell() {
        this.createInsideCellsArrayIfNecessary();
        final int[] coords = this.m_insideCells.get(MathHelper.random(this.m_insideCells.size()));
        return new Point3(coords);
    }
    
    private void createInsideCellsArrayIfNecessary() {
        if (this.m_insideCells != null) {
            return;
        }
        this.m_insideCells = new ArrayList<int[]>();
        for (int i = 0; i < this.m_cells.length; ++i) {
            if (this.isInside(i)) {
                final int x = i % this.m_width + this.m_minX;
                final int y = i / this.m_width + this.m_minY;
                this.m_insideCells.add(new int[] { x, y });
            }
        }
    }
    
    public List<int[]> getInsideCells() {
        this.createInsideCellsArrayIfNecessary();
        return new ArrayList<int[]>(this.m_insideCells);
    }
    
    public void setBorderCellBlocked() {
        for (int i = 0; i < this.m_cells.length; ++i) {
            final short cell = this.m_cells[i];
            if ((cell & 0x80) != 0x0) {
                final short[] cells = this.m_cells;
                final int n = i;
                cells[n] |= 0x200;
            }
        }
    }
    
    static {
        m_debugLogger = Logger.getLogger("debug");
        m_sourceCellPathData = new CellPathData[32];
        m_logger = Logger.getLogger((Class)FightMap.class);
        for (int i = 0; i < FightMap.m_sourceCellPathData.length; ++i) {
            FightMap.m_sourceCellPathData[i] = new CellPathData();
        }
    }
}
