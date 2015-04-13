package com.ankamagames.baseImpl.graphics.alea.adviser.text.flying;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.common.*;

public class FlyingImage extends AbstractDelayedAdviser
{
    private final Entity3D m_entity;
    private WorldPositionable m_target;
    private final FlyingImageDeformer m_deformer;
    private float m_x;
    private float m_y;
    private int m_xOffset;
    private int m_yOffset;
    private int m_id;
    private int m_type;
    private boolean m_visible;
    public static final int INFINITE_DURATION = -1;
    
    private static float easeOut(float t, final float b, final float c, final float d) {
        return -c * ((t = t / d - 1.0f) * t * t * t - 1.0f) + b;
    }
    
    private static float linear(final float tc, final float a, final float b, final float tt) {
        return tc * (b - a) / tt + a;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
    }
    
    public FlyingImage(final String imagePath, final int width, final int height, final FlyingImageDeformer deformer, final int duration) {
        this(TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(imagePath), imagePath, false), width, height, deformer, duration);
    }
    
    public FlyingImage(final Texture texture, int width, int height, final FlyingImageDeformer deformer, final int duration) {
        super();
        this.m_type = 4;
        this.m_visible = true;
        this.m_deformer = deformer;
        this.m_duration = duration;
        this.m_entity = Entity3D.Factory.newPooledInstance();
        this.m_entity.getTransformer().addTransformer(new TransformerSRT());
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        if (width == -1) {
            width = texture.getLayer(0).getWidth();
        }
        if (height == -1) {
            height = texture.getLayer(0).getHeight();
        }
        geom.setBounds(height, 0.0f, width, height);
        geom.setTextureCoordinates(0.0f, 0.0f, 1.0f, 1.0f);
        this.m_entity.addTexturedGeometry(geom, texture, null);
        geom.removeReference();
        this.m_entity.setColor(1.0f, 1.0f, 1.0f, 0.0f);
    }
    
    @Override
    public WorldPositionable getTarget() {
        return this.m_target;
    }
    
    @Override
    public void setTarget(final WorldPositionable target) {
        this.m_target = target;
    }
    
    @Override
    public int getXOffset() {
        return this.m_xOffset;
    }
    
    @Override
    public void setXOffset(final int offset) {
        this.m_xOffset = offset;
    }
    
    @Override
    public int getYOffset() {
        return this.m_yOffset;
    }
    
    @Override
    public void setYOffset(final int offset) {
        this.m_yOffset = offset;
    }
    
    @Override
    public float getWorldX() {
        if (this.m_target != null) {
            return this.m_target.getWorldX();
        }
        return 0.0f;
    }
    
    @Override
    public float getWorldY() {
        if (this.m_target != null) {
            return this.m_target.getWorldY();
        }
        return 0.0f;
    }
    
    @Override
    public float getAltitude() {
        if (this.m_target != null) {
            return this.m_target.getAltitude();
        }
        return 0.0f;
    }
    
    private void updatePosition() {
        this.getEntity().getTransformer().setTranslation(0, this.m_x, this.m_y);
    }
    
    @Override
    public void setPosition(final float x, final float y, final float deltaX, final float deltaY) {
        this.m_x = x + deltaX;
        this.m_y = y + deltaY;
        this.updatePosition();
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public void setId(final int id) {
        this.m_id = id;
    }
    
    @Override
    public int getTypeId() {
        return this.m_type;
    }
    
    @Override
    public void setTypeId(final int typeId) {
        this.m_type = typeId;
    }
    
    @Override
    public Entity3D getEntity() {
        return this.m_entity;
    }
    
    public void resetElapsedLifeTime() {
        this.m_elapsedLifeTime = 0;
    }
    
    @Override
    public void process(final int deltaTime) {
        super.process(deltaTime);
        if (this.m_visible) {
            this.m_deformer.process(this, deltaTime);
        }
    }
    
    @Override
    public void process(final AleaWorldScene scene, final int deltaTime) {
        this.process(deltaTime);
    }
    
    @Override
    public void cleanUp() {
        this.m_entity.removeReference();
        this.m_target = null;
    }
    
    @Override
    public boolean needsToPrecomputeZoom() {
        return true;
    }
    
    public float getX() {
        return this.m_x;
    }
    
    public float getY() {
        return this.m_y;
    }
    
    public static class DefaultFlyingImageDeformer implements FlyingImageDeformer
    {
        @Override
        public void process(final FlyingImage flyingImage, final int deltaTime) {
            final int elapsedTime = flyingImage.getElapsedLifeTime();
            final int duration = flyingImage.getDuration();
            final GLGeometrySprite geom = (GLGeometrySprite)flyingImage.getEntity().getGeometry(0);
            flyingImage.setYOffset((int)easeOut(elapsedTime, 60.0f, 60.0f, duration));
            flyingImage.setXOffset(-geom.getWidth() / 2);
            flyingImage.updatePosition();
            final float alphaDuration = duration * 0.8f;
            final float alpha = MathHelper.clamp(easeOut(Math.min(elapsedTime, alphaDuration), 20.0f, -20.1f, alphaDuration), 0.0f, 1.0f);
            flyingImage.getEntity().setColor(1.0f, 1.0f, 1.0f, alpha);
        }
    }
    
    public static class StaticFlyingImageDeformer implements FlyingImageDeformer
    {
        @Override
        public void process(final FlyingImage flyingImage, final int deltaTime) {
            final int elapsedTime = flyingImage.getElapsedLifeTime();
            final int duration = flyingImage.getDuration();
            final GLGeometrySprite geom = (GLGeometrySprite)flyingImage.getEntity().getGeometry(0);
            flyingImage.setXOffset(-geom.getWidth() / 2);
            flyingImage.updatePosition();
            float alpha = 1.0f;
            if (elapsedTime < duration / 4) {
                alpha = easeOut(elapsedTime, -0.5f, 1.6f, duration / 4);
            }
            else if (elapsedTime > duration * 3 / 4) {
                alpha = easeOut(elapsedTime - duration * 3 / 4, 1.5f, -1.6f, duration / 4);
            }
            flyingImage.getEntity().setColor(1.0f, 1.0f, 1.0f, alpha);
        }
    }
    
    public static class LinkToUIFlyingImageDeformer implements FlyingImageDeformer
    {
        private AleaWorldScene m_scene;
        private FlyingWidgetUIDelegate m_delegate;
        private float m_screenX;
        private float m_screenY;
        private float m_zoomFactor;
        private float m_firstX;
        private float m_firstY;
        private int m_firstWidth;
        private int m_firstHeight;
        
        public LinkToUIFlyingImageDeformer(final AleaWorldScene scene, final FlyingWidgetUIDelegate delegate) {
            super();
            this.m_firstX = -1.0f;
            this.m_firstY = -1.0f;
            this.m_firstWidth = -1;
            this.m_firstHeight = -1;
            this.m_scene = scene;
            this.m_delegate = delegate;
        }
        
        private void recompute() {
            final AleaIsoCamera cam = this.m_scene.getIsoCamera();
            this.m_zoomFactor = cam.getZoomResolutionFactor();
            final int camScreenX = cam.getScreenX();
            final int camScreenY = cam.getScreenY();
            final int widgetX = (int)(this.m_delegate.getScreenX() / this.m_zoomFactor);
            final int widgetY = (int)(this.m_delegate.getScreenY() / this.m_zoomFactor);
            this.m_screenX = widgetX + camScreenX;
            this.m_screenY = widgetY + camScreenY;
        }
        
        @Override
        public void process(final FlyingImage flyingImage, final int deltaTime) {
            final int elapsedTime = flyingImage.getElapsedLifeTime();
            final int duration = flyingImage.getDuration();
            final Entity3D entity = flyingImage.getEntity();
            final GLGeometrySprite geom = (GLGeometrySprite)entity.getGeometry(0);
            final int phase2StartTime = Math.min(250, duration / 4);
            final int phase3StartTime = duration / 2;
            final int height = geom.getHeight();
            final int width = geom.getWidth();
            if (elapsedTime > phase3StartTime) {
                this.recompute();
                if (this.m_firstX == -1.0f) {
                    this.m_firstX = flyingImage.getXOffset();
                }
                if (this.m_firstY == -1.0f) {
                    this.m_firstY = flyingImage.getYOffset();
                }
                if (this.m_firstWidth == -1) {
                    this.m_firstWidth = width;
                }
                if (this.m_firstHeight == -1) {
                    this.m_firstHeight = height;
                }
                final int deltaTimeP3 = elapsedTime - phase3StartTime;
                final int durationP3 = duration - phase3StartTime;
                final int xoffset = Math.round(linear(deltaTimeP3, this.m_firstX, this.m_screenX - (flyingImage.getX() - flyingImage.getXOffset()), durationP3));
                final int yoffset = Math.round(linear(deltaTimeP3, this.m_firstY, this.m_screenY - (flyingImage.getY() - flyingImage.getYOffset()), durationP3));
                flyingImage.setXOffset(xoffset);
                flyingImage.setYOffset(yoffset);
                if (width > 0 && height > 0) {
                    final int modWidth = Math.max(0, (int)(width - (width - linear(deltaTimeP3, this.m_firstWidth, 0.0f, duration))));
                    final int modHeight = Math.max(0, (int)(height - (height - linear(deltaTimeP3, this.m_firstHeight, 0.0f, duration))));
                    geom.setSize(modWidth, modHeight);
                }
            }
            else if (elapsedTime < phase2StartTime) {
                final float sizeFactor = easeOut(elapsedTime, 0.0f, 1.0f, phase2StartTime);
                geom.setSize((int)(48.0f * sizeFactor), (int)(48.0f * sizeFactor));
                flyingImage.setYOffset((int)easeOut(elapsedTime, 60.0f, 60.0f, duration));
                flyingImage.setXOffset(-geom.getWidth() / 2);
            }
            flyingImage.updatePosition();
            entity.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    public static class LinkFromUIFlyingImageDeformer implements FlyingImageDeformer
    {
        private AleaWorldScene m_scene;
        private FlyingWidgetUIDelegate m_delegate;
        private float m_screenX;
        private float m_screenY;
        private float m_zoomFactor;
        private float m_deltaX;
        private float m_deltaY;
        private int m_firstWidth;
        private int m_firstHeight;
        
        public LinkFromUIFlyingImageDeformer(final AleaWorldScene scene, final FlyingWidgetUIDelegate delegate) {
            super();
            this.m_deltaX = -1.0f;
            this.m_deltaY = -1.0f;
            this.m_firstWidth = -1;
            this.m_firstHeight = -1;
            this.m_scene = scene;
            this.m_delegate = delegate;
        }
        
        private void recompute() {
            final AleaIsoCamera cam = this.m_scene.getIsoCamera();
            this.m_zoomFactor = cam.getZoomResolutionFactor();
            final int camScreenX = cam.getScreenX();
            final int camScreenY = cam.getScreenY();
            final int widgetX = (int)(this.m_delegate.getScreenX() / this.m_zoomFactor);
            final int widgetY = (int)(this.m_delegate.getScreenY() / this.m_zoomFactor);
            this.m_screenX = widgetX + camScreenX;
            this.m_screenY = widgetY + camScreenY;
        }
        
        @Override
        public void process(final FlyingImage flyingImage, final int deltaTime) {
            final int elapsedTime = flyingImage.getElapsedLifeTime();
            final int duration = flyingImage.getDuration();
            final Entity3D entity = flyingImage.getEntity();
            final GLGeometrySprite geom = (GLGeometrySprite)entity.getGeometry(0);
            final int phase3StartTime;
            final int phase2Duration = phase3StartTime = duration / 2;
            final int phase3Duration = duration / 2;
            final int height = geom.getHeight();
            final int width = geom.getWidth();
            this.recompute();
            if (flyingImage.getX() != 0.0f || flyingImage.getY() != 0.0f) {
                if (this.m_deltaX == -1.0f) {
                    this.m_deltaX = this.m_screenX - flyingImage.getX();
                }
                if (this.m_deltaY == -1.0f) {
                    this.m_deltaY = this.m_screenY - flyingImage.getY();
                }
                flyingImage.setYOffset((int)this.m_deltaY);
                flyingImage.setXOffset((int)this.m_deltaX);
                flyingImage.getEntity().setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
            if (this.m_firstWidth == -1) {
                this.m_firstWidth = width;
            }
            if (this.m_firstHeight == -1) {
                this.m_firstHeight = height;
            }
            if (elapsedTime > phase3StartTime) {
                final int deltaTimeP3 = elapsedTime - phase3StartTime;
                flyingImage.setYOffset((int)easeOut(deltaTimeP3, 60.0f, 40.0f, phase3Duration));
                flyingImage.setXOffset(-geom.getWidth() / 2);
                final float alphaDuration = phase3Duration * 0.8f;
                final float alpha = MathHelper.clamp(easeOut(Math.min(deltaTimeP3, alphaDuration), 20.0f, -20.1f, alphaDuration), 0.0f, 1.0f);
                flyingImage.getEntity().setColor(1.0f, 1.0f, 1.0f, alpha);
            }
            else {
                final int xoffset = Math.round(easeOut(elapsedTime, this.m_deltaX, -this.m_deltaX - width / 2, phase2Duration));
                final int yoffset = Math.round(easeOut(elapsedTime, this.m_deltaY, -this.m_deltaY + 60.0f, phase2Duration));
                flyingImage.setXOffset(xoffset);
                flyingImage.setYOffset(yoffset);
            }
            flyingImage.updatePosition();
        }
    }
}
