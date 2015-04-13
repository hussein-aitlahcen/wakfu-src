package com.ankamagames.baseImpl.graphics.isometric.camera;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.listener.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public abstract class AbstractCamera
{
    private static final Logger m_logger;
    protected final IsoWorldScene m_scene;
    private final Rect m_clipPlanesInScreen;
    private boolean m_clipPlanesNeedUpdate;
    private float m_resolutionFactor;
    private int m_screenWidth;
    private int m_screenHeight;
    private ListenerHandler<ClipPlanesUpdatedListener> m_clipPlanesUpdatedListener;
    
    protected AbstractCamera(final IsoWorldScene scene) {
        super();
        this.m_clipPlanesInScreen = new Rect();
        this.m_clipPlanesNeedUpdate = true;
        this.m_resolutionFactor = 1.0f;
        this.m_scene = scene;
        this.setClipPlanesNeedUpdate();
    }
    
    public final float getZoomResolutionFactor() {
        return this.m_resolutionFactor * this.getZoomFactor();
    }
    
    public final float getResolutionFactor() {
        return this.m_resolutionFactor;
    }
    
    public final int getScreenWidth() {
        return this.m_screenWidth;
    }
    
    public final int getScreenHeight() {
        return this.m_screenHeight;
    }
    
    public final boolean isVisibleInScreen(final int top, final int left, final int bottom, final int right) {
        return right >= this.m_clipPlanesInScreen.m_xMin && left <= this.m_clipPlanesInScreen.m_xMax && top >= this.m_clipPlanesInScreen.m_yMin && bottom <= this.m_clipPlanesInScreen.m_yMax;
    }
    
    public final void setScreenResolution(final int screenWidth, final int screenHeight) {
        this.m_screenWidth = screenWidth;
        this.m_screenHeight = screenHeight;
        final float xFactor = screenWidth / 1024.0f;
        final float yFactor = screenHeight / 768.0f;
        this.m_resolutionFactor = Math.max(1.0f, Math.min(xFactor, yFactor));
        this.setClipPlanesNeedUpdate();
    }
    
    protected final void updateClipPlanes() {
        this.computeClipPlanes(this.getScreenFloatX(), this.getScreenFloatY(), this.m_clipPlanesInScreen);
        this.m_clipPlanesNeedUpdate = false;
        this.fireClipPlanesUpdated();
    }
    
    public final void computeClipPlanes(final float centerX, final float centerY, final Rect result) {
        final float ratio = Math.abs(1.0f / (2.0f * this.getZoomResolutionFactor()));
        final float halfWidth = this.getScreenWidth() * ratio;
        final float halfHeight = this.getScreenHeight() * ratio;
        result.m_xMin = MathHelper.fastFloor(centerX - halfWidth);
        result.m_xMax = MathHelper.fastCeil(centerX + halfWidth);
        result.m_yMin = MathHelper.fastFloor(centerY - halfHeight);
        result.m_yMax = MathHelper.fastCeil(centerY + halfHeight);
    }
    
    public final boolean isClipPlanesNeedUpdate() {
        return this.m_clipPlanesNeedUpdate;
    }
    
    protected final void setClipPlanesNeedUpdate() {
        this.m_clipPlanesNeedUpdate = true;
    }
    
    public final void addClipPlanesUpdatedListener(final ClipPlanesUpdatedListener listener) {
        if (this.m_clipPlanesUpdatedListener == null) {
            this.m_clipPlanesUpdatedListener = new ListenerHandler<ClipPlanesUpdatedListener>(new ListenerNotifier<ClipPlanesUpdatedListener>() {
                @Override
                public void notify(final ClipPlanesUpdatedListener clipPlanesUpdated) {
                    clipPlanesUpdated.onClipPlanesUpdated();
                }
            });
        }
        this.m_clipPlanesUpdatedListener.addListener(listener);
    }
    
    public final void removeClipPlanesUpdatedListener(final ClipPlanesUpdatedListener listener) {
        this.m_clipPlanesUpdatedListener.removeListener(listener);
    }
    
    private void fireClipPlanesUpdated() {
        if (this.m_clipPlanesUpdatedListener != null) {
            this.m_clipPlanesUpdatedListener.notifyListeners();
        }
    }
    
    public final Rect getScreenBounds() {
        return this.m_clipPlanesInScreen;
    }
    
    public final int getScreenX() {
        return MathHelper.fastRound(this.getScreenFloatX());
    }
    
    public final int getScreenY() {
        return MathHelper.fastRound(this.getScreenFloatY());
    }
    
    public abstract float getCameraExactIsoWorldX();
    
    public abstract float getCameraExactIsoWorldY();
    
    public abstract float getScreenFloatX();
    
    public abstract float getScreenFloatY();
    
    public abstract float getRotationZ();
    
    public abstract float getZoomFactor();
    
    public abstract void process(final int p0);
    
    public IsoWorldTarget getIsoTarget() {
        return new DefaultIsoWorldTarget() {
            @Override
            public float getWorldX() {
                return AbstractCamera.this.getCameraExactIsoWorldX();
            }
            
            @Override
            public float getWorldY() {
                return AbstractCamera.this.getCameraExactIsoWorldY();
            }
            
            @Override
            public float getAltitude() {
                return super.getAltitude();
            }
            
            @Override
            public int getScreenX() {
                return AbstractCamera.this.getScreenX();
            }
            
            @Override
            public int getScreenY() {
                return AbstractCamera.this.getScreenY();
            }
        };
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractCamera.class);
    }
}
