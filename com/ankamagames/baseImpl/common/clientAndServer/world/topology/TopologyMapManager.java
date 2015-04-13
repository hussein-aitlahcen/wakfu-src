package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;

public class TopologyMapManager
{
    private static boolean m_useConstantWorld;
    private static boolean m_limitToThreadSafeMethods;
    private static int m_worldId;
    private static String m_pathURL;
    private static final CellPathData[] m_cellPathData;
    private static final PathChecker m_pathChecker;
    private static final Logger m_logger;
    private static final TLongObjectHashMap<TopologyMapInstance> m_mapInstances;
    private static final TLongObjectHashMap<TopologyMap> m_topologyMaps;
    private static final Object m_mapsSynchronizer;
    private static AsyncMapLoader m_asyncLoader;
    
    public static void loadMapAsync(final short mapCoordX, final short mapCoordY) {
        assert TopologyMapManager.m_asyncLoader != null : " Appel\u00e9 enableAsyncLoading lors de l'initialisation de l'appli";
        final long key = getHashCode(TopologyMapManager.m_worldId, mapCoordX, mapCoordY, 0);
        TopologyMapManager.m_asyncLoader.submit(key, new Runnable() {
            @Override
            public void run() {
                try {
                    TopologyMapManager.loadMap(mapCoordX, mapCoordY);
                }
                catch (IOException e) {
                    TopologyMapManager.m_logger.error((Object)"", (Throwable)e);
                }
            }
        });
    }
    
    public static void enableConstantWorld() {
        TopologyMapManager.m_useConstantWorld = true;
    }
    
    public static void enableAsyncLoading() {
        assert TopologyMapManager.m_useConstantWorld;
        TopologyMapManager.m_asyncLoader = new AsyncMapLoader("topology loader");
    }
    
    public static void setLimitToThreadSafeMethods(final boolean limitToThreadSafeMethods) {
        TopologyMapManager.m_limitToThreadSafeMethods = limitToThreadSafeMethods;
    }
    
    public static boolean useConstantWorld() {
        return TopologyMapManager.m_useConstantWorld;
    }
    
    public static void setPath(final String path) {
        try {
            TopologyMapManager.m_pathURL = ContentFileHelper.getURL(path).toString();
            if (!TopologyMapManager.m_pathURL.endsWith("/")) {
                TopologyMapManager.m_pathURL += "/";
            }
        }
        catch (IOException e) {
            TopologyMapManager.m_logger.error((Object)("Invalid path : " + path), (Throwable)e);
        }
    }
    
    public static void setWorldId(final int worldId) {
        assert TopologyMapManager.m_useConstantWorld : "Can't set worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        TopologyMapManager.m_worldId = worldId;
    }
    
    public static int getWorldId() {
        assert TopologyMapManager.m_useConstantWorld : "Can't get worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return TopologyMapManager.m_worldId;
    }
    
    private static String createMapPath(final String path, final int worldId, final short x, final short y) {
        assert path != null && path.contains("%d") && path.endsWith("/");
        return String.format(TopologyMapManager.m_pathURL, worldId) + x + '_' + y;
    }
    
    public static void loadMap(final int worldId, final short x, final short y) throws IOException {
        final long hashCode = getHashCode(worldId, x, y, 0);
        synchronized (TopologyMapManager.m_mapsSynchronizer) {
            if (TopologyMapManager.m_topologyMaps.contains(hashCode)) {
                return;
            }
            final String fileName = createMapPath(TopologyMapManager.m_pathURL, worldId, x, y);
            final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(WorldMapFileHelper.readFile(fileName));
            final byte header = bitStream.readByte();
            final TopologyMap topologyMap = TopologyMapFactory.createTopologyMap(header);
            if (topologyMap == null) {
                TopologyMapManager.m_logger.error((Object)("Unable to create map (" + x + "; " + y + ";" + worldId + ")"));
                return;
            }
            topologyMap.load(bitStream);
            assert MapConstants.getMapCoordFromCellX(topologyMap.m_x) == x && MapConstants.getMapCoordFromCellY(topologyMap.m_y) == y;
            TopologyMapManager.m_topologyMaps.put(hashCode, topologyMap);
        }
    }
    
    public static void loadMap(final short x, final short y) throws IOException {
        assert TopologyMapManager.m_useConstantWorld : "Can't loadMap without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        TopologyMapManager.m_logger.debug((Object)("START loadMap " + TopologyMapManager.m_worldId));
        loadMap(TopologyMapManager.m_worldId, x, y);
        TopologyMapManager.m_logger.debug((Object)("START addTopologyMapInstance " + TopologyMapManager.m_worldId));
        addTopologyMapInstance(TopologyMapManager.m_worldId, x, y, (short)0);
        TopologyMapManager.m_logger.debug((Object)("END loadMap " + TopologyMapManager.m_worldId));
    }
    
    public static void unloadMap(final short x, final short y) {
        assert TopologyMapManager.m_useConstantWorld : "Can't loadMap without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        final long hashCode = getHashCode(TopologyMapManager.m_worldId, x, y, 0);
        if (TopologyMapManager.m_asyncLoader != null) {
            TopologyMapManager.m_asyncLoader.remove(hashCode);
        }
        synchronized (TopologyMapManager.m_mapsSynchronizer) {
            TopologyMapManager.m_topologyMaps.remove(hashCode);
            TopologyMapManager.m_mapInstances.remove(hashCode);
        }
    }
    
    public static TopologyMapInstance getMap(final int worldId, final short x, final short y, final short instanceUID) {
        final long hashCode = getHashCode(worldId, x, y, instanceUID);
        if (TopologyMapManager.m_asyncLoader != null) {
            TopologyMapManager.m_asyncLoader.waitFor(hashCode);
        }
        synchronized (TopologyMapManager.m_mapsSynchronizer) {
            return TopologyMapManager.m_mapInstances.get(hashCode);
        }
    }
    
    public static TopologyMapInstance getMap(final short x, final short y) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getMap without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return getMap(TopologyMapManager.m_worldId, x, y, (short)0);
    }
    
    public static TopologyMapInstance getMapFromCell(final int worldId, final int cellX, final int cellY, final short instanceUID) {
        final short x = (short)MapConstants.getMapCoordFromCellX(cellX);
        final short y = (short)MapConstants.getMapCoordFromCellY(cellY);
        return getMap(worldId, x, y, instanceUID);
    }
    
    public static TopologyMapInstance getMapFromCell(final int cellX, final int cellY) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getMapFromCell without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return getMapFromCell(TopologyMapManager.m_worldId, cellX, cellY, (short)0);
    }
    
    public static void reset() {
        if (TopologyMapManager.m_asyncLoader != null) {
            TopologyMapManager.m_asyncLoader.clear();
        }
        synchronized (TopologyMapManager.m_mapsSynchronizer) {
            TopologyMapManager.m_topologyMaps.clear();
            TopologyMapManager.m_mapInstances.clear();
        }
    }
    
    public static TopologyMapInstance addTopologyMapInstance(final int worldId, final short x, final short y, final short instanceUID) {
        final long hashCode = getHashCode(worldId, x, y, instanceUID);
        synchronized (TopologyMapManager.m_mapsSynchronizer) {
            TopologyMapInstance instance = TopologyMapManager.m_mapInstances.get(hashCode);
            if (instance != null) {
                return instance;
            }
            final long topologyMapHashCode = hashCode & 0xFFFFFFFFFFFF0000L;
            final TopologyMap topologyMap = TopologyMapManager.m_topologyMaps.get(topologyMapHashCode);
            assert topologyMap != null : "The cell (" + x + "; " + y + ") in world " + worldId + " belongs to a map not loaded";
            instance = new TopologyMapInstance(topologyMap);
            TopologyMapManager.m_mapInstances.put(hashCode, instance);
            return instance;
        }
    }
    
    public static void removeTopologyMapInstance(final int worldId, final int x, final int y, final short instanceUID) {
        final long hashCode = getHashCode(worldId, x, y, instanceUID);
        synchronized (TopologyMapManager.m_mapsSynchronizer) {
            TopologyMapManager.m_mapInstances.remove(hashCode);
        }
    }
    
    public static void getTopologyMapInstances(final int worldId, final short instanceUID, int srcX, int srcY, int destX, int destY, final int radius, final TopologyMapInstanceSet mapSet) {
        mapSet.reset();
        if (srcX > destX) {
            final int tmp = srcX;
            srcX = destX;
            destX = tmp;
        }
        if (srcY > destY) {
            final int tmp = srcY;
            srcY = destY;
            destY = tmp;
        }
        srcX -= radius;
        srcY -= radius;
        destX += radius;
        destY += radius;
        final int mapX = MapConstants.getMapCoordFromCellX(srcX);
        final int mapY = MapConstants.getMapCoordFromCellY(srcY);
        final int lastMapX = MapConstants.getMapCoordFromCellX(destX);
        for (int lastMapY = MapConstants.getMapCoordFromCellY(destY), y = mapY; y <= lastMapY; ++y) {
            for (int x = mapX; x <= lastMapX; ++x) {
                final TopologyMapInstance map = getMap(worldId, (short)x, (short)y, instanceUID);
                mapSet.addMap(map, x, y);
            }
        }
    }
    
    public static void getTopologyMapInstances(final int worldId, final short instanceUID, final int x, final int y, final int radius, final TopologyMapInstanceSet mapSet) {
        getTopologyMapInstances(worldId, instanceUID, x, y, x, y, radius, mapSet);
    }
    
    public static void getTopologyMapInstances(final int srcX, final int srcY, final int destX, final int destY, final TopologyMapInstanceSet mapSet) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getTopologyMapInstances without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        getTopologyMapInstances(TopologyMapManager.m_worldId, (short)0, srcX, srcY, destX, destY, 9, mapSet);
    }
    
    public static void setMoverCaracteristics(final short moverHeight, final byte moverPhysicalRadius, final short moverJumpCapacity) {
        TopologyMapManager.m_pathChecker.setMoverCaracteristics(moverHeight, moverPhysicalRadius, moverJumpCapacity);
    }
    
    public static boolean isGap(final int cellX, final int cellY) {
        final TopologyMapInstance mapInstance = getMapFromCell(cellX, cellY);
        if (mapInstance == null) {
            return false;
        }
        try {
            for (int num = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0), i = 0; i < num; ++i) {
                final CellPathData cellPathData = TopologyMapManager.m_cellPathData[i];
                if (cellPathData != null && cellPathData.isGap()) {
                    return true;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            TopologyMapManager.m_logger.error((Object)"", (Throwable)e);
        }
        return false;
    }
    
    public static boolean isJump(final int cellX, final int cellY) {
        final TopologyMapInstance mapInstance = getMapFromCell(cellX, cellY);
        if (mapInstance == null) {
            return false;
        }
        try {
            for (int num = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0), i = 0; i < num; ++i) {
                final CellPathData cellPathData = TopologyMapManager.m_cellPathData[i];
                if (cellPathData != null && cellPathData.isJump()) {
                    return true;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            TopologyMapManager.m_logger.error((Object)"", (Throwable)e);
        }
        return false;
    }
    
    public static short getPossibleNearestWalkableZ(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getTopologyMapInstances without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call getPossibleNearestWalkableZ in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(cellX, cellY);
        if (mapInstance == null || mapInstance.isCellBlocked(cellX, cellY)) {
            return -32768;
        }
        try {
            final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
            markBlocked(mapInstance, 0, numZ);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            TopologyMapManager.m_logger.error((Object)"", (Throwable)e);
        }
        final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
        markBlocked(mapInstance, 0, numZ);
        return TopologyChecker.getHighestWalkableZ(0, numZ, TopologyMapManager.m_cellPathData, (short)(z + TopologyMapManager.m_pathChecker.getMoverJumpCapacity()), TopologyMapManager.m_pathChecker.getMoverHeight());
    }
    
    public static short getHighestWalkableZ(final int cellX, final int cellY) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getTopologyMapInstances without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return getHighestWalkableZ(TopologyMapManager.m_worldId, cellX, cellY, (short)0);
    }
    
    public static short getHighestWalkableZ(final int worldId, final int cellX, final int cellY, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call getHighestWalkableZ in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        if (mapInstance == null) {
            TopologyMapManager.m_logger.error((Object)("The cell (" + cellX + "; " + cellY + ", instance " + worldId + ") belongs to a map not loaded"));
            return -32768;
        }
        if (mapInstance.isCellBlocked(cellX, cellY)) {
            return -32768;
        }
        try {
            final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
            markBlocked(mapInstance, 0, numZ);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            TopologyMapManager.m_logger.error((Object)"", (Throwable)e);
        }
        final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
        markBlocked(mapInstance, 0, numZ);
        return TopologyChecker.getHighestWalkableZ(0, numZ, TopologyMapManager.m_cellPathData, (short)32767, TopologyMapManager.m_pathChecker.getMoverHeight());
    }
    
    public static short getNearestWalkableZ(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getTopologyMapInstances without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return getNearestWalkableZ(TopologyMapManager.m_worldId, cellX, cellY, z, (short)0);
    }
    
    public static boolean isStoppableUpon(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getTopologyMapInstances without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return !isGap(cellX, cellY);
    }
    
    public static short getNearestWalkableZ(final int worldId, final int cellX, final int cellY, final short z, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call getNearestWalkableZ in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        if (mapInstance == null) {
            TopologyMapManager.m_logger.error((Object)("MapInstance is null for parameters : worldId=" + worldId + ", pos=[" + cellX + ";" + cellY + "], InstanceUid=" + instanceUniqueId));
            return -32768;
        }
        if (mapInstance.isCellBlocked(cellX, cellY)) {
            return -32768;
        }
        final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
        markBlocked(mapInstance, 0, numZ);
        short delta = 32767;
        short nearestZ = -32768;
        for (int i = 0; i < numZ; ++i) {
            final CellPathData data = TopologyMapManager.m_cellPathData[i];
            if (data.m_cost != -1) {
                if (data.m_z != -32768) {
                    if (!data.m_hollow) {
                        final int dz = Math.abs(data.m_z - z);
                        if (dz < delta) {
                            delta = (short)dz;
                            nearestZ = data.m_z;
                        }
                    }
                }
            }
        }
        return nearestZ;
    }
    
    @Nullable
    public static Point3 getNearestWalkableCell(final int worldId, final short instanceUniqueId, final Point3 pos, final int radius) {
        return getNearestWalkableCell(worldId, instanceUniqueId, pos.getX(), pos.getY(), pos.getZ(), radius);
    }
    
    @Nullable
    public static Point3 getNearestWalkableCell(final int worldId, final short instanceUniqueId, final int startX, final int startY, final short startZ, final int radius) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call getNearestWalkableZ in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, startX, startY, instanceUniqueId);
        if (mapInstance == null) {
            return null;
        }
        for (int i = 1; i <= radius; ++i) {
            for (int k = 0; k < i; ++k) {
                final int x = startX + i - k;
                final int y = startY - k;
                final short z = getNearestWalkableZ(worldId, x, y, startZ, instanceUniqueId);
                if (z != -32768) {
                    return new Point3(x, y, z);
                }
            }
            for (int k = 0; k < i; ++k) {
                final int x = startX - k;
                final int y = startY - i + k;
                final short z = getNearestWalkableZ(worldId, x, y, startZ, instanceUniqueId);
                if (z != -32768) {
                    return new Point3(x, y, z);
                }
            }
            for (int k = 0; k < i; ++k) {
                final int x = startX - i + k;
                final int y = startY + k;
                final short z = getNearestWalkableZ(worldId, x, y, startZ, instanceUniqueId);
                if (z != -32768) {
                    return new Point3(x, y, z);
                }
            }
            for (int k = 0; k < i; ++k) {
                final int x = startX + k;
                final int y = startY + i - k;
                final short z = getNearestWalkableZ(worldId, x, y, startZ, instanceUniqueId);
                if (z != -32768) {
                    return new Point3(x, y, z);
                }
            }
        }
        return null;
    }
    
    public static short getNearestZ(final int cellX, final int cellY, final short cellZ) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getTopologyMapInstances without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return getNearestZ(TopologyMapManager.m_worldId, cellX, cellY, cellZ, (short)0);
    }
    
    public static short getNearestZ(final int worldId, final int cellX, final int cellY, final short z, final short instanceUID) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call getNearestZ in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUID);
        if (mapInstance == null) {
            return -32768;
        }
        final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
        short delta = 32767;
        short nearestZ = z;
        for (int i = 0; i < numZ; ++i) {
            final int dz = Math.abs(TopologyMapManager.m_cellPathData[i].m_z - z);
            if (dz < delta) {
                delta = (short)dz;
                nearestZ = TopologyMapManager.m_cellPathData[i].m_z;
            }
        }
        return nearestZ;
    }
    
    public static boolean isIndoor(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't isIndoor without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return isIndoor(TopologyMapManager.m_worldId, cellX, cellY, z, (short)0);
    }
    
    public static boolean isIndoor(final int worldId, final int cellX, final int cellY, final short z, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call isIndoor in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        assert mapInstance != null : "The cell (" + cellX + "; " + cellY + ") belongs to a map not loaded";
        return mapInstance.isIndoor(cellX, cellY, z);
    }
    
    public static boolean isMoboSterileOrNotWalkable(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't isWalkable without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return isMoboSterileOrNotWalkable(TopologyMapManager.m_worldId, cellX, cellY, z, (short)0);
    }
    
    public static boolean isMoboSterileOrNotWalkable(final int worldId, final int cellX, final int cellY, final short z, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call isMoboSterile in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        assert mapInstance != null : "The cell (" + cellX + "; " + cellY + ") belongs to a map not loaded";
        final CellPathData result = getWalkableCellPathData(mapInstance, cellX, cellY, z);
        return result == null || result.isMoboSteryl();
    }
    
    public static boolean isFightoSterileOrNotWalkable(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't isFightoSterile without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return isFightoSterileOrNotWalkable(TopologyMapManager.m_worldId, cellX, cellY, z, (short)0);
    }
    
    public static boolean isFightoSterileOrNotWalkable(final int worldId, final int cellX, final int cellY, final short z, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call isFightoSterile in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        if (mapInstance == null) {
            TopologyMapManager.m_logger.warn((Object)("On essaye d'acc\u00e9der \u00e0 une cellule dans une map pas charg\u00e9e : (" + cellX + ", " + cellY + ")@" + worldId));
            return false;
        }
        final CellPathData result = getWalkableCellPathData(mapInstance, cellX, cellY, z);
        return result == null || result.isFightoSteryl();
    }
    
    public static boolean isIESterileOrNotWalkable(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't isFightoSterile without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return isIESterileOrNotWalkable(TopologyMapManager.m_worldId, cellX, cellY, z, (short)0);
    }
    
    public static boolean isIESterileOrNotWalkable(final int worldId, final int cellX, final int cellY, final short z, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call isIESterile in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        if (mapInstance == null) {
            TopologyMapManager.m_logger.warn((Object)("On essaye d'acc\u00e9der \u00e0 une cellule dans une map pas charg\u00e9e : (" + cellX + ", " + cellY + ")@" + worldId));
            return false;
        }
        final CellPathData result = getWalkableCellPathData(mapInstance, cellX, cellY, z);
        return result == null || result.isIESteryl();
    }
    
    public static boolean isWalkable(final int cellX, final int cellY, final short z) {
        assert TopologyMapManager.m_useConstantWorld : "Can't isWalkable without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return isWalkable(TopologyMapManager.m_worldId, cellX, cellY, z, (short)0);
    }
    
    public static boolean isWalkable(final int worldId, final int cellX, final int cellY, final short z, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call isWalkable in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        assert mapInstance != null : "The cell (" + cellX + "; " + cellY + ") belongs to a map not loaded";
        return !mapInstance.isBlocked(cellX, cellY, z) && getWalkableCellPathData(mapInstance, cellX, cellY, z) != null;
    }
    
    public static short[] getWalkableZ(final int cellX, final int cellY) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getWalkableZ without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return getWalkableZ(TopologyMapManager.m_worldId, cellX, cellY, (short)0);
    }
    
    public static short[] getWalkableZ(final int worldId, final int cellX, final int cellY, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call getNearestZ in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        if (mapInstance == null || mapInstance.isCellBlocked(cellX, cellY)) {
            return PrimitiveArrays.EMPTY_SHORT_ARRAY;
        }
        final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
        final TShortArrayList zs = new TShortArrayList();
        for (int i = 0; i < numZ; ++i) {
            if (TopologyMapManager.m_cellPathData[i].m_cost != -1) {
                if (!TopologyMapManager.m_cellPathData[i].m_hollow) {
                    if (!mapInstance.isBlocked(cellX, cellY, TopologyMapManager.m_cellPathData[i].m_z)) {
                        zs.add(TopologyMapManager.m_cellPathData[i].m_z);
                    }
                }
            }
        }
        zs.reverse();
        return zs.toNativeArray();
    }
    
    public static short[] getZ(final int cellX, final int cellY) {
        assert TopologyMapManager.m_useConstantWorld : "Can't getWalkableZ without giving worldId if not using constant world. See TopologyMapManager.enableConstantWorld for more informations";
        return getZ(TopologyMapManager.m_worldId, cellX, cellY, (short)0);
    }
    
    public static short[] getZ(final int worldId, final int cellX, final int cellY, final short instanceUniqueId) {
        assert !TopologyMapManager.m_limitToThreadSafeMethods : "Can't call getNearestZ in a 'thread safe aware' environnement. See TopologyMapManager.setLimitToThreadSafeMethods for more informations";
        final TopologyMapInstance mapInstance = getMapFromCell(worldId, cellX, cellY, instanceUniqueId);
        if (mapInstance == null) {
            return PrimitiveArrays.EMPTY_SHORT_ARRAY;
        }
        final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0);
        final short[] result = new short[numZ];
        for (int i = 0; i < numZ; ++i) {
            result[i] = TopologyMapManager.m_cellPathData[i].m_z;
        }
        return result;
    }
    
    public static boolean areCellsContiguous(final int cellX, final int cellY, final int cell2X, final int cell2Y, final boolean heightDirection) {
        final int diffX = Math.abs(cell2X - cellX);
        if (diffX > 1) {
            return false;
        }
        final int diffY = Math.abs(cell2Y - cellY);
        return diffY <= 1 && (diffX != diffY || (heightDirection && diffX != 0));
    }
    
    public static void insertTopologyMapPatch(final int worldId, final short x, final short y, final short instanceUID, final TopologyMap topologyMap) {
        final long hashCode = getHashCode(worldId, x, y, instanceUID);
        synchronized (TopologyMapManager.m_mapsSynchronizer) {
            TopologyMapManager.m_topologyMaps.put(hashCode, topologyMap);
            TopologyMapManager.m_mapInstances.put(hashCode, new TopologyMapInstance(topologyMap));
        }
    }
    
    private static long getHashCode(final int worldId, long x, long y, final int uniqueInstanceId) {
        x += 32767L;
        y += 32767L;
        return x << 48 | y << 32 | (worldId & 0xFFFF) << 16 | (uniqueInstanceId & 0xFFFF);
    }
    
    private static CellPathData getWalkableCellPathData(final TopologyMapInstance mapInstance, final int cellX, final int cellY, final short cellZ) {
        if (mapInstance.isBlocked(cellX, cellY, cellZ)) {
            return null;
        }
        for (int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, TopologyMapManager.m_cellPathData, 0), i = 0; i < numZ; ++i) {
            if (TopologyMapManager.m_cellPathData[i].m_z == cellZ) {
                return (TopologyMapManager.m_cellPathData[i].m_cost == -1) ? null : TopologyMapManager.m_cellPathData[i];
            }
        }
        return null;
    }
    
    private static void markBlocked(final TopologyMapInstance mapInstance, final int startIndex, final int numZ) {
        for (int i = 0; i < numZ; ++i) {
            final CellPathData data = TopologyMapManager.m_cellPathData[startIndex + i];
            if (mapInstance.isBlocked(data.m_x, data.m_y, data.m_z)) {
                data.m_cost = -1;
            }
        }
    }
    
    public static void main(final String[] args) {
        try {
            final int worldId = 332;
            TopologyMapManager.m_pathURL = "jar:file:/F:/Work/Code/Wakfu_export_data/data/wakfu/client/contents/maps/tplg/%d.jar!/";
            loadMap(332, (short)0, (short)0);
            final TopologyMapInstance map = addTopologyMapInstance(332, (short)0, (short)0, (short)0);
            System.out.println("");
        }
        catch (IOException e) {
            TopologyMapManager.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    static {
        TopologyMapManager.m_useConstantWorld = false;
        TopologyMapManager.m_limitToThreadSafeMethods = false;
        m_cellPathData = CellPathData.createCellPathDataTab();
        m_pathChecker = new PathChecker(-1, (byte)0, -1);
        m_logger = Logger.getLogger((Class)TopologyMapManager.class);
        m_mapInstances = new TLongObjectHashMap<TopologyMapInstance>();
        m_topologyMaps = new TLongObjectHashMap<TopologyMap>();
        m_mapsSynchronizer = new Object();
    }
}
