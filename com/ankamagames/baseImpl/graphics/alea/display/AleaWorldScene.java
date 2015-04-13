package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.kernel.core.controllers.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.isometric.tween.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.sound.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.alea.rendertreee.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.baseImpl.graphics.alea.display.occlusion.*;
import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import java.awt.event.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.framework.kernel.core.common.*;

public abstract class AleaWorldScene extends IsoWorldScene implements MouseController, KeyboardController, FocusController
{
    private static final Logger m_logger;
    public static final String DEFAULT_GFX_PATH = "contents/gfx";
    public static final String DEFAULT_SND_PATH = "contents/sounds";
    protected String m_gfxPath;
    protected String m_sndPath;
    private int m_mouseX;
    private int m_mouseY;
    private static float DEFAULT_OUTDOOR_ZOOM_FACTOR;
    protected static float DEFAULT_INDOOR_ZOOM_FACTOR;
    private float m_outdoorZoomFactor;
    private float m_indoorZoomFactor;
    protected final ArrayList<RenderProcessHandler> m_renderProcessHandlers;
    private boolean m_forceUpdateDisplayCell;
    private final Point3 m_lastCameraPosition;
    private final Matrix44 m_cameraTransformer;
    protected RenderTreeInterface m_renderTree;
    protected final DisplayedScreenWorld m_displayedScreenWorld;
    protected boolean m_displayInitialized;
    private boolean m_isInitialized;
    private boolean m_useSymmetry;
    protected final Color m_backgroundColor;
    protected boolean m_backgroundColorChanged;
    private final Matrix44 _matrixCameraTransformer;
    private final Quaternion _quaternionCameraTransformer;
    private TopologyMapInstance m_topologyMap;
    private boolean m_inGroup;
    final ElementPicker m_elementPicker;
    final ElementPicker m_elementPickerEmpty;
    
    public AleaWorldScene(final float minZoom, final float maxZoom) {
        this(null, DisplayedScreenWorld.getInstance(), minZoom, maxZoom);
        this.resetRenderTree();
    }
    
    protected AleaWorldScene(final RenderTreeInterface renderTree, final DisplayedScreenWorld displayedScreenWorld, final float minZoom, final float maxZoom) {
        super(minZoom, maxZoom);
        this.m_gfxPath = "contents/gfx";
        this.m_sndPath = "contents/sounds";
        this.m_outdoorZoomFactor = AleaWorldScene.DEFAULT_OUTDOOR_ZOOM_FACTOR;
        this.m_indoorZoomFactor = AleaWorldScene.DEFAULT_INDOOR_ZOOM_FACTOR;
        this.m_renderProcessHandlers = new ArrayList<RenderProcessHandler>();
        this.m_lastCameraPosition = new Point3(Integer.MIN_VALUE, Integer.MIN_VALUE, (short)(-32768));
        this.m_cameraTransformer = Matrix44.Factory.create();
        this.m_backgroundColor = new Color();
        this._matrixCameraTransformer = Matrix44.Factory.newInstance();
        this._quaternionCameraTransformer = new Quaternion();
        this.m_elementPicker = new ElementPicker();
        this.m_elementPickerEmpty = new ElementPicker();
        this.addRenderProcessHandler(GroupLayerManager.getInstance());
        this.addRenderProcessHandler(MobileManager.getInstance());
        this.addRenderProcessHandler(IsoParticleSystemManager.getInstance());
        this.addRenderProcessHandler(HighLightManager.getInstance());
        this.addRenderProcessHandler(TweenManager.getInstance());
        this.addRenderProcessHandler(AdviserManager.getInstance());
        this.m_renderTree = renderTree;
        this.m_displayedScreenWorld = displayedScreenWorld;
    }
    
    @Override
    protected AbstractCamera createCamera() {
        return new AleaIsoCamera(this, this.m_minZoom, this.m_maxZoom) {};
    }
    
    @Override
    public final AleaIsoCamera getIsoCamera() {
        return (AleaIsoCamera)super.getIsoCamera();
    }
    
    public void setForceUpdateDisplayCell(final boolean forceUpdateDisplayCell) {
        this.m_forceUpdateDisplayCell = forceUpdateDisplayCell;
    }
    
    public void setGfxPath(final String gfxPath) {
        this.m_gfxPath = gfxPath;
        AleaTextureManager.getInstance().setGfxPath(gfxPath);
    }
    
    public void initSoundBank(final String path) {
        SoundBank.getInstance().setFile(path);
        SoundBank.getInstance().load();
    }
    
    public int getMouseX() {
        return this.m_mouseX;
    }
    
    public int getMouseY() {
        return this.m_mouseY;
    }
    
    public void addRenderProcessHandler(final RenderProcessHandler renderProcessHandler) {
        this.m_renderProcessHandlers.add(renderProcessHandler);
    }
    
    public void removeRenderProcessHandler(final RenderProcessHandler renderProcessHandler) {
        this.m_renderProcessHandlers.remove(renderProcessHandler);
    }
    
    @Override
    public void uninitialize() {
        this.clean(true);
        super.uninitialize();
    }
    
    public void prepareBeforeFade() {
        final HashSet<Entity> entities = new HashSet<Entity>();
        this.m_renderTree.getAddEntities(entities);
        for (final Entity e : entities) {
            e.addReference();
        }
    }
    
    public void cleanAfterFade() {
        final HashSet<Entity> entities = new HashSet<Entity>();
        this.m_renderTree.getAddEntities(entities);
        for (final Entity e : entities) {
            e.removeReference();
        }
        this.m_renderTree.clear();
        super.uninitialize();
    }
    
    public void clean(final boolean forceUpdate) {
        this.m_cameraTransformer.setIdentity();
        this.setInitialized(false);
        this.setBackgoundColor(Color.BLACK);
        this.m_renderTree.clear();
        this.cleanManagers();
        this.m_displayedScreenWorld.clear();
        this.setForceUpdateDisplayCell(true);
        this.m_sortedEntities.clear();
        this.m_unsortedEntities.clear();
        this.m_displayInitialized = false;
        synchronized (this.m_elementPicker) {
            this.m_elementPicker.reset();
        }
    }
    
    protected void cleanManagers() {
        HiddenElementManager.getInstance().clear();
        MobileManager.getInstance().removeAllMobiles();
        IsoParticleSystemManager.getInstance().clearParticleSystems();
        AdviserManager.getInstance().clear();
        IsoSceneLightManager.INSTANCE.reset();
        AmbianceManager.INSTANCE.clear();
        EffectManager.getInstance().clearWorldEffects();
    }
    
    @Override
    public void init(final GLAutoDrawable glAutoDrawable) {
        this.m_isInitialized = true;
        super.init(glAutoDrawable);
        this.initializeCells();
    }
    
    @Override
    public void setFrustumSize(final int frustumWidth, final int frustumHeight) {
        if (this.m_frustumWidth != frustumWidth || this.m_frustumHeight != frustumHeight) {
            super.setFrustumSize(frustumWidth, frustumHeight);
        }
        this.m_isoCamera.setScreenResolution(frustumWidth, frustumHeight);
    }
    
    public void initializeCells() {
        if (this.m_isoCamera != null) {
            this.m_displayInitialized = false;
        }
    }
    
    @Override
    public void alignCameraOnTrackingTarget() {
        super.alignCameraOnTrackingTarget();
        this.m_displayInitialized = false;
    }
    
    private void processTreeSort() {
        this.m_renderTree.clear();
        for (int numberOfEntities = this.m_entityList.size(), i = 0; i < numberOfEntities; ++i) {
            this.m_renderTree.push(this.m_entityList.get(i), 0);
        }
        if (this.m_renderTree instanceof RenderTreeStencil) {
            RenderTreeDebug.INSTANCE.setRenderTree((RenderTreeStencil)this.m_renderTree);
        }
    }
    
    @Override
    public void process(final int deltaTime) {
        if (!this.processBegin(deltaTime)) {
            return;
        }
        super.process(deltaTime);
        final IsoCamera isoCamera = this.getIsoCamera();
        isoCamera.process(deltaTime);
        final int centerScreenIsoWorldX = (int)Math.floor(isoCamera.getCameraExactIsoWorldX());
        final int centerScreenIsoWorldY = (int)Math.floor(isoCamera.getCameraExactIsoWorldY());
        MapManagerHelper.LoadMapsAroundCellPosition(centerScreenIsoWorldX, centerScreenIsoWorldY, 2);
        for (int processHandlersSize = this.m_renderProcessHandlers.size(), i = 0; i < processHandlersSize; ++i) {
            this.m_renderProcessHandlers.get(i).process(this, deltaTime);
        }
        this.updateDisplayedCell(this.m_forceUpdateDisplayCell);
        this.prepareBeforeRendering(deltaTime);
        this.updateLigthing(deltaTime);
        this.sortAndInsert();
        this.processTreeSort();
        this.computeMasks();
        for (int i = 0; i < this.m_unsortedEntities.size(); ++i) {
            this.m_renderTree.pushAtEnd(this.m_unsortedEntities.get(i));
        }
        this.m_sortedEntities.clear();
        this.m_unsortedEntities.clear();
        EffectManager.getInstance().update(deltaTime);
    }
    
    protected final void sortAndInsert() {
        this.m_zList.resetQuick();
        for (int size = Math.min(this.m_sortedEntities.size(), 32766), i = 0; i < size; ++i) {
            final Entity e = this.m_sortedEntities.get(i);
            if (e.getNumReferences() >= 0) {
                final long z = e.m_zOrder;
                LayerOrder.checkSortable(z);
                this.m_zList.add((z << 15) + i);
            }
        }
        this.m_zList.sort();
        this.m_entityList.clear();
        for (int zListSize = this.m_zList.size(), j = 0; j < zListSize; ++j) {
            final int index = (int)(this.m_zList.getQuick(j) & 0x7FFFL);
            assert index >= 0;
            final Entity entity = this.m_sortedEntities.get(index);
            this.insert(entity);
        }
    }
    
    protected void insert(final Entity entity) {
        this.m_entityList.add(entity);
    }
    
    protected final void prepareBeforeRendering(final int deltaTime) {
        this.m_displayedScreenWorld.update(this, deltaTime);
        this.prepareSceneBeforeRendering(this.m_isoCamera.getCameraExactIsoWorldX(), this.m_isoCamera.getCameraExactIsoWorldY());
    }
    
    protected final boolean processBegin(final int deltaTime) {
        if (!this.isInitialized()) {
            this.m_entityList.clear();
            return false;
        }
        if (this.m_isoCamera == null) {
            return false;
        }
        if (FadeManager.getInstance().isFadeIn()) {
            return false;
        }
        if (FadeManager.getInstance().isDisabledIn()) {
            this.m_isoCamera.process(deltaTime);
            this.updateDisplayedCell(this.m_forceUpdateDisplayCell);
            return false;
        }
        return true;
    }
    
    protected void updateLigthing(final int deltaTime) {
        IsoSceneLightManager.INSTANCE.update(this, deltaTime);
    }
    
    private void computeMasks() {
        final OcclusionProcess occlusionProcess = OcclusionProcess.getCurrent();
        occlusionProcess.reset();
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        this.setCameraMatrix(renderer);
        occlusionProcess.selectOccluder(this.m_entityList);
        occlusionProcess.compute(renderer.getCameraMatrix(), this.m_isoCamera);
        occlusionProcess.onDone(this);
    }
    
    protected final void setScene(final GL gl) {
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        renderer.m_stateManager.setVertexArrayComponents(0);
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        stateManager.enableBlend(true);
        stateManager.setColorScale(1.0f);
        stateManager.applyMatrixMode(gl, MatrixModes.TEXTURE, Matrix44.IDENTITY);
        stateManager.applyViewportAndOrtho2D(gl, this.getViewPort());
        this.setCameraMatrix(renderer);
        RenderStateManager.getInstance().reset();
        EffectManager.getInstance().resetAllEffects();
    }
    
    private void setCameraMatrix(final Renderer renderer) {
        if (!FadeManager.getInstance().isFadeIn()) {
            final AbstractCamera camera = this.getCam();
            final float s = camera.getZoomResolutionFactor();
            final float tx = -camera.getScreenFloatX() * s;
            final float ty = -camera.getScreenFloatY() * s;
            this.m_cameraTransformer.setIdentity();
            if (this.m_useSymmetry) {
                this.m_cameraTransformer.setScale(-s, s, 1.0f);
                this.m_cameraTransformer.setTranslation(-tx, ty, 0.0f);
            }
            else {
                this.m_cameraTransformer.setScale(s, s, 1.0f);
                this.m_cameraTransformer.setTranslation(tx, ty, 0.0f);
            }
            this._quaternionCameraTransformer.set(Vector3.AXIS_Z, camera.getRotationZ());
            this._matrixCameraTransformer.set(this._quaternionCameraTransformer);
            this.m_cameraTransformer.setMultiply(this._matrixCameraTransformer);
        }
        renderer.setCameraMatrix(this.m_cameraTransformer);
    }
    
    protected void drawAllWithoutEffect(final GLRenderer renderer) {
        this.setCameraMatrix(renderer);
    }
    
    @Override
    public void display(final GL gl) {
        if (!this.m_isInitialized) {
            return;
        }
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        this.setScene(gl);
        this.drawAllWithoutEffect(renderer);
        this.renderEffect(renderer);
        if (this.displayWalkabilityEnabled()) {
            displayWalkability((int)this.m_isoCamera.getCameraExactIsoWorldX(), (int)this.m_isoCamera.getCameraExactIsoWorldY());
        }
    }
    
    protected void renderEffect(final GLRenderer renderer) {
        EffectManager.getInstance().render(this.m_renderTree, renderer);
    }
    
    private void updateGroupDisplay(final AleaIsoCamera isoCamera) {
        final boolean inGroup = this.cameraIsIndoor(isoCamera);
        if (this.m_inGroup == inGroup) {
            return;
        }
        this.onInGroupChanged(inGroup);
        this.m_inGroup = inGroup;
    }
    
    protected void onInGroupChanged(final boolean inGroup) {
    }
    
    private boolean cameraIsIndoor(final AleaIsoCamera isoCamera) {
        if (isoCamera.isLocked()) {
            return this.m_inGroup;
        }
        final int cameraX = isoCamera.getWorldCellX();
        final int cameraY = isoCamera.getWorldCellY();
        int targetAltitude = 0;
        if (this.getCameraTarget() != null) {
            targetAltitude = (int)Math.ceil(this.getCameraTarget().getAltitude() + 0.5f);
        }
        if (this.m_lastCameraPosition.equals(cameraX, cameraY, targetAltitude) && this.m_displayedScreenWorld.isReadyAndDisplay()) {
            return this.m_inGroup;
        }
        this.m_lastCameraPosition.set(cameraX, cameraY, (short)targetAltitude);
        final DisplayedScreenElement walkableElement = AleaIsoCamera.getDisplayedScreenElement(this.m_displayedScreenWorld, cameraX, cameraY, targetAltitude);
        if (walkableElement == null) {
            isoCamera.resetMaskKey();
            return false;
        }
        final int x = walkableElement.getWorldCellX();
        final int y = walkableElement.getWorldCellY();
        final int maskKey = walkableElement.getMaskKey();
        isoCamera.setMaskKey(maskKey, walkableElement.getGroupId());
        boolean inGroup = false;
        if (maskKey == 0) {
            inGroup = false;
        }
        else {
            if (this.m_topologyMap == null || !this.m_topologyMap.getTopologyMap().isInMap(x, y)) {
                this.m_topologyMap = TopologyMapManager.getMapFromCell(x, y);
            }
            if (this.m_topologyMap == null) {
                AleaWorldScene.m_logger.error((Object)("pas de map topologique aux coordonn\u00e9es " + x + " " + y));
                return this.m_inGroup;
            }
            final short z = walkableElement.getWorldCellAltitude();
            inGroup = this.m_topologyMap.isIndoor(x, y, z);
        }
        return inGroup;
    }
    
    public void updateDisplayedCell(final boolean forceUpdate) {
        final AleaIsoCamera isoCamera = this.getIsoCamera();
        if (!this.m_displayInitialized) {
            this.m_displayInitialized = true;
        }
        this.m_forceUpdateDisplayCell = false;
        this.updateGroupDisplay(isoCamera);
        this.m_displayedScreenWorld.update(isoCamera.getScreenBounds());
    }
    
    protected void prepareSceneBeforeRendering(final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        for (int renderProcessHandlersCount = this.m_renderProcessHandlers.size(), i = 0; i < renderProcessHandlersCount; ++i) {
            this.m_renderProcessHandlers.get(i).prepareBeforeRendering(this, centerScreenIsoWorldX, centerScreenIsoWorldY);
        }
        try {
            this.m_displayedScreenWorld.addToScene(this);
        }
        catch (Exception ex) {}
    }
    
    public ArrayList<DisplayedScreenElement> getDisplayedElementsUnderMousePoint(final float mouseX, final float mouseY, final float altitude, final DisplayedScreenElementComparator comparator) {
        return this.getDisplayedElementsUnderMousePoint(mouseX, mouseY, altitude, comparator, 0.0f, 0.0f);
    }
    
    public ArrayList<DisplayedScreenElement> getDisplayedElementsUnderMousePoint(final float mouseX, final float mouseY, final float altitude, final DisplayedScreenElementComparator comparator, final float offsetScreenX, final float offsetScreenY) {
        if (this.m_isoCamera == null) {
            return null;
        }
        final int adjustedMouseX = (int)(this.getAdjustedMouseX(mouseX) + this.m_isoCamera.getScreenX() - offsetScreenX);
        final int adjustedMouseY = (int)(this.getAdjustedMouseY(mouseY) + this.m_isoCamera.getScreenY() - offsetScreenY);
        final ArrayList<DisplayedScreenElement> hitElements = new ArrayList<DisplayedScreenElement>();
        this.m_displayedScreenWorld.getDisplayedElements(adjustedMouseX, adjustedMouseY, hitElements);
        if (comparator.isRemoveTooFar()) {
            int i = 0;
            while (i < hitElements.size()) {
                final DisplayedScreenElement elt = hitElements.get(i);
                final float hitDistance = (elt.m_element.getCommonProperties().getSlope() == 0) ? 0.55f : 1.0f;
                if (this.distanceToCenter(adjustedMouseX, adjustedMouseY, elt) > hitDistance) {
                    hitElements.remove(i);
                }
                else {
                    ++i;
                }
            }
        }
        comparator.sort(hitElements, new DisplayedScreenElementParameteredComparator.Parameters(this, altitude, adjustedMouseX, adjustedMouseY));
        return hitElements;
    }
    
    private float distanceToCenter(final int adjustedMouseX, final int adjustedMouseY, final DisplayedScreenElement element) {
        final int cellX = element.getWorldCellX();
        final int cellY = element.getWorldCellY();
        final float screenX = this.isoToScreenX(cellX, cellY);
        final float screenY = this.isoToScreenY(cellX, cellY, element.getWorldCellAltitude());
        final float dX = (adjustedMouseX - screenX) / this.m_cellWidth;
        final float dY = (adjustedMouseY - screenY) / this.m_cellHeight;
        return Math.abs(dX) + Math.abs(dY);
    }
    
    protected final float getAdjustedMouseX(final float mouseX) {
        if (this.m_useSymmetry) {
            return (-mouseX + this.m_frustumWidth * 0.5f) / this.m_isoCamera.getZoomResolutionFactor();
        }
        return (mouseX - this.m_frustumWidth * 0.5f) / this.m_isoCamera.getZoomResolutionFactor();
    }
    
    protected final float getAdjustedMouseY(final float mouseY) {
        return (this.m_frustumHeight * 0.5f - mouseY) / this.m_isoCamera.getZoomResolutionFactor();
    }
    
    public ArrayList<Mobile> getMobilesUnderMousePoint(final float mouseX, final float mouseY) {
        if (this.m_isoCamera == null) {
            return null;
        }
        return MobileManager.getInstance().getElementUnderMousePoint(this.getAdjustedMouseX(mouseX), this.getAdjustedMouseY(mouseY));
    }
    
    @Override
    public abstract boolean mouseClicked(final MouseEvent p0);
    
    @Override
    public abstract boolean mousePressed(final MouseEvent p0);
    
    @Override
    public abstract boolean mouseReleased(final MouseEvent p0);
    
    @Override
    public abstract boolean mouseEntered(final MouseEvent p0);
    
    @Override
    public abstract boolean mouseExited(final MouseEvent p0);
    
    @Override
    public boolean mouseDragged(final MouseEvent mouseEvent) {
        this.m_mouseX = mouseEvent.getX();
        this.m_mouseY = mouseEvent.getY();
        return false;
    }
    
    @Override
    public boolean mouseMoved(final MouseEvent mouseEvent) {
        this.m_mouseX = mouseEvent.getX();
        this.m_mouseY = mouseEvent.getY();
        return false;
    }
    
    @Override
    public abstract boolean mouseWheelMoved(final MouseWheelEvent p0);
    
    @Override
    public abstract boolean keyTyped(final KeyEvent p0);
    
    @Override
    public abstract boolean keyPressed(final KeyEvent p0);
    
    @Override
    public abstract boolean keyReleased(final KeyEvent p0);
    
    @Override
    public String toString() {
        return "zoom=" + this.m_isoCamera.getZoomResolutionFactor() + ", " + super.toString();
    }
    
    @Override
    public boolean initialize(final Maskable maskable, @NotNull final Entity entity, final int x, final int y, final int z, final int deltaZ, final boolean canBeSetOnEmptyElement) {
        synchronized (this.m_elementPicker) {
            final int altitudeOrder = this.m_elementPicker.getAltitudeOrder(this.m_displayedScreenWorld, x, y, z);
            entity.m_zOrder = LayerOrder.computeZOrder(x, y, altitudeOrder, deltaZ);
            final DisplayedScreenElement elt = this.m_elementPicker.getElement(this.m_displayedScreenWorld, x, y, z);
            MaskableHelper.set(maskable, elt);
            return elt != null;
        }
    }
    
    public final int getGroupId(final int x, final int y, final int z) {
        synchronized (this.m_elementPicker) {
            final DisplayedScreenElement element = this.m_elementPicker.getElement(this.m_displayedScreenWorld, x, y, z);
            return (element == null) ? 0 : element.getMaskKey();
        }
    }
    
    @Override
    public abstract boolean focusGained(final FocusEvent p0);
    
    @Override
    public abstract boolean focusLost(final FocusEvent p0);
    
    public void setBackgoundColor(final Color backGroundColor) {
        if (this.m_backgroundColor.get() == backGroundColor.get()) {
            return;
        }
        this.m_backgroundColor.set(backGroundColor.get());
        this.m_backgroundColorChanged = true;
    }
    
    public boolean isWorldReady() {
        return this.m_displayedScreenWorld.isReady();
    }
    
    public final void forceSetZoom() {
        final AleaIsoCamera isoCamera = this.getIsoCamera();
        this.m_inGroup = this.cameraIsIndoor(isoCamera);
    }
    
    public DisplayedScreenWorld getDisplayedScreenWorld() {
        return this.m_displayedScreenWorld;
    }
    
    public final void resetRenderTree() {
        this.m_renderTree = ((MemoryObject.ObjectFactory<RenderTreeInterface>)RenderTreeStencil.Factory).newInstance();
    }
    
    public final AleaWorldScene duplicate() {
        this.process(0);
        final AleaWorldScene scene = this.createNew(this.m_renderTree, this.m_minZoom, this.m_maxZoom);
        scene.copyFrom(this);
        scene.prepareBeforeFade();
        return scene;
    }
    
    protected abstract AleaWorldScene createNew(final RenderTreeInterface p0, final float p1, final float p2);
    
    protected void copyFrom(final AleaWorldScene scene) {
        this.m_cameraTransformer.set(scene.m_cameraTransformer);
        this.m_isInitialized = scene.m_isInitialized;
        this.m_backgroundColor.set(scene.m_backgroundColor.getABGR());
        this.m_backgroundColorChanged = scene.m_backgroundColorChanged;
        this.setFrustumSize((int)scene.getFrustumWidth(), (int)scene.getFrustumHeight());
        this.m_entityList.addAll(scene.m_entityList);
        this.m_zList.add(scene.m_zList.toNativeArray());
    }
    
    public final void enableDisplayWalkability(final boolean enable) {
        DebugHelper.enableDisplayWalkability(enable);
    }
    
    public final boolean displayWalkabilityEnabled() {
        return DebugHelper.displayWalkabilityEnabled();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AleaWorldScene.class);
        AleaWorldScene.DEFAULT_OUTDOOR_ZOOM_FACTOR = 1.1f;
        AleaWorldScene.DEFAULT_INDOOR_ZOOM_FACTOR = 1.4f;
    }
    
    private static class DebugHelper
    {
        private static final Logger m_logger;
        private static final CellPathData[] m_cellPathData;
        private static final String WALKABLE_LAYER_NAME = "debug_walkable";
        private static final String NON_WALKABLE_LAYER_NAME = "debug_non_walkable";
        private static final String MOBO_STERYL_LAYER_NAME = "debug_mobosteryl";
        private static boolean m_enableDisplayWalkability;
        
        public static void enableDisplayWalkability(final boolean enable) {
            DebugHelper.m_enableDisplayWalkability = enable;
            try {
                if (DebugHelper.m_enableDisplayWalkability) {
                    final HighLightLayer layer0 = HighLightManager.getInstance().createLayer("debug_walkable");
                    layer0.setColor(new float[] { 0.1f, 0.7f, 0.1f, 0.8f });
                    final HighLightLayer layer = HighLightManager.getInstance().createLayer("debug_non_walkable");
                    layer.setColor(new float[] { 0.7f, 0.1f, 0.1f, 0.8f });
                    final HighLightLayer layer2 = HighLightManager.getInstance().createLayer("debug_mobosteryl");
                    layer2.setColor(new float[] { 0.6f, 0.7f, 0.1f, 0.8f });
                }
                else {
                    HighLightManager.getInstance().removeLayer("debug_walkable");
                    HighLightManager.getInstance().removeLayer("debug_non_walkable");
                    HighLightManager.getInstance().removeLayer("debug_mobosteryl");
                }
            }
            catch (Exception e) {
                DebugHelper.m_logger.error((Object)"", (Throwable)e);
            }
        }
        
        public static boolean displayWalkabilityEnabled() {
            return DebugHelper.m_enableDisplayWalkability;
        }
        
        private static void displayWalkability(final int cellCenterX, final int cellCenterY) {
            assert DebugHelper.m_enableDisplayWalkability;
            final int SIZE = 18;
            HighLightManager.getInstance().clearLayer("debug_walkable");
            HighLightManager.getInstance().clearLayer("debug_non_walkable");
            HighLightManager.getInstance().clearLayer("debug_mobosteryl");
            for (int cellX = cellCenterX - 18; cellX < cellCenterX + 18; ++cellX) {
                for (int cellY = cellCenterY - 18; cellY < cellCenterY + 18; ++cellY) {
                    final TopologyMapInstance mapInstance = TopologyMapManager.getMapFromCell(cellX, cellY);
                    if (mapInstance != null) {
                        for (int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, DebugHelper.m_cellPathData, 0), i = 0; i < numZ; ++i) {
                            if (DebugHelper.m_cellPathData[i].m_z != -32768) {
                                final long handle = HighLightManager.getHandle(cellX, cellY, DebugHelper.m_cellPathData[i].m_z);
                                if (DebugHelper.m_cellPathData[i].m_hollow) {
                                    HighLightManager.getInstance().add(handle, "debug_non_walkable");
                                }
                                else {
                                    final boolean blocked = mapInstance.isBlocked(cellX, cellY, DebugHelper.m_cellPathData[i].m_z);
                                    if (blocked || DebugHelper.m_cellPathData[i].m_cost == -1) {
                                        HighLightManager.getInstance().add(handle, "debug_non_walkable");
                                    }
                                    else if (DebugHelper.m_cellPathData[i].isMoboSteryl()) {
                                        HighLightManager.getInstance().add(handle, "debug_mobosteryl");
                                    }
                                    else {
                                        HighLightManager.getInstance().add(handle, "debug_walkable");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        static {
            m_logger = Logger.getLogger((Class)DebugHelper.class);
            m_cellPathData = CellPathData.createCellPathDataTab();
        }
    }
}
