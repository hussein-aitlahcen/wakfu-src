package com.ankamagames.baseImpl.graphics.game;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.baseImpl.graphics.ui.utils.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public abstract class GameWorldScene extends AleaWorldSceneWithParallax
{
    protected final AbstractGameClientInstance m_gameClientInstance;
    private float m_maskMaxHeight;
    private float m_maskHeight;
    private float m_maskSpeed;
    private boolean m_maskShow;
    private final EntitySprite m_mask;
    
    protected GameWorldScene(final AbstractGameClientInstance gameClientInstance, final float minZoom, final float maxZoom) {
        super(minZoom, maxZoom);
        this.m_gameClientInstance = gameClientInstance;
        this.m_maskMaxHeight = 25.0f;
        this.m_maskHeight = 0.0f;
        this.m_maskSpeed = 2.0f;
        this.m_maskShow = false;
        this.m_mask = EntitySprite.Factory.newInstance();
        this.m_mask.m_owner = this;
        final GLGeometrySprite geometry = GLGeometrySprite.Factory.newInstance();
        this.m_mask.setGeometry(geometry);
        geometry.removeReference();
        this.m_mask.setColor(0.0f, 0.0f, 0.0f, 1.0f);
    }
    
    @Override
    public void init(final GLAutoDrawable glAutoDrawable) {
        super.init(glAutoDrawable);
        this.m_gameClientInstance.onWorldSceneInitialized();
    }
    
    @Override
    public void display(final GL gl) {
        if (!FadeManager.getInstance().isFadeIn() && this.m_backgroundColorChanged) {
            gl.glClearColor(this.m_backgroundColor.getRed(), this.m_backgroundColor.getGreen(), this.m_backgroundColor.getBlue(), this.m_backgroundColor.getAlpha());
            this.m_backgroundColorChanged = false;
        }
        if (this.m_maskHeight <= 0.0f) {
            super.display(gl);
            return;
        }
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        final int maskHeight = (int)(this.m_maskHeight * this.m_frustumHeight / 100.0f);
        super.display(gl);
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        renderer.setCameraMatrix(Matrix44.IDENTITY);
        this.m_mask.setBounds(-this.m_frustumHeight / 2.0f + maskHeight / 2, -this.m_frustumWidth / 2.0f, (int)this.m_frustumWidth, maskHeight / 2);
        this.m_mask.render(renderer);
        this.m_mask.setBounds(this.m_frustumHeight / 2.0f, -this.m_frustumWidth / 2.0f, (int)this.m_frustumWidth, maskHeight / 2);
        this.m_mask.render(renderer);
        gl.glClearColor(this.m_backgroundColor.getRed(), this.m_backgroundColor.getGreen(), this.m_backgroundColor.getBlue(), this.m_backgroundColor.getAlpha());
    }
    
    @Override
    public void process(final int deltaTime) {
        super.process(deltaTime);
        if (this.m_maskShow) {
            this.m_maskHeight += this.m_maskMaxHeight * this.m_maskSpeed * deltaTime / 1000.0f;
            if (this.m_maskHeight > this.m_maskMaxHeight) {
                this.m_maskHeight = this.m_maskMaxHeight;
            }
        }
        else {
            this.m_maskHeight -= this.m_maskMaxHeight * this.m_maskSpeed * deltaTime / 1000.0f;
            if (this.m_maskHeight < 0.0f) {
                this.m_maskHeight = 0.0f;
            }
        }
    }
    
    public final void changeScalesLimiteAndSet(final ScreenResolution screenResolution) {
        final AleaIsoCamera isoCamera = this.getIsoCamera();
        isoCamera.setScreenResolution(screenResolution.getScreenWidth(), screenResolution.getScreenHeight());
        isoCamera.setZoomFactor(isoCamera.getZoomFactor());
    }
    
    public void setMaskMaxHeight(final float maskMaxHeight) {
        this.m_maskMaxHeight = maskMaxHeight;
    }
    
    public void setMaskHeight(final float maskHeight) {
        this.m_maskHeight = maskHeight;
    }
    
    public void setMaskSpeed(final float maskSpeed) {
        this.m_maskSpeed = maskSpeed;
    }
    
    public void setMaskShow(final boolean maskShow) {
        this.m_maskShow = maskShow;
    }
    
    @Override
    public void cleanAfterFade() {
        super.cleanAfterFade();
        this.m_mask.removeReference();
    }
}
