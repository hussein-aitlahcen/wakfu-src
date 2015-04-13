package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;

public class DisplayedScreenWorld implements LitScene
{
    private static final int CACHE_SIZE = 25;
    private static final int NOT_READY = 0;
    private static final int READY = 1;
    private static final int DISPLAYED = 2;
    private static final int READY_AND_DISPLAYED = 3;
    public static final int MIN_LOD_LEVEL = 0;
    public static final int AVG_LOD_LEVEL = 1;
    public static final int MAX_LOD_LEVEL = 2;
    private final ArrayList<DisplayedScreenElement> m_visibleEntities;
    private ScreenWorld m_world;
    private ArrayList<DisplayedScreenMap> m_maps;
    private String m_mapPath;
    private String m_basePath;
    private PartitionExistValidator m_mapsCoordinates;
    private int m_instanceId;
    private byte m_readyState;
    private static byte m_visibilityMask;
    private static final DisplayedScreenWorld m_instance;
    private static final Logger m_logger;
    private final DisplayedScreenElementFactory m_factory;
    private final ArrayList<ReloadListener> m_reloadListener;
    private final ArrayList<MapLoadListener> m_mapLoadListeners;
    private final DisplayedWorldHelper m_cache;
    private final THashSet<DisplayedScreenElement> _animatedElementProcessed;
    private final Sea m_sea;
    private final ArrayList<DisplayedScreenElement> m_externalElements;
    private int m_lodLevel;
    private boolean _checkErrorOnce;
    
    private DisplayedScreenWorld() {
        super();
        this.m_visibleEntities = new ArrayList<DisplayedScreenElement>(2048);
        this.m_instanceId = Integer.MIN_VALUE;
        this.m_factory = new DisplayedScreenElementFactory();
        this.m_reloadListener = new ArrayList<ReloadListener>();
        this.m_mapLoadListeners = new ArrayList<MapLoadListener>();
        this._animatedElementProcessed = new THashSet<DisplayedScreenElement>();
        this.m_sea = new Sea();
        this.m_externalElements = new ArrayList<DisplayedScreenElement>();
        this._checkErrorOnce = false;
        this.m_maps = new ArrayList<DisplayedScreenMap>(25);
        this.m_cache = new DisplayedWorldHelper(this.m_factory);
        this.m_world = new ScreenWorld();
        this.clear();
    }
    
    public static DisplayedScreenWorld createScreenWorld() {
        return new DisplayedScreenWorld();
    }
    
    public static DisplayedScreenWorld getInstance() {
        return DisplayedScreenWorld.m_instance;
    }
    
    public Iterator<DisplayedScreenMap> getMapsIterator() {
        return this.m_maps.iterator();
    }
    
    public void setWorld(@NotNull final ScreenWorld world, final PartitionExistValidator mapsCoordinates) {
        this.m_world.clear();
        this.m_world = world;
        this.setMapsCoordinates(mapsCoordinates);
    }
    
    public void clear() {
        this.m_externalElements.clear();
        this.m_mapLoadListeners.clear();
        this.m_reloadListener.clear();
        this.m_readyState = 0;
        this.m_maps.clear();
        this.m_world.clear();
        this.m_cache.clear();
        this._animatedElementProcessed.clear();
        this.m_factory.clear();
    }
    
    public void rebuildCache() {
        this.m_cache.clear();
        this.setMaps();
    }
    
    private static String createMapPath(final String basepath, final int worldId) {
        if (basepath == null) {
            return null;
        }
        assert basepath.contains("%d") && basepath.endsWith("/");
        return String.format(basepath, worldId);
    }
    
    public void setWorldInstanceId(final int instanceId) {
        if (this.m_instanceId == instanceId) {
            return;
        }
        this.m_instanceId = instanceId;
        if (this.m_basePath != null) {
            this.m_mapPath = createMapPath(this.m_basePath, this.m_instanceId);
        }
        this.m_readyState &= 0xFFFFFFFD;
    }
    
    public void setPath(final String path) {
        this.m_basePath = path;
        this.m_mapPath = createMapPath(this.m_basePath, this.m_instanceId);
        this.m_readyState &= 0xFFFFFFFD;
    }
    
    public void update(final AleaWorldScene aleaWorldScene, final int deltaTime) {
        this._animatedElementProcessed.clear();
        final AbstractCamera camera = aleaWorldScene.getCam();
        for (int i = 0, numMap = this.m_maps.size(); i < numMap; ++i) {
            final DisplayedScreenMap map = this.m_maps.get(i);
            final ArrayList<DisplayedScreenElement> animatedElements = map.getAnimatedElements();
            final int size = animatedElements.size();
            if (size != 0) {
                if (map.isVisible(camera)) {
                    for (int e = 0; e < size; ++e) {
                        final DisplayedScreenElement element = animatedElements.get(e);
                        if (element.m_element.isAnimated()) {
                            if (element.m_sprite != null) {
                                if (!this._animatedElementProcessed.contains(element)) {
                                    element.updateTextureCoord((short)deltaTime);
                                    this._animatedElementProcessed.add(element);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public final void update(final Rect rect) {
        this.update(rect.m_yMax, rect.m_xMin, rect.m_yMin, rect.m_xMax);
    }
    
    private void update(final int top, final int left, final int bottom, final int right) {
        assert this.m_mapPath != null : "You must call setPath before";
        this.m_world.loadMaps(this.m_mapPath, top, left, bottom, right, this.m_mapsCoordinates);
        final boolean changed = this.m_world.boundChanged();
        if (changed && this.setMaps()) {
            this.fireMapLoaded();
        }
        this.m_cache.compact(this.m_world.getCenterX(), this.m_world.getCenterY(), 25, this.m_maps);
        final int mapCount = this.m_maps.size();
        if (mapCount != 0) {
            if (!changed && this.isReady()) {
                return;
            }
            this._checkErrorOnce = false;
            boolean isReady = true;
            for (int i = 0; i < mapCount; ++i) {
                final DisplayedScreenMap screenMap = this.m_maps.get(i);
                if (screenMap != null) {
                    screenMap.update();
                    final boolean ready = screenMap.isReady();
                    isReady &= ready;
                }
            }
            if (isReady) {
                this.m_readyState |= 0x1;
            }
            else {
                this.m_readyState &= 0xFFFFFFFE;
            }
        }
        else {
            this.m_readyState |= 0x1;
            if (!this._checkErrorOnce) {
                this._checkErrorOnce = true;
                DisplayedScreenWorld.m_logger.error((Object)("pas de maps \u00e0 charger (screen map=" + this.m_world.getCenterX() + ", " + this.m_world.getCenterY() + ") @" + this.m_instanceId));
            }
        }
    }
    
    private boolean setMaps() {
        final IntObjectLightWeightMap<ScreenMap> screenMaps = this.m_world.m_maps;
        final short newMapCount = (short)screenMaps.size();
        boolean loaded = false;
        this.m_maps.clear();
        for (int i = 0; i < newMapCount; ++i) {
            final int key = screenMaps.getQuickKey(i);
            final ScreenMap map = screenMaps.getQuickValue(i);
            DisplayedScreenMap displayedMap = this.m_cache.getMap(key);
            if (displayedMap == null) {
                loaded = true;
                displayedMap = new DisplayedScreenMap();
                displayedMap.setMap(map, this.m_factory, false);
                this.m_cache.put(key, displayedMap);
            }
            this.m_maps.add(displayedMap);
        }
        return loaded;
    }
    
    private void fireMapLoaded() {
        for (int i = 0; i < this.m_mapLoadListeners.size(); ++i) {
            this.m_mapLoadListeners.get(i).onMapLoaded();
        }
    }
    
    public void addMapLoadListener(final MapLoadListener loadListener) {
        if (!this.m_mapLoadListeners.contains(loadListener)) {
            this.m_mapLoadListeners.add(loadListener);
        }
    }
    
    public void removeMapLoadListener(final MapLoadListener loadListener) {
        this.m_mapLoadListeners.remove(loadListener);
    }
    
    public boolean addReloadListener(final ReloadListener reloadListener) {
        return this.m_reloadListener.add(reloadListener);
    }
    
    public boolean removeReloadListener(final ReloadListener reloadListener) {
        return this.m_reloadListener.remove(reloadListener);
    }
    
    public void reloadDisplayedScreenMaps() {
        assert this.m_maps.size() == this.m_world.mapCount();
        for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
            final DisplayedScreenMap displayedMap = this.m_maps.get(i);
            displayedMap.setMap(displayedMap.getMap(), this.m_factory, true);
        }
        for (int i = 0; i < this.m_reloadListener.size(); ++i) {
            this.m_reloadListener.get(i).onReload();
        }
    }
    
    public void addToScene(final IsoWorldScene scene) {
        for (int i = 0, size = this.m_visibleEntities.size(); i < size; ++i) {
            this.m_visibleEntities.get(i).m_added = false;
        }
        this.m_visibleEntities.clear();
        final AbstractCamera camera = scene.getCam();
        for (int j = 0, numMaps = this.m_maps.size(); j < numMaps; ++j) {
            this.m_maps.get(j).addToScene(scene, this.m_visibleEntities, camera);
        }
        for (int j = 0, size2 = this.m_externalElements.size(); j < size2; ++j) {
            this.m_externalElements.get(j).addToScene(scene, this.m_visibleEntities, camera);
        }
        scene.addEntities(this.m_visibleEntities);
        this.m_readyState |= 0x2;
    }
    
    public final void getElements(final int cellX, final int cellY, final ArrayList<DisplayedScreenElement> elements, final ElementFilter elementFilter) {
        for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
            this.m_maps.get(i).getElements(cellX, cellY, elements, elementFilter);
        }
    }
    
    public final DisplayedScreenElement getElementAtTop(final int x, final int y, final ElementFilter elementFilter) {
        DisplayedScreenElement elementAtTop = null;
        for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
            final DisplayedScreenMap screenMap = this.m_maps.get(i);
            final DisplayedScreenElement element = screenMap.getElementAtTop(x, y, elementFilter);
            if (element != null) {
                if (elementAtTop == null || elementAtTop.m_element.m_altitudeOrder <= element.m_element.m_altitudeOrder) {
                    elementAtTop = element;
                }
            }
        }
        return elementAtTop;
    }
    
    public final DisplayedScreenElement getElementFromTop(final int from, final int x, final int y, final ElementFilter elementFilter) {
        DisplayedScreenElement elementFromTop = null;
        for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
            final DisplayedScreenMap screenMap = this.m_maps.get(i);
            final DisplayedScreenElement element = screenMap.getElementFromTop(from, x, y, elementFilter);
            if (element != null) {
                if (elementFromTop == null || elementFromTop.m_element.m_altitudeOrder <= element.m_element.m_altitudeOrder) {
                    elementFromTop = element;
                }
            }
        }
        return elementFromTop;
    }
    
    public final DisplayedScreenElement getElementAtTop(final int x, final int y, final int z, final ElementFilter elementFilter) {
        DisplayedScreenElement elementAtTop = null;
        for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
            final DisplayedScreenMap screenMap = this.m_maps.get(i);
            final DisplayedScreenElement element = screenMap.getElementAtTop(x, y, z, elementFilter);
            if (element != null) {
                if (elementAtTop == null || elementAtTop.m_element.m_altitudeOrder <= element.m_element.m_altitudeOrder) {
                    elementAtTop = element;
                }
            }
        }
        return elementAtTop;
    }
    
    public final DisplayedScreenElement getWalkableElementAtTop(final int x, final int y, final int z, final ElementFilter elementFilter) {
        DisplayedScreenElement elementAtTop = null;
        for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
            final DisplayedScreenMap screenMap = this.m_maps.get(i);
            final DisplayedScreenElement element = screenMap.getWalkableElementAtTop(x, y, z, elementFilter);
            if (element != null) {
                if (elementAtTop == null || elementAtTop.m_element.m_altitudeOrder <= element.m_element.m_altitudeOrder) {
                    elementAtTop = element;
                }
            }
        }
        return elementAtTop;
    }
    
    public final DisplayedScreenElement getNearesetWalkableElement(final int cellX, final int cellY, final int z, final ElementFilter elementFilter) {
        final short cellZ = TopologyMapManager.getNearestWalkableZ(cellX, cellY, (short)z);
        if (cellZ == -32768) {
            return null;
        }
        return this.getWalkableElementAtTop(cellX, cellY, cellZ, elementFilter);
    }
    
    public final DisplayedScreenElement getNearesetElement(final int cellX, final int cellY, final int z, final ElementFilter elementFilter) {
        final short cellZ = TopologyMapManager.getNearestZ(cellX, cellY, (short)z);
        if (cellZ == -32768) {
            return null;
        }
        return this.getElementAtTop(cellX, cellY, cellZ, elementFilter);
    }
    
    public void getDisplayedElements(final int x, final int y, final ArrayList<DisplayedScreenElement> elements) {
        for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
            final DisplayedScreenMap screenMap = this.m_maps.get(i);
            final ScreenMap map = screenMap.getMap();
            if (map != null) {
                screenMap.getDisplayedElements(x, y, elements);
            }
        }
    }
    
    public final ArrayList<DisplayedScreenMap> getMaps() {
        return this.m_maps;
    }
    
    public final boolean isReady() {
        return (this.m_readyState & 0x1) == 0x1;
    }
    
    final boolean isReadyAndDisplay() {
        return this.m_readyState == 3;
    }
    
    public void setMapsCoordinates(final PartitionExistValidator mapsCoordinates) {
        this.m_mapsCoordinates = mapsCoordinates;
    }
    
    public void setLODLevel(final int level) {
        this.m_lodLevel = level;
        final byte visibilityMask = (byte)((DisplayedScreenWorld.m_visibilityMask & 0xFFFFFFF8) | getLODMask(level));
        this.setVisibilityMask(visibilityMask);
        this.reloadDisplayedScreenMaps();
    }
    
    public int getLodLevel() {
        return this.m_lodLevel;
    }
    
    public void setVisibilityMask(final byte mask, final boolean enable) {
        byte totalMask = DisplayedScreenWorld.m_visibilityMask;
        if (enable) {
            totalMask |= mask;
        }
        else {
            totalMask &= (byte)~mask;
        }
        this.setVisibilityMask(totalMask);
    }
    
    public static byte getLODMask(final int lodLevel) {
        switch (lodLevel) {
            case 2: {
                return 7;
            }
            case 1: {
                return 3;
            }
            case 0: {
                return 1;
            }
            default: {
                return 1;
            }
        }
    }
    
    private void setVisibilityMask(final byte mask) {
        if (mask > DisplayedScreenWorld.m_visibilityMask) {
            this.m_factory.createSpriteForLOD(mask);
            IsoSceneLightManager.INSTANCE.forceUpdate();
        }
        DisplayedScreenWorld.m_visibilityMask = mask;
    }
    
    public static byte getVisibilityMask() {
        return DisplayedScreenWorld.m_visibilityMask;
    }
    
    @Override
    public final void queryObjects(final AbstractCamera camera, final ArrayList<LitSceneObject> objects) {
        for (int i = 0, size = this.m_visibleEntities.size(); i < size; ++i) {
            objects.add(this.m_visibleEntities.get(i));
        }
    }
    
    public boolean addExternalElement(final DisplayedScreenElement o) {
        return this.m_externalElements.add(o);
    }
    
    public boolean removeExternalElement(final Object o) {
        return this.m_externalElements.remove(o);
    }
    
    public void clearExternalElements() {
        this.m_externalElements.clear();
    }
    
    static {
        DisplayedScreenWorld.m_visibilityMask = -1;
        m_instance = new DisplayedScreenWorld();
        m_logger = Logger.getLogger((Class)DisplayedScreenWorld.class);
    }
    
    public interface ReloadListener
    {
        void onReload();
    }
}
