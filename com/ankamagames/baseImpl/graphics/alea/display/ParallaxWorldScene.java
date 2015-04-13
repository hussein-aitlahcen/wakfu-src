package com.ankamagames.baseImpl.graphics.alea.display;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.*;
import java.awt.event.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.client.core.world.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.*;
import com.ankamagames.framework.kernel.core.common.*;

public class ParallaxWorldScene extends AleaWorldScene
{
    private static final Logger m_logger;
    private short m_worldId;
    private boolean m_background;
    private AleaWorldSceneWithParallax m_refScene;
    
    private ParallaxWorldScene(final RenderTreeInterface renderTree, final float minZoom, final float maxZoom) {
        super(renderTree, null, minZoom, maxZoom);
        this.m_worldId = -1;
        this.m_background = true;
        this.setForceUpdateDisplayCell(false);
    }
    
    public ParallaxWorldScene(final float minZoom, final float maxZoom) {
        super(((MemoryObject.ObjectFactory<RenderTreeInterface>)RenderTree.Factory).newInstance(), DisplayedScreenWorld.createScreenWorld(), minZoom, maxZoom);
        this.m_worldId = -1;
        this.m_background = true;
        this.setForceUpdateDisplayCell(false);
    }
    
    private ParallaxCamera getParallaxCamera() {
        return (ParallaxCamera)this.m_isoCamera;
    }
    
    @Override
    protected AbstractCamera createCamera() {
        return new ParallaxCamera((AleaWorldScene)this);
    }
    
    @Override
    public boolean mouseClicked(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mousePressed(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseReleased(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseEntered(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseExited(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseDragged(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseWheelMoved(final MouseWheelEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean keyTyped(final KeyEvent keyEvent) {
        return false;
    }
    
    @Override
    public boolean keyPressed(final KeyEvent keyEvent) {
        return false;
    }
    
    @Override
    public boolean keyReleased(final KeyEvent keyEvent) {
        return false;
    }
    
    @Override
    public boolean focusGained(final FocusEvent e) {
        return false;
    }
    
    @Override
    public boolean focusLost(final FocusEvent e) {
        return false;
    }
    
    @Override
    protected void cleanManagers() {
    }
    
    @Override
    public void process(final int deltaTime) {
        if (!this.processBegin(deltaTime)) {
            return;
        }
        this.updateCamera(deltaTime);
        this.updateDisplayedCell(true);
        this.prepareBeforeRendering(deltaTime);
        this.m_renderTree.clear();
        this.sortAndInsert();
        this.m_sortedEntities.clear();
        this.m_unsortedEntities.clear();
    }
    
    @Override
    protected void insert(final Entity entity) {
        this.m_renderTree.pushAtEnd(entity);
    }
    
    @Override
    protected void updateLigthing(final int deltaTime) {
        throw new UnsupportedOperationException("pas de light sur les map de d\u00e9cor de fond");
    }
    
    @Override
    public void updateDisplayedCell(final boolean forceUpdate) {
        this.m_displayedScreenWorld.update(this.m_isoCamera.getScreenBounds());
    }
    
    @Override
    protected void prepareSceneBeforeRendering(final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        this.m_displayedScreenWorld.addToScene(this);
    }
    
    @Override
    public boolean isWorldReady() {
        return !this.isValidWorld() || this.m_displayedScreenWorld.isReady();
    }
    
    public void reset() {
        this.setWorld((short)(-1), 0.0f, 0.0f, 0.0f);
    }
    
    public void setWorld(final short worldId, final float speed) {
        this.setWorld(worldId, speed, speed, 1.0f);
    }
    
    public void setWorld(final short worldId, final float speed, final float baseZoom) {
        this.setWorld(worldId, speed, speed, baseZoom);
    }
    
    public void setWorld(final short worldId, final float movementSpeed, final float zoomSpeed, final float baseZoom) {
        this.clean(true);
        this.m_refScene.removeParallax(this);
        this.m_worldId = worldId;
        if (this.isValidWorld()) {
            final IsoCamera refCam = this.m_refScene.getIsoCamera();
            this.getParallaxCamera().prepareCamera(refCam, movementSpeed, zoomSpeed, baseZoom);
            this.m_displayedScreenWorld.setMapsCoordinates(MapManagerHelper.loadValidGfxMapsCoordinates(worldId));
            this.m_displayedScreenWorld.setWorldInstanceId(worldId);
            this.setForceUpdateDisplayCell(true);
            this.setInitialized(true);
            this.m_refScene.addParallax(this);
        }
    }
    
    public void setParallax(final ParallaxInfo parallaxInfo) {
        this.m_background = parallaxInfo.m_isBackground;
        this.setWorld(parallaxInfo.m_worldId, parallaxInfo.m_moveSpeed, parallaxInfo.m_zoomSpeed, parallaxInfo.m_zoom);
    }
    
    public boolean isValidWorld() {
        return this.m_worldId != -1;
    }
    
    public void setReferenceScene(@NotNull final AleaWorldSceneWithParallax scene) {
        this.m_refScene = scene;
    }
    
    public boolean isBackground() {
        return this.m_background;
    }
    
    private void updateCamera(final int deltaTime) {
        if (this.m_refScene == null) {
            return;
        }
        final AleaIsoCamera refCamera = this.m_refScene.getIsoCamera();
        this.getParallaxCamera().update(refCamera, this.m_background, deltaTime);
    }
    
    @Override
    protected AleaWorldScene createNew(final RenderTreeInterface renderTreeCopy, final float minZoom, final float maxZoom) {
        return new ParallaxWorldScene(renderTreeCopy, minZoom, maxZoom);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ParallaxWorldScene.class);
    }
    
    private static class ParallaxCamera extends AbstractCamera
    {
        private static final float ALTITUDE_FACTOR = 0.005f;
        private final Interpolator m_altitudeInterpolator;
        private float m_movementScale;
        private float m_zoomScale;
        private float m_baseZoom;
        private float m_x;
        private float m_y;
        private float m_zoomFactor;
        
        private ParallaxCamera(final AleaWorldScene scene) {
            super(scene);
            this.m_altitudeInterpolator = new Interpolator();
            this.m_movementScale = 0.1f;
            this.m_zoomScale = 0.1f;
            this.m_baseZoom = 1.0f;
            this.m_altitudeInterpolator.setSpeed(2.0f);
            this.m_altitudeInterpolator.setDelta(0.005f);
        }
        
        @Override
        public float getCameraExactIsoWorldX() {
            return 0.0f;
        }
        
        @Override
        public float getCameraExactIsoWorldY() {
            return 0.0f;
        }
        
        @Override
        public final float getScreenFloatX() {
            return this.m_x;
        }
        
        @Override
        public final float getScreenFloatY() {
            return this.m_y;
        }
        
        @Override
        public float getRotationZ() {
            return 0.0f;
        }
        
        @Override
        public final float getZoomFactor() {
            return this.m_zoomFactor;
        }
        
        @Override
        public void process(final int deltaTime) {
            this.m_altitudeInterpolator.process(deltaTime);
            if (this.isClipPlanesNeedUpdate()) {
                this.updateClipPlanes();
            }
        }
        
        final void prepareCamera(final IsoCamera refCam, final float movementSpeed, final float zoomSpeed, final float baseZoom) {
            this.m_movementScale = movementSpeed;
            this.m_zoomScale = zoomSpeed;
            this.m_baseZoom = baseZoom;
            this.m_zoomFactor = this.m_baseZoom;
            if (refCam == null) {
                this.m_x = 0.0f;
                this.m_y = 0.0f;
                this.m_altitudeInterpolator.set(0.0f);
            }
            else {
                this.m_x = refCam.getScreenFloatX();
                this.m_y = refCam.getScreenFloatY();
                this.m_altitudeInterpolator.set(refCam.getAltitude());
            }
            this.setClipPlanesNeedUpdate();
        }
        
        final void update(final AleaIsoCamera refCamera, final boolean background, final int deltaTime) {
            this.updatePosition(refCamera);
            this.m_altitudeInterpolator.setEnd(refCamera.getAltitude());
            this.process(deltaTime);
            this.updateZoom(refCamera, background);
        }
        
        private void updatePosition(final AleaIsoCamera refCamera) {
            final float newX = refCamera.getScreenFloatX() * this.m_movementScale;
            final float newY = refCamera.getScreenFloatY() * this.m_movementScale;
            if (this.getScreenFloatX() != newX || this.getScreenFloatY() != newY) {
                this.m_x = newX;
                this.m_y = newY;
                this.setClipPlanesNeedUpdate();
            }
        }
        
        private void updateZoom(final AleaIsoCamera refCamera, final boolean background) {
            final float deltaZoom = refCamera.getZoomFactor() - 1.0f;
            final float zoomAltitude = 0.005f * this.m_altitudeInterpolator.getValue();
            final float newZoomFactor = this.m_baseZoom + (deltaZoom + zoomAltitude) * this.m_zoomScale;
            if (this.getZoomFactor() != newZoomFactor) {
                this.m_zoomFactor = newZoomFactor;
                this.setClipPlanesNeedUpdate();
            }
        }
    }
}
