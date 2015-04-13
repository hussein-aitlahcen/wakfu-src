package com.ankamagames.baseImpl.graphics.alea.adviser.text.flying;

import com.ankamagames.baseImpl.graphics.alea.adviser.text.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.graphics.engine.text.*;

public class FlyingText extends TimeTargetedTextArea implements Adviser
{
    private FlyingTextDeformer m_deformer;
    private int m_id;
    private int m_typeId;
    private float m_x;
    private float m_y;
    private Vector4 _tempVec;
    
    private static float progressive(final float a, final float b, final int current, final int duration) {
        float percent = current / duration / 2.0f;
        final float delta = (0.5f - percent) * (1.0f - 2.0f * Math.abs(0.5f - percent));
        percent = (percent - delta) * 2.0f;
        return a + (b - a) * percent;
    }
    
    private static float easeOut(float t, final float b, final float c, final float d) {
        return -c * ((t = t / d - 1.0f) * t * t * t - 1.0f) + b;
    }
    
    private static float linear(final float tc, final float a, final float b, final float tt) {
        return tc * (b - a) / tt + a;
    }
    
    public FlyingText(final Font font, final String text) {
        this(font, text, -1);
    }
    
    public FlyingText(final Font font, final String text, final int duration) {
        this(font, text, new DefaultFlyingTextDeformer(), duration);
    }
    
    public FlyingText(final Font font, final String text, final FlyingTextDeformer deformer, final int duration) {
        super(font, text, duration);
        this.m_deformer = null;
        this.m_typeId = 3;
        this._tempVec = new Vector4();
        this.getEntity().setBackgroundGeometry(null);
        this.getEntity().setFont(font);
        this.m_deformer = deformer;
    }
    
    public FlyingTextDeformer getDeformer() {
        return this.m_deformer;
    }
    
    public void setDeformer(final FlyingTextDeformer deformer) {
        this.m_deformer = deformer;
    }
    
    public float getSortPosition() {
        return 2.0f;
    }
    
    @Override
    public void setPosition(final float x, final float y, final float deltaX, final float deltaY) {
        this._tempVec.set(x, y, -1.0f);
        this.getEntity().setPosition(this._tempVec);
        this.getEntity().setTextOffset((int)deltaX, (int)deltaY);
        this.m_x = x;
        this.m_y = y;
        this.updatePosition();
    }
    
    @Override
    public void process(final AleaWorldScene scene, final int deltaTime) {
        this.process(deltaTime);
    }
    
    @Override
    public void process(final int deltaTime) {
        super.process(deltaTime);
        this.getDeformer().process(this, deltaTime);
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
        return this.m_typeId;
    }
    
    @Override
    public void setTypeId(final int typeId) {
        this.m_typeId = typeId;
    }
    
    @Override
    public EntityText getEntity() {
        return super.getEntity();
    }
    
    @Override
    public void cleanUp() {
        this.release();
    }
    
    private void updatePosition() {
        this.getEntity().getTransformer().setTranslation(0, this.m_x, this.m_y);
    }
    
    public float getX() {
        return this.m_x;
    }
    
    public void setX(final float x) {
        this.m_x = x;
    }
    
    public float getY() {
        return this.m_y;
    }
    
    public void setY(final float y) {
        this.m_y = y;
    }
    
    public static class DefaultFlyingTextDeformer implements FlyingTextDeformer
    {
        private int m_xOffset;
        private int m_yOffset;
        
        public DefaultFlyingTextDeformer() {
            super();
        }
        
        public DefaultFlyingTextDeformer(final int xOffset, final int yOffset) {
            super();
            this.m_xOffset = xOffset;
            this.m_yOffset = yOffset;
        }
        
        @Override
        public void process(final FlyingText flyingText, final int deltaTime) {
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            flyingText.setXOffset(this.m_xOffset);
            flyingText.setYOffset((int)easeOut(elapsedTime, 60.0f, 50.0f, duration) + this.m_yOffset);
            final Color color = flyingText.getEntity().getColor();
            color.setAlpha(easeOut(elapsedTime, 1.5f, -1.6f, duration));
            flyingText.getEntity().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            flyingText.getEntity().setZoom(0.5f);
        }
    }
    
    public static class StaticFlyingTextDeformer implements FlyingTextDeformer
    {
        private int m_xOffset;
        private int m_yOffset;
        
        public StaticFlyingTextDeformer() {
            super();
        }
        
        public StaticFlyingTextDeformer(final int xOffset, final int yOffset) {
            super();
            this.m_xOffset = xOffset;
            this.m_yOffset = yOffset;
        }
        
        @Override
        public void process(final FlyingText flyingText, final int deltaTime) {
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            flyingText.setXOffset(this.m_xOffset);
            flyingText.setYOffset(this.m_yOffset);
            final Color color = flyingText.getEntity().getColor();
            flyingText.getEntity().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            flyingText.getEntity().setZoom(1.0f);
        }
    }
    
    public static class FadingFlyingTextDeformer implements FlyingTextDeformer
    {
        private int m_xOffset;
        private int m_yOffset;
        
        public FadingFlyingTextDeformer(final int xOffset, final int yOffset) {
            super();
            this.m_xOffset = xOffset;
            this.m_yOffset = yOffset;
        }
        
        @Override
        public void process(final FlyingText flyingText, final int deltaTime) {
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            flyingText.setYOffset((int)easeOut(elapsedTime, 60.0f, 60.0f, duration) + this.m_yOffset);
            flyingText.setXOffset(this.m_xOffset);
            final Color color = flyingText.getEntity().getColor();
            final float alphaDuration = duration * 0.8f;
            color.setAlpha(MathHelper.clamp(easeOut(Math.min(elapsedTime, alphaDuration), 20.0f, -20.1f, alphaDuration), 0.0f, 1.0f));
            flyingText.getEntity().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
        
        private float fadeEaseOut(final float elapsedTime, final float duration, final int numRepeat) {
            final float value = elapsedTime * numRepeat / duration;
            final int intPart = (int)Math.floor(value);
            final float delta = value - intPart;
            final boolean even = intPart % 2 == 0;
            if (even) {
                return delta;
            }
            return 1.0f - delta;
        }
    }
    
    public static class ShakeTextDeformer implements FlyingTextDeformer
    {
        private int m_delay;
        
        public ShakeTextDeformer(final int delay) {
            super();
            this.m_delay = 0;
            this.m_delay = delay;
        }
        
        @Override
        public void process(final FlyingText flyingText, final int deltaTime) {
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            int xOffset = 0;
            final int yOffset = (int)easeOut(Math.max(0, elapsedTime - this.m_delay), 80.0f, 100.0f, duration);
            if (elapsedTime < this.m_delay) {
                final float factor = 1.0f - elapsedTime / this.m_delay;
                xOffset += (int)MathHelper.random(-5.0f * factor, 5.0f * factor);
            }
            flyingText.setYOffset(yOffset);
            flyingText.setXOffset(xOffset);
            final Color color = flyingText.getEntity().getColor();
            color.setAlpha(easeOut(elapsedTime, 1.5f, -1.6f, duration));
            flyingText.getEntity().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
    }
    
    public static class MovingFlyingTextDeformer implements FlyingTextDeformer
    {
        private final int m_xDistance;
        private final int m_yDistance;
        
        public MovingFlyingTextDeformer(final int xDistance, final int yDistance) {
            super();
            this.m_xDistance = xDistance;
            this.m_yDistance = yDistance;
        }
        
        @Override
        public void process(final FlyingText flyingText, final int deltaTime) {
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            final Color color = flyingText.getEntity().getColor();
            color.setAlpha(easeOut(elapsedTime, 1.5f, -1.6f, duration));
            flyingText.getEntity().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            final float EXPLOSION_DURATION = 100.0f;
            float scaleFactor;
            if (elapsedTime < 100.0f) {
                scaleFactor = 0.75f;
            }
            else {
                final float percentage = MathHelper.clamp((elapsedTime - 100.0f) / (duration - 100.0f) * 4.0f, 0.0f, 1.0f);
                scaleFactor = Math.max(0.0f, 0.75f - percentage);
            }
            final float totalPercentage = Math.min(1.0f, elapsedTime / duration * 4.0f);
            flyingText.setXOffset((int)(this.m_xDistance * totalPercentage));
            final double piPercentage = 3.141592653589793 * totalPercentage;
            flyingText.setYOffset((int)(this.m_yDistance * Math.abs(Math.sin(piPercentage))) + 60);
            flyingText.getEntity().setZoom(scaleFactor);
        }
    }
    
    public static class LinkToUIFlyingTextDeformer implements FlyingTextDeformer
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
        private boolean m_resize;
        
        public LinkToUIFlyingTextDeformer(final AleaWorldScene scene, final FlyingWidgetUIDelegate delegate, final boolean resize) {
            super();
            this.m_firstX = -1.0f;
            this.m_firstY = -1.0f;
            this.m_firstWidth = -1;
            this.m_firstHeight = -1;
            this.m_scene = scene;
            this.m_delegate = delegate;
            this.m_resize = resize;
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
        public void process(final FlyingText flyingText, final int deltaTime) {
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            final EntityText entity = flyingText.getEntity();
            final int phase2StartTime = duration / 4;
            final int phase3StartTime = duration / 2;
            final int height = entity.getTextHeight();
            final int width = entity.getTextWidth();
            if (elapsedTime > phase3StartTime) {
                this.recompute();
                if (this.m_firstX == -1.0f) {
                    this.m_firstX = flyingText.getXOffset();
                }
                if (this.m_firstY == -1.0f) {
                    this.m_firstY = flyingText.getYOffset();
                }
                if (this.m_resize) {
                    if (this.m_firstWidth == -1) {
                        this.m_firstWidth = width;
                    }
                    if (this.m_firstHeight == -1) {
                        this.m_firstHeight = height;
                    }
                }
                final int deltaTimeP3 = elapsedTime - phase3StartTime;
                final int durationP3 = duration - phase3StartTime;
                final int xoffset = Math.round(linear(deltaTimeP3, this.m_firstX, this.m_screenX - (flyingText.getX() - flyingText.getXOffset()), durationP3));
                final int yoffset = Math.round(linear(deltaTimeP3, this.m_firstY, this.m_screenY - (flyingText.getY() - flyingText.getYOffset()), durationP3));
                flyingText.setXOffset(xoffset);
                flyingText.setYOffset(yoffset);
                if (this.m_resize && width > 0 && height > 0) {
                    final int modWidth = Math.max(0, (int)(width - (width - linear(deltaTimeP3, this.m_firstWidth, 0.0f, duration))));
                    final int modHeight = Math.max(0, (int)(height - (height - linear(deltaTimeP3, this.m_firstHeight, 0.0f, duration))));
                    flyingText.getEntity().setZoom(linear(deltaTimeP3, 2.0f, 1.0f, duration));
                }
            }
            else if (elapsedTime < phase2StartTime) {
                final int sizeFactor = (int)easeOut(elapsedTime, 0.0f, 2.0f, phase2StartTime);
                flyingText.setYOffset((int)easeOut(elapsedTime, 60.0f, 60.0f, duration));
                flyingText.setXOffset(-(width + sizeFactor) / 2);
                if (this.m_resize) {
                    flyingText.getEntity().setZoom(sizeFactor);
                }
            }
            flyingText.updatePosition();
        }
    }
    
    public static class DragonicaTextDeformer implements FlyingTextDeformer
    {
        private final int m_xDistance;
        private final int m_yDistance;
        private final int m_xOffset;
        private final int m_yOffset;
        private final float m_maxScale;
        private final int m_explosionDuration;
        public static final int DEFAULT_EXPLOSION_DURATION = 100;
        
        public DragonicaTextDeformer(final int xDistance, final int yDistance, final int xOffset, final int yOffset, final float maxScale, final int explosionDuration) {
            super();
            this.m_xDistance = xDistance;
            this.m_yDistance = yDistance;
            this.m_xOffset = xOffset;
            this.m_yOffset = yOffset;
            this.m_maxScale = maxScale;
            this.m_explosionDuration = explosionDuration;
        }
        
        @Override
        public void process(final FlyingText flyingText, final int deltaTime) {
            flyingText.getEntity().setAlign(TextAlignment.CENTER);
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            final Color color = flyingText.getEntity().getColor();
            if (elapsedTime > duration - 500) {
                color.setAlpha(easeOut(elapsedTime - duration + 500, 1.5f, -1.6f, 500.0f));
            }
            flyingText.getEntity().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            int xOffset = this.m_xOffset;
            int yOffset = this.m_yOffset;
            float scaleFactor;
            if (elapsedTime < this.m_explosionDuration) {
                scaleFactor = progressive(this.m_maxScale * 3.0f, this.m_maxScale, elapsedTime, this.m_explosionDuration);
            }
            else if (elapsedTime > this.m_explosionDuration + 350) {
                scaleFactor = this.m_maxScale;
                final int phase2Duration = duration - this.m_explosionDuration - 350;
                final int phase2Time = elapsedTime - this.m_explosionDuration - 350;
                final float perc = progressive(0.0f, 1.0f, phase2Time, phase2Duration);
                xOffset += (int)(this.m_xDistance * perc);
                yOffset += (int)(this.m_yDistance * perc);
            }
            else {
                scaleFactor = this.m_maxScale;
            }
            flyingText.setXOffset(xOffset);
            flyingText.setYOffset(yOffset);
            flyingText.getEntity().setZoom(scaleFactor);
        }
    }
    
    public static class SplodingFlyingTextDeformer implements FlyingTextDeformer
    {
        private final int m_xDistance;
        private final int m_yDistance;
        private final int m_xOffset;
        private final int m_yOffset;
        private final float m_maxScale;
        private final int m_explosionDuration;
        private final int m_shakeDuration;
        public static final int DEFAULT_EXPLOSION_DURATION = 100;
        
        public SplodingFlyingTextDeformer(final int xDistance, final int yDistance, final int xOffset, final int yOffset, final float maxScale, final int explosionDuration) {
            super();
            this.m_xDistance = xDistance;
            this.m_yDistance = yDistance;
            this.m_xOffset = xOffset;
            this.m_yOffset = yOffset;
            this.m_maxScale = maxScale;
            this.m_explosionDuration = explosionDuration;
            this.m_shakeDuration = 0;
        }
        
        public SplodingFlyingTextDeformer(final int xDistance, final int yDistance, final int xOffset, final int yOffset, final float maxScale, final int explosionDuration, final int shakeDuration) {
            super();
            this.m_xDistance = xDistance;
            this.m_yDistance = yDistance;
            this.m_xOffset = xOffset;
            this.m_yOffset = yOffset;
            this.m_maxScale = maxScale;
            this.m_explosionDuration = explosionDuration;
            this.m_shakeDuration = shakeDuration;
        }
        
        @Override
        public void process(final FlyingText flyingText, final int deltaTime) {
            final int elapsedTime = flyingText.getElapsedLifeTime();
            final int duration = flyingText.getDuration();
            final Color color = flyingText.getEntity().getColor();
            if (elapsedTime > duration - 500) {
                color.setAlpha(easeOut(elapsedTime - duration + 500, 1.5f, -1.6f, 500.0f));
            }
            flyingText.getEntity().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            float perc;
            if (elapsedTime < this.m_explosionDuration) {
                perc = elapsedTime / this.m_explosionDuration;
            }
            else {
                perc = 1.0f;
            }
            int xoffset = (int)(this.m_xDistance * perc) + this.m_xOffset;
            int yoffset = (int)(this.m_yDistance * perc) + this.m_yOffset;
            if (elapsedTime < this.m_shakeDuration) {
                final float factor = 1.0f - elapsedTime / this.m_shakeDuration;
                xoffset += (int)MathHelper.random(-10.0f * factor, 10.0f * factor);
                yoffset += (int)MathHelper.random(-10.0f * factor, 10.0f * factor);
            }
            flyingText.setXOffset(xoffset);
            flyingText.setYOffset(yoffset);
            flyingText.getEntity().setZoom(this.m_maxScale * perc);
        }
    }
}
