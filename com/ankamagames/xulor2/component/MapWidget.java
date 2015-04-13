package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.mesh.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import java.awt.geom.*;
import com.ankamagames.framework.graphics.image.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.component.mapOverlay.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.xulor2.util.*;
import gnu.trove.*;
import com.ankamagames.xulor2.core.dragndrop.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.event.*;

public class MapWidget extends MapOverlay<MapMesh> implements PixmapClient
{
    private static final Logger m_logger;
    public static final String POISSON_DISK = "poissonDisk";
    public static final String TAG = "map";
    public static final String INTERNAL_POPUP = "internalPopup";
    public static final String INTERNAL_POPUP_TEXT_VIEW = "internalPopupTextView";
    public static final String INTERNAL_POPUP_TEXT_EDITOR = "internalPopupTextEditor";
    public static final String INTERNAL_POPUP_VALID_BUTTON = "internalPopupValid";
    public static final String INTERNAL_POPUP_CANCEL_BUTTON = "internalPopupCancel";
    private MapDragNDropHandler m_dragNDropListener;
    private Pixmap m_mapBackgroundPixmap;
    private int m_mapBackgroundStartX;
    private int m_mapBackgroundStartY;
    private int m_mapBackgroundEndX;
    private int m_mapBackgroundEndY;
    private EventListener m_mrcResizeListener;
    private String m_mapAnmPath;
    private MapWidgetZoneManager m_zoneManager;
    private boolean m_displayMapZones;
    private Texture m_maskTexture;
    private boolean m_needsToRemoveMapZones;
    private boolean m_needsToComputeMapZones;
    private boolean m_needsToComputeZoom;
    private boolean m_needsToComputeMinSize;
    private boolean m_needsToLoadTextures;
    private final ArrayList<MapListener> m_mapListeners;
    public static final int KNOWN_MAPS_HASH;
    
    public MapWidget() {
        super();
        this.m_displayMapZones = true;
        this.m_mapListeners = new ArrayList<MapListener>();
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
    }
    
    public void addMapListener(final MapListener listener) {
        this.m_mapListeners.add(listener);
    }
    
    public void setMapBackgroundPixmap(final Pixmap mapBackgroundPixmap) {
        this.m_mapBackgroundPixmap = mapBackgroundPixmap;
        this.m_needsToLoadTextures = true;
        this.setNeedsToPreProcess();
    }
    
    public Point2D.Float getMapWidgetZoom() {
        if (this.m_mapBackgroundPixmap == null) {
            return new Point2D.Float(1.0f, 1.0f);
        }
        final float mapDecoWidthRatio = (this.m_mapBackgroundEndX - this.m_mapBackgroundStartX) / this.m_mapBackgroundPixmap.getWidth();
        final float mapDecoHeightRatio = (this.m_mapBackgroundEndY - this.m_mapBackgroundStartY) / this.m_mapBackgroundPixmap.getHeight();
        final float width = this.getWidth() / mapDecoWidthRatio;
        final float height = this.getHeight() / mapDecoHeightRatio;
        final float widthRatio = width / this.m_mapBackgroundPixmap.getWidth();
        final float heightRatio = height / this.m_mapBackgroundPixmap.getHeight();
        MapWidget.m_logger.info((Object)("width ratio = " + widthRatio));
        MapWidget.m_logger.info((Object)("height ratio = " + heightRatio));
        return new Point2D.Float(widthRatio, heightRatio);
    }
    
    public String getMapAnmPath() {
        return this.m_mapAnmPath;
    }
    
    public void setMapAnmPath(final String mapAnmPath) {
        this.m_mapAnmPath = mapAnmPath;
    }
    
    public Pixmap getMapBackgroundPixmap() {
        return this.m_mapBackgroundPixmap;
    }
    
    public int getMapBackgroundStartX() {
        return this.m_mapBackgroundStartX;
    }
    
    public int getMapBackgroundStartY() {
        return this.m_mapBackgroundStartY;
    }
    
    public int getMapBackgroundEndX() {
        return this.m_mapBackgroundEndX;
    }
    
    public int getMapBackgroundEndY() {
        return this.m_mapBackgroundEndY;
    }
    
    public void setMapBackgroundStartX(final int mapBackgroundStartX) {
        this.m_mapBackgroundStartX = mapBackgroundStartX;
    }
    
    public void setMapBackgroundStartY(final int mapBackgroundStartY) {
        this.m_mapBackgroundStartY = mapBackgroundStartY;
    }
    
    public void setMapBackgroundEndX(final int mapBackgroundEndX) {
        this.m_mapBackgroundEndX = mapBackgroundEndX;
    }
    
    public void setMapBackgroundEndY(final int mapBackgroundEndY) {
        this.m_mapBackgroundEndY = mapBackgroundEndY;
    }
    
    @Override
    public void setPixmap(final PixmapElement p) {
        if (p == null) {
            return;
        }
        final String name = p.getName();
        if (name == null || !name.equals("poissonDisk")) {
            return;
        }
        final Pixmap pixmap = p.getPixmap();
        if (pixmap != null && pixmap.needsCompute()) {
            pixmap.computeCoordinates();
        }
    }
    
    @Override
    public void setModulationColor(final Color c) {
    }
    
    @Override
    public Color getModulationColor() {
        return null;
    }
    
    @Override
    public String getTag() {
        return "map";
    }
    
    public void setKnownMaps(final int[] mapsCoordinates) {
    }
    
    @Override
    protected void setMeshCenter() {
    }
    
    @Override
    protected void createMaskTexture(final String path, final URL url, final DocumentEntry child) {
        this.m_maskTexture = TextureInfo.createMaskTexture(child, url, path);
    }
    
    @Override
    protected void endSetPath() {
        this.m_needsToComputeMapZones = true;
        this.m_needsToComputeZoom = true;
        this.m_needsToComputeMinSize = true;
        super.endSetPath();
    }
    
    @Override
    protected void createPixmap(final TextureInfo textureInfo, final Texture texture) {
        super.createPixmap(textureInfo, texture);
        this.initMapBackground();
        this.m_needsToLoadTextures = true;
    }
    
    private void initMapBackground() {
        if (this.m_mapBackgroundPixmap != null) {
            final float mapDecoWidthRatio = (this.m_mapBackgroundEndX - this.m_mapBackgroundStartX) / this.m_mapBackgroundPixmap.getWidth();
            final float mapDecoHeightRatio = (this.m_mapBackgroundEndY - this.m_mapBackgroundStartY) / this.m_mapBackgroundPixmap.getHeight();
            final float mapDecoIsoWidth = this.m_mapChunkWidth / mapDecoWidthRatio;
            final float mapDecoIsoHeight = this.m_mapChunkHeight / mapDecoHeightRatio;
            final float mapDecoIsoX = -mapDecoIsoWidth * this.m_mapBackgroundStartX / this.m_mapBackgroundPixmap.getWidth();
            final float mapDecoIsoY = -mapDecoIsoHeight * this.m_mapBackgroundStartY / this.m_mapBackgroundPixmap.getHeight();
            ((MapMesh)this.m_mesh).setMapBackgroundParameters((int)mapDecoIsoX, (int)mapDecoIsoY, (int)mapDecoIsoWidth, (int)mapDecoIsoHeight);
        }
    }
    
    @Override
    protected void setBaseMapDisplayer() {
        ((MapMesh)this.m_mesh).setMapDisplayer(new PixmapMapDisplayer());
    }
    
    @Override
    public void setMapRect(final int minX, final int minY, final int width, final int height) {
        super.setMapRect(minX, minY, width, height);
        this.initMapBackground();
    }
    
    public void setAllMapZonesVisible(final boolean visible) {
        this.m_displayMapZones = visible;
        ((MapMesh)this.m_mesh).setZoneDisplayEnabled(this.m_displayMapZones && this.m_zoneManager.hasMapZone());
        this.m_needsToComputeImage = true;
        this.setNeedsToPostProcess();
    }
    
    public void setMapZoneVisibleById(final int id, final boolean visible) {
        if (!this.m_zoneManager.setMapZoneVisibleById(id, visible)) {
            MapWidget.m_logger.info((Object)("Impossible de trouver la zone d'id " + id));
        }
        this.m_needsToComputeImage = true;
        this.setNeedsToPostProcess();
    }
    
    public void setPlayerMapZone(final int cellX, final int cellY) {
        this.m_zoneManager.setPlayerMapZone(cellX, cellY);
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof MapAppearance;
    }
    
    public MapZone getSelectedMapZone() {
        return this.m_zoneManager.getSelectedMapZone();
    }
    
    public void removeAllMapZones() {
        this.m_zoneManager.removeAllMapZones();
        this.m_needsToRemoveMapZones = true;
        this.setNeedsToPostProcess();
    }
    
    public void removeMapZone(final int id) {
        this.m_zoneManager.removeMapZone(id);
        this.m_needsToRemoveMapZones = true;
        this.setNeedsToPostProcess();
    }
    
    private void doRemoveMapZones() {
        this.m_zoneManager.doRemoveMapZones();
        this.m_needsToRemoveMapZones = false;
        this.m_needsToComputeImage = true;
    }
    
    public void addMapZone(final ParentMapZoneDescription parentMapZoneDesc) {
        final ParentMapZone parentMapZone = MapWidgetZoneManager.createParentMapZone(parentMapZoneDesc, this.m_zoom, this.m_originX, this.m_originY, this.m_mapAnmPath);
        this.m_zoneManager.put(parentMapZoneDesc.getId(), parentMapZone);
        ((MapMesh)this.m_mesh).addMapZone(parentMapZoneDesc.getId(), parentMapZone);
        this.m_needsToComputeImage = true;
        this.setNeedsToPostProcess();
    }
    
    @Override
    protected void mouseMovedIso(final int isoX, final int isoY) {
        super.mouseMovedIso(isoX, isoY);
        final int partX = PartitionConstants.getPartitionXFromCellX(isoX);
        final int partY = PartitionConstants.getPartitionYFromCellY(isoY);
        this.m_zoneManager.setSelectedMapZone(partX, partY);
    }
    
    @Override
    public void validate() {
        super.validate();
        this.m_needsToComputeZoom = true;
        this.m_zoneManager.setPlayerMapZone();
        for (int i = 0, size = this.m_mapListeners.size(); i < size; ++i) {
            this.m_mapListeners.get(i).onResized();
        }
    }
    
    private boolean computeMinSize() {
        if (this.m_needsToComputeMinSize) {
            final Dimension greedySize = MasterRootContainer.getInstance().getSize();
            boolean minSizeChanged = false;
            float width = greedySize.width * 0.9f;
            float height = greedySize.height * 0.9f;
            final float availableSizeRatio = width / height;
            final float ratio = 2.0f;
            if (availableSizeRatio >= 2.0f) {
                width = height * 2.0f;
            }
            else {
                height = width / 2.0f;
            }
            if (this.m_minSize == null || this.m_minSize.width != width || this.m_minSize.height != height) {
                this.setMinSize(new Dimension((int)width, (int)height));
                minSizeChanged = true;
            }
            this.m_needsToComputeMinSize = false;
            return minSizeChanged;
        }
        return false;
    }
    
    private void computeMinMaxZoom() {
        if (!this.m_needsToComputeZoom) {
            return;
        }
        if (this.m_appearance == null) {
            return;
        }
        final float widthFactor = this.m_appearance.getContentWidth() / this.m_mapChunkWidth;
        final float heightFactor = this.m_appearance.getContentHeight() / this.m_mapChunkHeight;
        final float zoomFactor = Math.min(widthFactor, heightFactor);
        this.setMinZoom(zoomFactor);
        this.setMaxZoom(zoomFactor);
        this.m_needsToComputeZoom = false;
        this.m_needsToComputeImage = true;
        final float screenX = -this.m_originX * this.m_zoom;
        final float screenY = -this.m_originY * this.m_zoom;
        final float isoX = this.m_scene.screenToIsoX(screenX, screenY);
        final float isoY = this.m_scene.screenToIsoY(screenX, screenY);
        this.setIsoCenterX(isoX);
        this.setIsoCenterY(isoY);
        this.setIsoCenterZ(0.0f);
    }
    
    private void computeMapZones() {
        this.m_zoneManager.computeMapZones(this.m_zoom, this.m_originX, this.m_originY);
        this.m_needsToComputeImage = true;
        this.m_needsToComputeMapZones = false;
    }
    
    @Override
    protected void computeImageMesh() {
        final TIntObjectHashMap<Color> colorById = new TIntObjectHashMap<Color>();
        final TIntByteHashMap indexById = new TIntByteHashMap();
        this.m_zoneManager.computeColorAndIndex(this.m_zoom, colorById, indexById);
        super.computeImageMesh();
        ((MapMesh)this.m_mesh).setColorByZoneId(colorById);
        ((MapMesh)this.m_mesh).setZoneIndexById(indexById);
        ((MapMesh)this.m_mesh).setZoneDisplayEnabled(this.m_displayMapZones && this.m_zoneManager.hasMapZone());
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_needsToComputeMinSize && this.computeMinSize() && this.m_containerParent != null) {
            this.m_containerParent.invalidateMinSize();
            this.setNeedsToPostProcess();
        }
        if (this.m_needsToLoadTextures) {
            final boolean backgroundLoaded = this.m_mapBackgroundPixmap != null && this.m_mapBackgroundPixmap.getTexture().isReady();
            final boolean pixmapLoaded = (this.m_pixmap != null && this.m_pixmap.getTexture().isReady()) || this.m_forceDisplayEntity;
            final boolean maskLoaded = this.m_maskTexture == null || this.m_maskTexture.isReady();
            if (!backgroundLoaded || !pixmapLoaded || !maskLoaded) {
                return true;
            }
            if (this.m_pixmap != null) {
                ((MapMesh)this.m_mesh).setPixmap(this.m_pixmap);
            }
            ((MapMesh)this.m_mesh).setZoneMaskTexture(this.m_maskTexture);
            if (this.m_mapBackgroundPixmap != null) {
                ((MapMesh)this.m_mesh).setMapBackground(this.m_mapBackgroundPixmap);
            }
            for (int i = this.m_mapListeners.size() - 1; i >= 0; --i) {
                this.m_mapListeners.get(i).onTexturesLoaded();
            }
            this.m_needsToLoadTextures = false;
            this.m_needsToComputeImage = true;
            this.setNeedsToPreProcess();
            this.setNeedsToPostProcess();
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (this.m_needsToLoadTextures) {
            return ret;
        }
        if (this.m_needsToRemoveMapZones) {
            this.doRemoveMapZones();
        }
        this.m_zoneManager.process(deltaTime);
        if (this.m_needsToComputeZoom) {
            this.computeMinMaxZoom();
        }
        if (this.m_needsToComputeMapZones) {
            this.computeMapZones();
        }
        if (this.m_needsToComputeImage) {
            this.computeImage();
        }
        return ret;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mapListeners.clear();
        this.m_zoneManager.clear();
        this.m_zoneManager = null;
        DragNDropManager.getInstance().removeDragNDropListener(this.m_dragNDropListener, true);
        MasterRootContainer.getInstance().removeEventListener(Events.RESIZED, this.m_mrcResizeListener, false);
        this.m_mrcResizeListener = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final MapAppearance app = new MapAppearance();
        app.onCheckOut();
        app.setWidget(this);
        this.add(app);
        this.setLayoutManager(null);
        this.m_mesh = (T)new MapMesh();
        ((MapMesh)this.m_mesh).onCheckOut();
        ((MapMesh)this.m_mesh).setModulationColor(new Color(Color.WHITE));
        this.m_zoneManager = new MapWidgetZoneManager((MapMesh)this.m_mesh);
        this.m_displayMapZones = true;
        this.m_needsToRemoveMapZones = false;
        this.m_needsToLoadTextures = false;
        this.m_enableTooltip = false;
        this.m_dragNDropListener = new MapDragNDropHandler(this);
        DragNDropManager.getInstance().addDragNDropListener(this.m_dragNDropListener);
        this.m_mrcResizeListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() == MasterRootContainer.getInstance()) {
                    MapWidget.this.m_needsToComputeMinSize = true;
                    MapWidget.this.setNeedsToPreProcess();
                }
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.RESIZED, this.m_mrcResizeListener, false);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == MapWidget.KNOWN_MAPS_HASH) {
            this.setKnownMaps((int[])value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapWidget.class);
        KNOWN_MAPS_HASH = "knownMaps".hashCode();
    }
    
    public interface MapListener
    {
        boolean onTexturesLoaded();
        
        boolean onResized();
    }
}
