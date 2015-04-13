package com.ankamagames.baseImpl.graphics.alea;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import org.jetbrains.annotations.*;

public class MapManagerHelper
{
    private static final Logger m_logger;
    private static TIntArrayList m_lastCoord;
    private static TIntArrayList m_coordLoaded;
    private static TIntArrayList m_lastLoaded;
    private static int m_lastMapX;
    private static int m_lastMapY;
    private static int m_lastSize;
    private static short m_worldId;
    private static boolean m_useAmbianceZone;
    private static final EnvironmentMapManager m_environmentMapMgr;
    private static final DisplayedScreenWorld m_screenWorld;
    private static final LightningMapManager m_lightningMapManager;
    private static final ArrayList<Runnable> m_onWorldChangeListeners;
    private static MapLoader m_mapLoader;
    
    public static void initializeSceneCamera(final AleaWorldScene scene, final int posX, final int posY, final short posZ) {
        final AleaIsoCamera camera = scene.getIsoCamera();
        final IsoWorldTarget target = camera.getTrackingTarget();
        LoadMapsAroundCellPosition(posX, posY, 2);
        target.setWorldPosition(posX, posY, posZ);
        camera.forceAlign();
        scene.forceSetZoom();
        MapManagerHelper.m_screenWorld.update(camera.getScreenBounds());
    }
    
    public static void LoadMapsAroundCellPosition(final int cellX, final int cellY, final int radius) {
        assert MapManagerHelper.m_worldId != -32768 : "Il faut d'abord appeler setWorldId";
        int mapX = MapConstants.getMapCoordFromCellX(cellX);
        int mapY = MapConstants.getMapCoordFromCellY(cellY);
        final int size = radius * 2 + 1;
        if (mapX == MapManagerHelper.m_lastMapX && mapY == MapManagerHelper.m_lastMapY && MapManagerHelper.m_lastSize >= size) {
            return;
        }
        MapManagerHelper.m_lastMapX = mapX;
        MapManagerHelper.m_lastMapY = mapY;
        MapManagerHelper.m_lastSize = size;
        mapX -= radius;
        mapY -= radius;
        TIntArrayList temp = MapManagerHelper.m_lastCoord;
        MapManagerHelper.m_lastCoord = MapManagerHelper.m_lastLoaded;
        (MapManagerHelper.m_coordLoaded = temp).clear();
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                final short mapCoordX = (short)(mapX + x);
                final short mapCoordY = (short)(mapY + y);
                final int coord = MathHelper.getIntFromTwoShort(mapCoordX, mapCoordY);
                if (MapManagerHelper.m_mapLoader.acceptMap(mapCoordX, mapCoordY)) {
                    MapManagerHelper.m_coordLoaded.add(coord);
                    if (!MapManagerHelper.m_lastCoord.contains(coord)) {
                        MapManagerHelper.m_mapLoader.loadMaps(mapCoordX, mapCoordY);
                    }
                }
            }
        }
        for (int i = 0; i < MapManagerHelper.m_lastCoord.size(); ++i) {
            final int mapIndex = MapManagerHelper.m_lastCoord.getQuick(i);
            if (!MapManagerHelper.m_coordLoaded.contains(mapIndex)) {
                final short x2 = MathHelper.getFirstShortFromInt(mapIndex);
                final short y2 = MathHelper.getSecondShortFromInt(mapIndex);
                unloadPartition(x2, y2);
            }
        }
        temp = MapManagerHelper.m_lastLoaded;
        MapManagerHelper.m_lastLoaded = MapManagerHelper.m_coordLoaded;
        MapManagerHelper.m_coordLoaded = temp;
    }
    
    private static void unloadPartition(final short x, final short y) {
        MapManagerHelper.m_environmentMapMgr.unload(x, y);
        MapManagerHelper.m_lightningMapManager.removeMap(x, y);
        TopologyMapManager.unloadMap(x, y);
    }
    
    public static void forceUpdate(final short x, final short y) {
        final int key = MathHelper.getIntFromTwoShort(x, y);
        TroveUtils.removeFirstValue(MapManagerHelper.m_lastLoaded, key);
        unloadPartition(x, y);
        MapManagerHelper.m_lastMapX = Integer.MIN_VALUE;
        MapManagerHelper.m_lastMapY = Integer.MIN_VALUE;
        MapManagerHelper.m_lastSize = Integer.MIN_VALUE;
    }
    
    public static void setWorldId(final short worldId) {
        setWorldId(worldId, DefaultMapLoader.getInstance());
    }
    
    public static void setWorldId(final short worldId, final MapLoader mapLoader) {
        if (MapManagerHelper.m_worldId == worldId) {
            return;
        }
        clear();
        (MapManagerHelper.m_mapLoader = mapLoader).initialize(MapManagerHelper.m_environmentMapMgr, MapManagerHelper.m_screenWorld, MapManagerHelper.m_lightningMapManager);
        MapManagerHelper.m_worldId = worldId;
        for (int i = 0, size = MapManagerHelper.m_onWorldChangeListeners.size(); i < size; ++i) {
            MapManagerHelper.m_onWorldChangeListeners.get(i).run();
        }
        MapManagerHelper.m_onWorldChangeListeners.clear();
        MapManagerHelper.m_mapLoader.prepare(worldId);
        TopologyMapManager.setWorldId(worldId);
        MapManagerHelper.m_environmentMapMgr.setWorldId(worldId);
        MapManagerHelper.m_screenWorld.setWorldInstanceId(worldId);
        MapManagerHelper.m_lightningMapManager.setWorldId(worldId);
        if (MapManagerHelper.m_useAmbianceZone) {
            MapManagerHelper.m_mapLoader.prepareAmbianceZone(worldId);
        }
    }
    
    public static short getWorldId() {
        return MapManagerHelper.m_worldId;
    }
    
    public static void clear() {
        MapManagerHelper.m_lastMapX = Integer.MIN_VALUE;
        MapManagerHelper.m_lastMapY = Integer.MIN_VALUE;
        MapManagerHelper.m_lastSize = Integer.MIN_VALUE;
        MapManagerHelper.m_worldId = -32768;
        MapManagerHelper.m_lastCoord.clear();
        MapManagerHelper.m_coordLoaded.clear();
        MapManagerHelper.m_lastLoaded.clear();
        MapManagerHelper.m_environmentMapMgr.unloadDynamicDataFromAllMaps();
        MapManagerHelper.m_environmentMapMgr.reset();
        MapManagerHelper.m_lightningMapManager.clean();
        TopologyMapManager.reset();
        MapManagerHelper.m_environmentMapMgr.reset();
        MapManagerHelper.m_lightningMapManager.clean();
        MapManagerHelper.m_screenWorld.clear();
        MapManagerHelper.m_mapLoader.clear();
    }
    
    public static PartitionExistValidator loadValidGfxMapsCoordinates(final short worldId) {
        return DefaultMapLoader.getInstance().loadValidGfxMapsCoordinates(worldId);
    }
    
    public static void setValidMapsCoordFile(@NotNull final String validTplgMapsCoordFile, @NotNull final String validGfxMapsCoordFile) {
        DefaultMapLoader.getInstance().setValidMapsCoordFile(validTplgMapsCoordFile, validGfxMapsCoordFile);
    }
    
    public static boolean isValidTopology(final short mapX, final short mapY) {
        return MapManagerHelper.m_mapLoader.acceptMap(mapX, mapY);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapManagerHelper.class);
        MapManagerHelper.m_lastCoord = new TIntArrayList(25);
        MapManagerHelper.m_coordLoaded = new TIntArrayList(25);
        MapManagerHelper.m_lastLoaded = new TIntArrayList(25);
        MapManagerHelper.m_lastMapX = Integer.MIN_VALUE;
        MapManagerHelper.m_lastMapY = Integer.MIN_VALUE;
        MapManagerHelper.m_lastSize = Integer.MIN_VALUE;
        MapManagerHelper.m_worldId = -32768;
        MapManagerHelper.m_useAmbianceZone = true;
        m_environmentMapMgr = EnvironmentMapManager.getInstance();
        m_screenWorld = DisplayedScreenWorld.getInstance();
        m_lightningMapManager = LightningMapManager.getInstance();
        m_onWorldChangeListeners = new ArrayList<Runnable>();
        DefaultMapLoader.getInstance().initialize(MapManagerHelper.m_environmentMapMgr, MapManagerHelper.m_screenWorld, MapManagerHelper.m_lightningMapManager);
        MapManagerHelper.m_mapLoader = DefaultMapLoader.getInstance();
    }
}
