package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.baseImpl.graphics.isometric.text.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import java.awt.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.mapOverlay.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public abstract class MapOverlay<T extends MapOverlayMesh> extends DragNDropContainer
{
    private static final int MAP_NAVIGATOR_UPDATE_FREQUENCY = 200;
    private static final EffectParams COLOR_SCALE_PARAMS;
    protected EntityGroup m_group;
    private BackgroundedTextHotPointPosition m_tooltipHotPoint;
    private ToolTipElement m_innerTooltip;
    protected boolean m_enableTooltip;
    private final MapOverlayCoord m_coords;
    private boolean m_isoMap;
    protected float m_zoom;
    protected float m_maxZoom;
    protected float m_minZoom;
    protected float m_zoomScale;
    private boolean m_enableMapMoveEvent;
    protected boolean m_forceDisplayEntity;
    protected MapOverlayPoint m_overItem;
    private final MapOverlayPointList m_items;
    private final MapOverlayPointList m_compass;
    private final MapOverlayPointList m_landMarks;
    private final HashMap<String, Texture> m_textures;
    private final MapOverlayParticleManager m_particleManager;
    private boolean m_dontInterpolateNextRender;
    protected IsoWorldScene m_scene;
    protected boolean m_displayPoints;
    protected boolean m_displayCompass;
    private float m_landMarkZoom;
    private boolean m_useAlternateTexture;
    protected float m_originX;
    protected float m_originY;
    protected float m_mapChunkWidth;
    protected float m_mapChunkHeight;
    protected Pixmap m_pixmap;
    protected T m_mesh;
    protected boolean m_needsToComputeImage;
    private short m_mapId;
    public static final int CONTENT_HASH;
    public static final int COMPASS_CONTENT_HASH;
    public static final int LANDMARK_CONTENT_HASH;
    public static final int ISO_CENTER_X_HASH;
    public static final int ISO_CENTER_Y_HASH;
    public static final int ISO_CENTER_Z_HASH;
    public static final int ISO_MAP_HASH;
    public static final int MAX_ZOOM_HASH;
    public static final int MIN_ZOOM_HASH;
    public static final int TOOLTIP_HOT_POINT_HASH;
    public static final int ZOOM_SCALE_HASH;
    public static final int ON_MAP_CLICK_HASH;
    public static final int ON_MAP_DOUBLE_CLICK_HASH;
    public static final int ON_MAP_MOVE_HASH;
    public static final int ENABLE_TOOLTIP_HASH;
    public static final int LANDMARK_ZOOM_HASH;
    public static final int MAP_PATH_HASH;
    public static final int MAP_ID_HASH;
    
    public MapOverlay() {
        super();
        this.m_tooltipHotPoint = BackgroundedTextHotPointPosition.SOUTH;
        this.m_enableTooltip = true;
        this.m_coords = new MapOverlayCoord();
        this.m_zoom = 1.0f;
        this.m_maxZoom = 1.0f;
        this.m_minZoom = 1.0f;
        this.m_zoomScale = 1.0f;
        this.m_items = new MapOverlayPointList();
        this.m_compass = new MapOverlayPointList();
        this.m_landMarks = new MapOverlayPointList();
        this.m_textures = new HashMap<String, Texture>();
        this.m_particleManager = new MapOverlayParticleManager();
    }
    
    private boolean applyTextureToMesh(final EntitySprite point, final DisplayableMapPointIcon icon) {
        final Texture texture = this.getMeshTexture(icon.getTexturePath());
        if (texture != null) {
            if (texture.isEmpty()) {
                return false;
            }
            final Layer layer = texture.getLayer(0);
            final Point2i imageSize = texture.getSize();
            final int width = imageSize.getX();
            final int height = imageSize.getY();
            point.setTexture(texture);
            point.setTextureCoordinates(0.0f, 0.0f, height / layer.getHeight(), width / layer.getWidth());
        }
        return true;
    }
    
    private void displayPoint(final IsoWorldScene scene, final DisplayableMapPoint displayableMapPoint, final EntitySprite point, final EntitySprite overlaySprite, final float halfScreenX, final float halfScreenY, final Point2 deltaCenter, final boolean stickToScreen, final boolean dontInterpolate) {
        if (!displayableMapPoint.isVisible()) {
            return;
        }
        if (this.m_mapId == -1 || displayableMapPoint.getInstanceId() != this.m_mapId) {
            return;
        }
        if (displayableMapPoint.needToBeRefreshed(0.05f)) {
            displayableMapPoint.setIsoX(this.getIsoValue(displayableMapPoint.getIsoX(), displayableMapPoint.getDesiredIsoX(), dontInterpolate));
            displayableMapPoint.setIsoY(this.getIsoValue(displayableMapPoint.getIsoY(), displayableMapPoint.getDesiredIsoY(), dontInterpolate));
            displayableMapPoint.setIsoZ(this.getIsoValue(displayableMapPoint.getIsoZ(), displayableMapPoint.getDesiredIsoZ(), dontInterpolate));
        }
        final boolean isOver = this.m_overItem != null && this.m_overItem.m_mesh == point;
        int width = 3;
        int height = 3;
        int originalHeight = 3;
        final boolean useAlternateTexture = this.m_useAlternateTexture && displayableMapPoint.getAlternateIcon() != null;
        final DisplayableMapPointIcon icon = useAlternateTexture ? displayableMapPoint.getAlternateIcon() : displayableMapPoint.getIcon();
        if (icon != null && this.applyTextureToMesh(point, icon)) {
            final Point2i texSize = point.getTexture().getSize();
            width = texSize.getX();
            originalHeight = (height = texSize.getY());
            int overlayWidth = 3;
            int overlayHeight = 3;
            int overlayOriginalHeight = 3;
            final DisplayableMapPointIcon overlayIcon = displayableMapPoint.getOverlayIcon();
            if (overlayIcon != null && this.applyTextureToMesh(overlaySprite, overlayIcon)) {
                overlaySprite.setVisible(true);
                final Point2i texSize2 = overlaySprite.getTexture().getSize();
                overlayWidth = texSize2.getX();
                overlayOriginalHeight = (overlayHeight = texSize2.getY());
            }
            else {
                overlaySprite.setVisible(false);
            }
            final float xScreenItem = scene.isoToScreenX(displayableMapPoint.getIsoX(), displayableMapPoint.getIsoY()) - deltaCenter.getX();
            final float yScreenItem = scene.isoToScreenY(displayableMapPoint.getIsoX(), displayableMapPoint.getIsoY(), displayableMapPoint.getIsoZ()) - deltaCenter.getY();
            int screenXItem2 = (int)(xScreenItem + this.getAppearance().getContentWidth() / 2.0f);
            int screenYItem2 = (int)(yScreenItem + this.getAppearance().getContentHeight() / 2.0f);
            screenXItem2 = (int)(xScreenItem * 1.05f + this.getAppearance().getContentWidth() / 2.0f);
            screenYItem2 = (int)(yScreenItem * 1.05f + this.getAppearance().getContentHeight() / 2.0f);
            final boolean haveToDisplay = this.getAppearance().getShape().insideInsets(screenXItem2, screenYItem2, this.getWidth(), this.getHeight());
            int x;
            int overlayX;
            int y;
            int overlayY;
            if (!haveToDisplay && stickToScreen) {
                overlayX = (x = this.getAppearance().getOnScreenX(screenXItem2, screenYItem2) - (int)(this.getAppearance().getContentWidth() / 2.0f));
                overlayY = (y = this.getAppearance().getOnScreenY(screenXItem2, screenYItem2) - (int)(this.getAppearance().getContentHeight() / 2.0f));
            }
            else {
                overlayX = (x = (int)xScreenItem);
                overlayY = (y = (int)yScreenItem);
            }
            if (isOver && displayableMapPoint.isHighlightOnOver()) {
                width *= (int)1.2;
                height *= (int)1.2;
                overlayWidth *= (int)1.2;
                overlayHeight *= (int)1.2;
            }
            if (!useAlternateTexture) {
                width *= (int)this.m_landMarkZoom;
                height *= (int)this.m_landMarkZoom;
                overlayWidth *= (int)this.m_landMarkZoom;
                overlayHeight *= (int)this.m_landMarkZoom;
            }
            x -= (int)(width * icon.getxHotPoint());
            y += (int)(height * icon.getyHotPoint());
            if (overlayIcon != null) {
                overlayX -= (int)(overlayWidth * overlayIcon.getxHotPoint());
                overlayY = y - 1;
            }
            point.setBounds((int)(halfScreenY + y), (int)(halfScreenX + x), width, height);
            overlaySprite.setBounds((int)(halfScreenY + overlayY), (int)(halfScreenX + overlayX), overlayWidth, overlayHeight);
            final float[] color = displayableMapPoint.getColor();
            float alpha = 1.0f;
            if (color != null && color.length == 4) {
                alpha = color[3] * ((stickToScreen && !haveToDisplay) ? 0.5f : 1.0f);
            }
            final XulorParticleSystem ps = this.m_particleManager.getParticleSystem(displayableMapPoint);
            if (ps != null) {
                final int deltaY = height - originalHeight;
                ps.setPosition(halfScreenX + xScreenItem, halfScreenY + yScreenItem + deltaY);
            }
            if (stickToScreen || haveToDisplay) {
                final Effect baseEffect = EffectManager.getInstance().getBaseEffect();
                if (baseEffect.isTechniqueValide(FxConstants.LUMINANCE_TECHNIQUE)) {
                    if (displayableMapPoint.isUseGrayScale()) {
                        point.setEffect(baseEffect, FxConstants.LUMINANCE_TECHNIQUE, null);
                        overlaySprite.setEffect(baseEffect, FxConstants.LUMINANCE_TECHNIQUE, null);
                    }
                    else {
                        point.setEffect(baseEffect, FxConstants.TRANSFORM_TECHNIQUE, MapOverlay.COLOR_SCALE_PARAMS);
                        overlaySprite.setEffect(baseEffect, FxConstants.TRANSFORM_TECHNIQUE, MapOverlay.COLOR_SCALE_PARAMS);
                    }
                }
                else if (displayableMapPoint.isUseGrayScale()) {
                    alpha *= 0.5f;
                }
                point.setColor(color[0], color[1], color[2], alpha);
                overlaySprite.setColor(color[0], color[1], color[2], alpha);
                this.m_group.addChild(point);
                this.m_group.addChild(overlaySprite);
                if (ps != null) {
                    ps.prepareParticlesBeforeRendering(this.m_group);
                }
            }
        }
    }
    
    private float getIsoValue(final float iso, final float desiredIso, final boolean dontInterpolate) {
        return this.m_coords.getIsoValue(iso, desiredIso, dontInterpolate);
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
    }
    
    @Override
    protected void addInnerMeshes() {
        this.m_entity.addChild(this.m_mesh.getEntity());
        this.m_entity.addChild(this.m_group);
        super.addInnerMeshes();
    }
    
    public boolean isDisplayPoints() {
        return this.m_displayPoints;
    }
    
    public void setDisplayPoints(final boolean displayPoints) {
        this.m_displayPoints = displayPoints;
    }
    
    public boolean isDisplayCompass() {
        return this.m_displayCompass;
    }
    
    public void setDisplayCompass(final boolean displayCompass) {
        this.m_displayCompass = displayCompass;
    }
    
    public float getIsoCenterX() {
        return this.m_coords.getIsoCenterX();
    }
    
    public void setIsoCenterX(final float isoCenterX) {
        this.m_coords.setIsoCenterX(isoCenterX);
    }
    
    public float getIsoCenterY() {
        return this.m_coords.getIsoCenterY();
    }
    
    public void setIsoCenterY(final float isoCenterY) {
        this.m_coords.setIsoCenterY(isoCenterY);
    }
    
    public float getIsoCenterZ() {
        return this.m_coords.getIsoCenterZ();
    }
    
    public void setIsoCenterZ(final float isoCenterZ) {
        this.m_coords.setIsoCenterZ(isoCenterZ);
    }
    
    public boolean getEnableTooltip() {
        return this.m_enableTooltip;
    }
    
    public void setEnableTooltip(final boolean enableTooltip) {
        this.m_enableTooltip = enableTooltip;
    }
    
    public float getZoom() {
        return this.m_zoom;
    }
    
    public float getMinZoom() {
        return this.m_minZoom;
    }
    
    public void setMinZoom(final float minZoom) {
        if (minZoom > 0.0f && minZoom <= 1.0f) {
            this.m_minZoom = minZoom;
            this.setZoom();
        }
    }
    
    public float getMaxZoom() {
        return this.m_maxZoom;
    }
    
    public void setMaxZoom(final float maxZoom) {
        if (maxZoom > 0.0f && maxZoom <= 1.0f) {
            this.m_maxZoom = maxZoom;
            this.setZoom();
        }
    }
    
    public float getZoomScale() {
        return this.m_zoomScale;
    }
    
    public void setZoomScale(final float zoomScale) {
        if (zoomScale >= 0.0f && zoomScale <= 1.0f) {
            this.m_zoomScale = zoomScale;
            this.setZoom();
        }
    }
    
    public void setMapId(final short mapId) {
        this.m_mapId = mapId;
    }
    
    public BackgroundedTextHotPointPosition getTooltipHotPoint() {
        return this.m_tooltipHotPoint;
    }
    
    public void setTooltipHotPoint(final BackgroundedTextHotPointPosition tooltipHotPoint) {
        this.m_tooltipHotPoint = tooltipHotPoint;
    }
    
    protected void setZoom() {
        this.m_zoom = this.m_minZoom + (this.m_maxZoom - this.m_minZoom) * this.m_zoomScale;
        assert this.m_zoom > 0.0f && this.m_zoom <= 1.0f;
        this.m_coords.setZoom(this.m_zoom);
        this.m_scene.setCellWidth(86.0f * this.m_zoom);
        this.m_scene.setCellHeight(43.0f * this.m_zoom * (this.m_isoMap ? 1 : 2));
        this.m_scene.setElevationUnit(10.0f * this.m_zoom);
        this.m_mesh.setZoomFactor(this.m_zoom);
        this.m_needsToComputeImage = true;
        this.setNeedsToPostProcess();
    }
    
    public boolean isIsoMap() {
        return this.m_isoMap;
    }
    
    public void setIsoMap(final boolean isoMap) {
        this.m_isoMap = isoMap;
        this.setZoom();
    }
    
    private void setList(final MapOverlayPointList list, final ArrayList<DisplayableMapPoint> item) {
        this.m_particleManager.clearParticleLinkedTo(list);
        list.addAll(item);
    }
    
    public void addLandMark(final DisplayableMapPoint item) {
        this.m_landMarks.add(item);
    }
    
    public void removeLandMark(final DisplayableMapPoint item) {
        this.m_landMarks.remove(item);
        if (this.getOverItem() == item) {
            this.setOverItem(null);
        }
    }
    
    public void clearLandMarks() {
        this.m_landMarks.clear();
        this.setOverItem(null);
    }
    
    private Texture getMeshTexture(final String path) {
        Texture texture = this.m_textures.get(path);
        if (texture != null) {
            return texture;
        }
        texture = TextureInfo.createTexturePowerOfTwo(path);
        this.m_textures.put(path, texture);
        return texture;
    }
    
    @Override
    public Widget getWidget(final int x, final int y) {
        if (this.m_unloading || MasterRootContainer.getInstance().isMovePointMode()) {
            return null;
        }
        return super.getWidget(x, y);
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance != null;
    }
    
    public void setOnMapClick(final MapClickListener l) {
        this.addEventListener(Events.MAP_CLICK, l, true);
    }
    
    public void setOnMapDoubleClick(final MapDoubleClickListener l) {
        this.addEventListener(Events.MAP_DOUBLE_CLICK, l, true);
    }
    
    public void setOnMapMove(final MapMoveListener l) {
        this.m_enableMapMoveEvent = true;
        this.addEventListener(Events.MAP_MOVE, l, true);
    }
    
    public DisplayableMapPoint getOverItem() {
        return (this.m_overItem == null) ? null : this.m_overItem.m_point;
    }
    
    public EntitySprite getOverMesh() {
        return (this.m_overItem == null) ? null : this.m_overItem.m_mesh;
    }
    
    public float getLandMarkZoom() {
        return this.m_landMarkZoom;
    }
    
    public void setLandMarkZoom(final float landMarkZoom) {
        this.m_landMarkZoom = landMarkZoom;
    }
    
    public boolean isUseAlternateTexture() {
        return this.m_useAlternateTexture;
    }
    
    public void setUseAlternateTexture(final boolean useAlternateTexture) {
        this.m_useAlternateTexture = useAlternateTexture;
    }
    
    protected void dontInterpolateNextRender() {
        this.m_dontInterpolateNextRender = true;
    }
    
    private boolean canInteractWith(final EntitySprite mesh, final int x, final int y) {
        if (mesh == null) {
            return false;
        }
        final int xScene = x - this.m_appearance.getLeftInset();
        final int yScene = y - this.m_appearance.getBottomInset();
        return this.getAppearance().insideInsets(xScene, yScene) && MapOverlayHelper.isHit(mesh, x, y);
    }
    
    public DisplayableMapPoint getItemUnderMouse(final MapOverlayPointList points, final ArrayList<DisplayableMapPoint> items, final int x, final int y) {
        final int index = this.indexOfItemUnderMouse(points, x, y);
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }
    
    private MapOverlayPoint getMapOverlayPointUnderMouse(final int x, final int y) {
        MapOverlayPoint point = this.m_displayCompass ? this.getMapOverlayPointUnderMouse(this.m_compass, x, y) : null;
        if (point != null) {
            return point;
        }
        point = (this.m_displayPoints ? this.getMapOverlayPointUnderMouse(this.m_items, x, y) : null);
        if (point != null) {
            return point;
        }
        return this.getMapOverlayPointUnderMouse(this.m_landMarks, x, y);
    }
    
    private MapOverlayPoint getMapOverlayPointUnderMouse(final MapOverlayPointList points, final int x, final int y) {
        final int index = this.indexOfItemUnderMouse(points, x, y);
        return points.getMapOverlayPoint(index);
    }
    
    private int indexOfItemUnderMouse(final MapOverlayPointList points, final int x, final int y) {
        for (int i = points.size() - 1; i >= 0; --i) {
            final EntitySprite mesh = points.getMesh(i);
            if (this.canInteractWith(mesh, x, y) && this.m_mapId != -1 && points.getPoint(i).getInstanceId() == this.m_mapId) {
                return i;
            }
        }
        return -1;
    }
    
    protected void itemOver(final MapOverlayPoint point) {
    }
    
    protected void itemOut(final MapOverlayPoint point) {
    }
    
    protected void itemClick(final int button, final MapOverlayPoint point) {
    }
    
    protected void mouseMovedIso(final int isoX, final int isoY) {
    }
    
    public Point2i getIsoMousePosition() {
        final int x = MouseManager.getInstance().getX() - this.getScreenX();
        final int y = MouseManager.getInstance().getY() - this.getScreenY();
        final Point2 iso = this.getMouseToIso(x, y);
        return new Point2i((int)iso.getX(), (int)iso.getY());
    }
    
    private void setOverItem(final MapOverlayPoint item) {
        if (this.m_overItem != null) {
            this.m_overItem.removeReference();
        }
        this.m_overItem = item;
        if (this.m_overItem != null) {
            this.m_overItem.addReference();
        }
    }
    
    public void updateOverMeshAndItemUnderMouse() {
        final int x = MouseManager.getInstance().getX() - this.getScreenX();
        final int y = MouseManager.getInstance().getY() - this.getScreenY();
        this.setOverItem(this.getMapOverlayPointUnderMouse(x, y));
    }
    
    void mouseMoved(final MouseEvent e) {
        final int x = e.getX(this);
        final int y = e.getY(this);
        final MapOverlayPoint overItem = this.getMapOverlayPointUnderMouse(x, y);
        if (this.m_overItem != overItem) {
            if (this.m_overItem != null) {
                this.itemOut(this.m_overItem);
                this.dispatchEvent(MapItemEvent.checkOut(e, this, Events.ITEM_OUT, this.m_overItem.m_point, this.m_overItem.m_mesh));
                this.setOverItem(null);
            }
            if (overItem != null) {
                this.itemOver(overItem);
                this.dispatchEvent(MapItemEvent.checkOut(e, this, Events.ITEM_OVER, overItem.m_point, overItem.m_mesh));
                this.setOverItem(overItem);
            }
        }
        final Point2 iso = this.getMouseToIso(x, y);
        this.mouseMovedIso((int)iso.getX(), (int)iso.getY());
        if (this.m_enableMapMoveEvent) {
            final MapEvent mapEvent = MapEvent.checkOut(e, iso.getX(), iso.getY(), (this.m_overItem != null) ? this.m_overItem.getValue() : null);
            mapEvent.setType(Events.MAP_MOVE);
            this.dispatchEvent(mapEvent);
        }
    }
    
    private Point2 getMouseToIso(final int x, final int y) {
        final int offsetIsoX = -this.m_appearance.getLeftInset() - this.m_appearance.getContentWidth() / 2;
        final int offsetIsoY = -this.m_appearance.getBottomInset() - this.m_appearance.getContentHeight() / 2;
        return this.m_coords.getMouseToIso(this.m_scene, x + offsetIsoX, y + offsetIsoY);
    }
    
    boolean mouseClicked(final MouseEvent e) {
        if (e.getType() != Events.MOUSE_CLICKED && e.getType() != Events.MOUSE_DOUBLE_CLICKED) {
            return false;
        }
        final int x = e.getX(this);
        final int y = e.getY(this);
        Events itemEventType;
        Events mapEventType;
        if (e.getType() == Events.MOUSE_CLICKED) {
            itemEventType = Events.ITEM_CLICK;
            mapEventType = Events.MAP_CLICK;
        }
        else {
            itemEventType = Events.ITEM_DOUBLE_CLICK;
            mapEventType = Events.MAP_DOUBLE_CLICK;
        }
        if (this.m_overItem != null) {
            final MapItemEvent ie = MapItemEvent.checkOut(e, this, itemEventType, this.m_overItem.m_point, this.m_overItem.m_mesh);
            this.dispatchEvent(ie);
            this.itemClick(e.getButton(), this.m_overItem);
        }
        final Point2 iso = this.getMouseToIso(x, y);
        this.mouseMovedIso((int)iso.getX(), (int)iso.getY());
        final MapEvent mapEvent = MapEvent.checkOut(e, iso.getX(), iso.getY(), (this.m_overItem != null) ? this.m_overItem.getValue() : null);
        mapEvent.setType(mapEventType);
        this.dispatchEvent(mapEvent);
        return false;
    }
    
    private void addListeners() {
        final EventListener mouseMoved = new EventListener() {
            @Override
            public boolean run(final Event event) {
                MapOverlay.this.mouseMoved((MouseEvent)event);
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_MOVED, mouseMoved, false);
        final EventListener mouseClickListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                return MapOverlay.this.mouseClicked((MouseEvent)event);
            }
        };
        this.addEventListener(Events.MOUSE_CLICKED, mouseClickListener, false);
        this.addEventListener(Events.MOUSE_DOUBLE_CLICKED, mouseClickListener, false);
    }
    
    public Point2i isoToScreen(final int isoX, final int isoY, final boolean useDesiredIsoCenter) {
        final Point2 point = this.m_coords.isoToScreen(this.m_scene, isoX, isoY, useDesiredIsoCenter);
        return new Point2i((int)point.getX(), (int)point.getY());
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_group.removeReference();
        this.m_group = null;
        this.m_items.clear();
        this.m_landMarks.clear();
        this.m_compass.clear();
        this.m_innerTooltip.onCheckIn();
        this.m_innerTooltip = null;
        this.setOverItem(null);
        this.m_particleManager.clear();
        this.m_scene.uninitialize();
        this.m_scene = null;
        this.clearPixmapReferences();
        if (this.m_mesh != null) {
            this.m_mesh.onCheckIn();
            this.m_mesh = null;
        }
    }
    
    protected final void computeImage() {
        if (!this.m_needsToComputeImage) {
            return;
        }
        if (this.m_appearance == null || this.m_appearance.getContentWidth() == 0 || this.m_appearance.getContentHeight() == 0) {
            return;
        }
        this.computeImageMesh();
        this.dontInterpolateNextRender();
        this.m_needsToComputeImage = false;
    }
    
    protected void computeImageMesh() {
        this.m_mesh.updateVertex(this.m_size, this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
    }
    
    protected final void clearPixmapReferences() {
        if (this.m_pixmap != null && this.m_pixmap.getTexture() != null) {
            this.m_pixmap.getTexture().removeReference();
        }
        this.m_pixmap = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        final RowLayout rl = RowLayout.checkOut();
        this.add(rl);
        assert this.m_group == null;
        this.m_group = EntityGroup.Factory.newPooledInstance();
        this.m_group.m_owner = this;
        this.m_group.getTransformer().addTransformer(new TransformerSRT());
        (this.m_innerTooltip = new ToolTipElement()).onCheckOut();
        this.m_coords.reset();
        this.m_enableTooltip = true;
        this.m_dontInterpolateNextRender = false;
        this.m_landMarkZoom = 1.0f;
        this.m_useAlternateTexture = false;
        this.addListeners();
        this.createScene();
        this.setNonBlocking(false);
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
    }
    
    @Override
    public void validate() {
        super.validate();
        this.m_coords.setCellNumber((int)(this.m_appearance.getContentWidth() / this.m_scene.getCellWidth()));
        this.m_needsToComputeImage = true;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        super.postProcess(deltaTime);
        this.m_scene.setVisible(false);
        this.m_scene.setFrustumSize(this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
        this.m_scene.getViewPort().setX(this.getDisplayX() + this.m_appearance.getLeftInset());
        this.m_scene.getViewPort().setY(this.getDisplayY() + this.m_appearance.getBottomInset());
        this.m_scene.process(deltaTime);
        return true;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final MapOverlay m = (MapOverlay)source;
        m.setIsoCenterX(this.getIsoCenterX());
        m.setIsoCenterY(this.getIsoCenterY());
        m.setIsoCenterZ(this.getIsoCenterZ());
        m.setIsoMap(this.m_isoMap);
        m.setMaxZoom(this.getMaxZoom());
        m.setMinSize(this.getMinSize());
        m.setTooltipHotPoint(this.getTooltipHotPoint());
        m.setZoomScale(this.getZoomScale());
        m.setEnableTooltip(this.getEnableTooltip());
        m.setMapId(this.m_mapId);
    }
    
    private void createScene() {
        assert this.m_scene == null;
        this.m_displayPoints = true;
        this.m_displayCompass = true;
        (this.m_scene = new IsoWorldScene(IsoWorldScene.DEFAULT_MIN_ZOOM_FACTOR, IsoWorldScene.DEFAULT_MAX_ZOOM_FACTOR) {
            @Override
            public void process(final int deltaTime) {
                final boolean dontInterpolateNextRender = MapOverlay.this.m_dontInterpolateNextRender;
                if (!MapOverlay.this.m_coords.isInitialized()) {
                    super.process(deltaTime);
                    return;
                }
                MapOverlay.this.m_group.removeAllChildren();
                final Point2 deltaCenter = MapOverlay.this.m_coords.computeCenter(this, dontInterpolateNextRender);
                final float halfScreenX = this.m_frustumWidth / 2.0f + MapOverlay.this.m_appearance.getLeftInset();
                final float halfScreenY = this.m_frustumHeight / 2.0f + MapOverlay.this.m_appearance.getBottomInset();
                this.displayPoints(MapOverlay.this.m_landMarks, halfScreenX, halfScreenY, deltaCenter, false, dontInterpolateNextRender);
                if (MapOverlay.this.m_displayPoints) {
                    this.displayPoints(MapOverlay.this.m_items, halfScreenX, halfScreenY, deltaCenter, false, dontInterpolateNextRender);
                }
                if (dontInterpolateNextRender) {
                    MapOverlay.this.m_dontInterpolateNextRender = false;
                }
                Collections.sort(MapOverlay.this.m_group.getChildList(), EntitySpriteYComparator.COMPARATOR);
                if (MapOverlay.this.m_displayCompass) {
                    this.displayPoints(MapOverlay.this.m_compass, halfScreenX, halfScreenY, deltaCenter, true, dontInterpolateNextRender);
                }
                super.process(deltaTime);
            }
            
            private void displayPoints(final MapOverlayPointList list, final float halfScreenX, final float halfScreenY, final Point2 deltaCenter, final boolean stickToScreen, final boolean dontInterpolateNextRender) {
                for (int i = 0, size = list.size(); i < size; ++i) {
                    final DisplayableMapPoint item = list.getPoint(i);
                    if (item != null) {
                        MapOverlay.this.displayPoint(this, item, list.getMesh(i), list.getOverlayMesh(i), halfScreenX, halfScreenY, deltaCenter, stickToScreen, dontInterpolateNextRender);
                    }
                }
            }
        }).setCellWidth(86.0f * this.m_zoom);
        this.m_scene.setCellHeight(43.0f * this.m_zoom);
        this.m_scene.setVisible(false);
    }
    
    public void setMapPath(final String path) {
        if (this.m_forceDisplayEntity) {
            return;
        }
        if (path == null) {
            return;
        }
        URL url;
        try {
            url = ContentFileHelper.getURL(path);
        }
        catch (MalformedURLException e1) {
            MapOverlay.m_logger.error((Object)("URL invalide : " + path));
            return;
        }
        XMLDocumentContainer doc;
        try {
            doc = Xulor.loadDoc(url);
        }
        catch (Exception e2) {
            MapOverlay.m_logger.error((Object)("Probl\u00e8me lors de la lecture du fichier de map d'url : " + url));
            return;
        }
        this.clearPixmapReferences();
        this.m_originX = 0.0f;
        this.m_originY = 0.0f;
        this.m_mapChunkWidth = 0.0f;
        this.m_mapChunkHeight = 0.0f;
        this.setBaseMapDisplayer();
        final ArrayList<? extends DocumentEntry> children = doc.getRootNode().getChildren();
        for (int size = children.size(), i = 0; i < size; ++i) {
            final DocumentEntry child = (DocumentEntry)children.get(i);
            final TextureInfo textureInfo = TextureInfo.create(child);
            if (textureInfo != null) {
                this.m_mapChunkWidth = (float)textureInfo.m_isoWidth;
                this.m_mapChunkHeight = (float)textureInfo.m_isoHeight;
                this.m_originX = (float)textureInfo.m_isoX;
                this.m_originY = (float)textureInfo.m_isoY;
                final Texture texture = TextureInfo.createTexture((DocumentEntry)children.get(i), url);
                this.createMaskTexture(path, url, child);
                if (texture != null) {
                    this.createPixmap(textureInfo, texture);
                }
                this.m_mesh.setMapSize((int)this.m_mapChunkWidth, (int)this.m_mapChunkHeight);
                this.setMeshCenter();
            }
        }
        this.endSetPath();
    }
    
    protected void endSetPath() {
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
    }
    
    protected void setMeshCenter() {
    }
    
    protected void createPixmap(final TextureInfo textureInfo, final Texture texture) {
        this.m_pixmap = textureInfo.createPixmap(texture);
    }
    
    protected abstract void createMaskTexture(final String p0, final URL p1, final DocumentEntry p2);
    
    protected abstract void setBaseMapDisplayer();
    
    public void setForceDisplayEntity(final boolean force) {
        this.m_forceDisplayEntity = force;
    }
    
    public void setMapRect(final int minX, final int minY, final int width, final int height) {
        assert this.m_forceDisplayEntity;
        this.m_originX = minX;
        this.m_originY = minY;
        this.m_mapChunkWidth = width;
        this.m_mapChunkHeight = height;
        this.m_mesh.setMapSize(width, height);
        this.clearPixmapReferences();
    }
    
    public void setMapDisplayer(final MapDisplayer displayer) {
        this.setForceDisplayEntity(true);
        this.m_mesh.setMapDisplayer(displayer);
        this.endSetPath();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == MapOverlay.MAP_PATH_HASH) {
            this.setMapPath(value);
        }
        else if (hash == MapOverlay.ISO_CENTER_X_HASH) {
            this.setIsoCenterX(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.ISO_CENTER_Y_HASH) {
            this.setIsoCenterY(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.ISO_CENTER_Z_HASH) {
            this.setIsoCenterZ(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.ISO_MAP_HASH) {
            this.setIsoMap(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MapOverlay.ON_MAP_CLICK_HASH) {
            this.setOnMapClick(cl.convert(MapClickListener.class, value));
        }
        else if (hash == MapOverlay.ON_MAP_DOUBLE_CLICK_HASH) {
            this.setOnMapDoubleClick(cl.convert(MapDoubleClickListener.class, value));
        }
        else if (hash == MapOverlay.ON_MAP_MOVE_HASH) {
            this.setOnMapMove(cl.convert(MapMoveListener.class, value));
        }
        else if (hash == MapOverlay.MAX_ZOOM_HASH) {
            this.setMaxZoom(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.MIN_ZOOM_HASH) {
            this.setMinZoom(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.TOOLTIP_HOT_POINT_HASH) {
            this.setTooltipHotPoint(BackgroundedTextHotPointPosition.value(value));
        }
        else if (hash == MapOverlay.ENABLE_TOOLTIP_HASH) {
            this.setEnableTooltip(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MapOverlay.LANDMARK_ZOOM_HASH) {
            this.setLandMarkZoom(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != MapOverlay.ZOOM_SCALE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setZoomScale(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == MapOverlay.MAP_PATH_HASH) {
            this.setMapPath(PrimitiveConverter.getString(value));
        }
        else if (hash == MapOverlay.MAP_ID_HASH) {
            this.setMapId(PrimitiveConverter.getShort(value));
        }
        else if (hash == MapOverlay.CONTENT_HASH) {
            this.setList(this.m_items, (ArrayList<DisplayableMapPoint>)value);
        }
        else if (hash == MapOverlay.COMPASS_CONTENT_HASH) {
            this.setList(this.m_compass, (ArrayList<DisplayableMapPoint>)value);
        }
        else if (hash == MapOverlay.LANDMARK_CONTENT_HASH) {
            this.setList(this.m_landMarks, (ArrayList<DisplayableMapPoint>)value);
        }
        else if (hash == MapOverlay.ISO_CENTER_X_HASH) {
            this.setIsoCenterX(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.ISO_CENTER_Y_HASH) {
            this.setIsoCenterY(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.ISO_CENTER_Z_HASH) {
            this.setIsoCenterZ(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.ISO_MAP_HASH) {
            this.setIsoMap(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MapOverlay.MAX_ZOOM_HASH) {
            this.setMaxZoom(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.MIN_ZOOM_HASH) {
            this.setMinZoom(PrimitiveConverter.getFloat(value));
        }
        else if (hash == MapOverlay.TOOLTIP_HOT_POINT_HASH) {
            this.setTooltipHotPoint((BackgroundedTextHotPointPosition)value);
        }
        else if (hash == MapOverlay.ENABLE_TOOLTIP_HASH) {
            this.setEnableTooltip(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == MapOverlay.ZOOM_SCALE_HASH) {
            this.setZoomScale(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != MapOverlay.LANDMARK_ZOOM_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setLandMarkZoom(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    static {
        (COLOR_SCALE_PARAMS = new EffectParams(new Variable[0])).setFloat("gColorScale", 1.0f);
        CONTENT_HASH = "content".hashCode();
        COMPASS_CONTENT_HASH = "compassContent".hashCode();
        LANDMARK_CONTENT_HASH = "landMarkContent".hashCode();
        ISO_CENTER_X_HASH = "isoCenterX".hashCode();
        ISO_CENTER_Y_HASH = "isoCenterY".hashCode();
        ISO_CENTER_Z_HASH = "isoCenterZ".hashCode();
        ISO_MAP_HASH = "isoMap".hashCode();
        MAX_ZOOM_HASH = "maxZoom".hashCode();
        MIN_ZOOM_HASH = "minZoom".hashCode();
        TOOLTIP_HOT_POINT_HASH = "tooltipHotPoint".hashCode();
        ZOOM_SCALE_HASH = "zoomScale".hashCode();
        ON_MAP_CLICK_HASH = "onMapClick".hashCode();
        ON_MAP_DOUBLE_CLICK_HASH = "onMapDoubleClick".hashCode();
        ON_MAP_MOVE_HASH = "onMapMove".hashCode();
        ENABLE_TOOLTIP_HASH = "enableTooltip".hashCode();
        LANDMARK_ZOOM_HASH = "landMarkZoom".hashCode();
        MAP_PATH_HASH = "mapPath".hashCode();
        MAP_ID_HASH = "mapId".hashCode();
    }
    
    public enum MapShape
    {
        LOSANGE, 
        RECTANGLE, 
        CIRCLE, 
        SQUARE;
        
        public static MapShape value(final String value) {
            final MapShape[] arr$;
            final MapShape[] values = arr$ = values();
            for (final MapShape a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
    }
}
