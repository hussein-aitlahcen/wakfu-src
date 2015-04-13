package com.ankamagames.baseImpl.graphics.isometric.camera;

import com.ankamagames.framework.sound.group.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.targetComputer.*;
import com.ankamagames.framework.kernel.core.common.listener.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.process.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;

public class IsoCamera extends AbstractCamera implements ObservedListener
{
    private static final Logger m_logger;
    private final float m_zoomFactorMin;
    private final float m_zoomFactorMax;
    private boolean m_userZoomLocked;
    private final Interpolator m_zoomInterpolator;
    private final Interpolator2D m_moveInterpolator;
    private float m_offsetX;
    private float m_offsetY;
    private float m_rotationZ;
    @NotNull
    private IsoWorldTarget m_trackingTarget;
    private final TargetPoint m_targetPoint;
    private CameraTargetComputer m_targetComputer;
    private final ListenerHandler<ReachTargetListener> m_reachTargetListener;
    private final Vector3 OBSERVED_VECTOR;
    
    public IsoCamera(final IsoWorldScene scene, final float minZoom, final float maxZoom) {
        super(scene);
        this.m_zoomInterpolator = new Interpolator();
        this.m_moveInterpolator = new Interpolator2D();
        this.m_trackingTarget = new DefaultIsoWorldTarget();
        this.m_targetPoint = new TargetPoint();
        this.m_targetComputer = new TargetTracker();
        this.m_reachTargetListener = new ListenerHandler<ReachTargetListener>(new ListenerNotifier<ReachTargetListener>() {
            @Override
            public void notify(final ReachTargetListener reachTargetListener) {
                reachTargetListener.onTargetReached();
            }
        });
        this.OBSERVED_VECTOR = new Vector3();
        this.m_zoomFactorMin = minZoom;
        this.m_zoomFactorMax = maxZoom;
        this.m_zoomInterpolator.setSpeed(2.1f);
        this.m_moveInterpolator.setSpeed(2.1f);
        this.m_zoomInterpolator.setDelta(0.01f);
        this.m_moveInterpolator.setDelta(0.01f);
        this.m_zoomInterpolator.set(IsoWorldScene.DEFAULT_DEFAULT_ZOOM_FACTOR);
    }
    
    public IsoCamera(final IsoWorldScene scene) {
        this(scene, 1.0f, 3.0f);
    }
    
    @Override
    public void process(final int deltaTime) {
        this.reset();
        this.updateZoomFactor(deltaTime);
        final float x = this.m_trackingTarget.getWorldX();
        final float y = this.m_trackingTarget.getWorldY();
        final float z = this.m_trackingTarget.getAltitude();
        this.m_targetPoint.set(x, y, z);
        final Point2 pt = this.m_targetComputer.getScreenPosition(this.m_scene, this);
        this.m_moveInterpolator.setEnd(pt.m_x, pt.m_y);
        final boolean processed = this.m_moveInterpolator.process(deltaTime, 1.0f / this.getZoomFactor());
        this.updateObservedListener();
        if (processed) {
            this.setClipPlanesNeedUpdate();
        }
        else if (!this.m_targetPoint.reached) {
            this.fireTargetReached();
        }
        if (this.isClipPlanesNeedUpdate()) {
            this.updateClipPlanes();
        }
    }
    
    protected final void updateZoomFactor(final int deltaTime) {
        final float value = this.m_zoomInterpolator.getValue();
        if (value != this.m_zoomInterpolator.process(deltaTime)) {
            this.setClipPlanesNeedUpdate();
        }
    }
    
    public void setCameraMovementMode(final InterpolationType interpolationType) {
        this.m_moveInterpolator.setProcessType(interpolationType);
    }
    
    public final void alignOnTrackingTarget() {
        final Point2 pt = IsoCameraFunc.getScreenPosition(this.m_scene, this.m_trackingTarget);
        this.m_moveInterpolator.set(pt.m_x, pt.m_y);
        this.updateObservedListener();
        this.setClipPlanesNeedUpdate();
        this.m_targetPoint.reached = false;
    }
    
    public final void forceAlign() {
        this.alignOnTrackingTarget();
        this.updateClipPlanes();
    }
    
    public float getDesiredZoomFactor() {
        return this.m_zoomInterpolator.getEnd();
    }
    
    public void setDesiredZoomFactor(final float desiredZoomFactor) {
        this.m_zoomInterpolator.setEnd(MathHelper.clamp(desiredZoomFactor, this.m_zoomFactorMin, this.m_zoomFactorMax));
    }
    
    public void setZoomFactor(final float zoomFactor) {
        this.m_zoomInterpolator.set(zoomFactor);
        this.updateClipPlanes();
    }
    
    @Override
    public float getZoomFactor() {
        return this.m_zoomInterpolator.getValue();
    }
    
    @Override
    public float getCameraExactIsoWorldX() {
        return this.m_scene.screenToIsoX(this.m_moveInterpolator.getValueX(), this.m_moveInterpolator.getValueY(), this.m_trackingTarget.getAltitude());
    }
    
    @Override
    public float getCameraExactIsoWorldY() {
        return this.m_scene.screenToIsoY(this.m_moveInterpolator.getValueX(), this.m_moveInterpolator.getValueY(), this.m_trackingTarget.getAltitude());
    }
    
    public float getZoomFactorMin() {
        return this.m_zoomFactorMin;
    }
    
    public float getZoomFactorMax() {
        return this.m_zoomFactorMax;
    }
    
    @NotNull
    public IsoWorldTarget getTrackingTarget() {
        return this.m_trackingTarget;
    }
    
    public void setTrackingTarget(@NotNull final IsoWorldTarget trackingTarget) {
        this.m_trackingTarget = trackingTarget;
        this.m_targetPoint.reached = false;
        IsoCamera.m_logger.info((Object)("changement de target de la camera " + trackingTarget), (Throwable)new Exception());
    }
    
    public void setTargetComputer(final CameraTargetComputer targetComputer) {
        this.m_targetComputer = targetComputer;
    }
    
    public float[] getLayerColor(final Maskable element) {
        return IsoCameraConstants.DEFAULT_LAYER_COLOR;
    }
    
    public final void addCameraReachTargetListener(final ReachTargetListener listener) {
        this.m_reachTargetListener.addListener(listener);
    }
    
    public final void removeCameraReachTargetListener(final ReachTargetListener listener) {
        this.m_reachTargetListener.removeListener(listener);
    }
    
    private void fireTargetReached() {
        this.m_targetPoint.reached = true;
        this.m_reachTargetListener.notifyListeners();
    }
    
    public final void modifyDesiredZoomFactor(final float delta) {
        if (!this.m_userZoomLocked) {
            this.setDesiredZoomFactor(this.getDesiredZoomFactor() - delta);
        }
    }
    
    public final void setUserZoomLocked(final boolean zoomLocked) {
        this.m_userZoomLocked = zoomLocked;
    }
    
    public void setZoomSpeedFactor(final float zoomSpeedFactor) {
        this.m_zoomInterpolator.setSpeed(2.1f * zoomSpeedFactor);
    }
    
    public void setMoveSpeedFactor(final float moveSpeedFactor) {
        this.m_moveInterpolator.setSpeed(2.1f * moveSpeedFactor);
    }
    
    public void addOffsets(final float offsetX, final float offsetY) {
        this.setOffsets(this.m_offsetX + offsetX, this.m_offsetY + offsetY);
    }
    
    public void reset() {
        this.setOffsets(0.0f, 0.0f);
        this.m_rotationZ = 0.0f;
    }
    
    private void setOffsets(final float offsetX, final float offsetY) {
        if (this.m_offsetX != offsetX || this.m_offsetY != offsetY) {
            this.m_offsetX = offsetX;
            this.m_offsetY = offsetY;
            this.setClipPlanesNeedUpdate();
        }
    }
    
    private void updateObservedListener() {
        final float x = this.m_trackingTarget.getWorldX();
        final float y = this.m_trackingTarget.getWorldY();
        final float z = this.m_trackingTarget.getAltitude();
        this.OBSERVED_VECTOR.set(x - y, -(x + y), z / 4.8f);
    }
    
    @Override
    public Vector3 getListenerPosition() {
        return this.OBSERVED_VECTOR;
    }
    
    @Override
    public float getListenerDistance() {
        return this.getZoomResolutionFactor();
    }
    
    @Override
    public int getGroupMaskKey() {
        return 0;
    }
    
    @Override
    public float getScreenFloatX() {
        return this.m_moveInterpolator.getValueX() + this.m_offsetX;
    }
    
    @Override
    public float getScreenFloatY() {
        return this.m_moveInterpolator.getValueY() + this.m_offsetY;
    }
    
    @Override
    public float getRotationZ() {
        return this.m_rotationZ;
    }
    
    public float getWorldX() {
        return this.m_trackingTarget.getWorldX();
    }
    
    public float getWorldY() {
        return this.m_trackingTarget.getWorldY();
    }
    
    public float getAltitude() {
        return this.m_trackingTarget.getAltitude();
    }
    
    public int getWorldCellX() {
        return MathHelper.fastRound(this.getWorldX());
    }
    
    public int getWorldCellY() {
        return MathHelper.fastRound(this.getWorldY());
    }
    
    public short getWorldCellAltitude() {
        return (short)MathHelper.fastRound(this.getAltitude());
    }
    
    public void addRotationZ(final float angle) {
        this.m_rotationZ = angle;
    }
    
    static {
        m_logger = Logger.getLogger("camera");
    }
    
    private static final class TargetPoint
    {
        private float x;
        private float y;
        private float z;
        private boolean reached;
        
        public void set(final float x, final float y, final float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public boolean equals(final float x, final float y, final float z) {
            return this.x == x && this.y == y && this.z == z;
        }
    }
}
