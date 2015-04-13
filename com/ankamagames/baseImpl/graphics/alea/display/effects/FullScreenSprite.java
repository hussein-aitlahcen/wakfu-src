package com.ankamagames.baseImpl.graphics.alea.display.effects;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class FullScreenSprite extends Effect
{
    private EntitySprite m_sprite;
    private float m_red;
    private float m_green;
    private float m_blue;
    private float m_alpha;
    private final Matrix44 m_previousWorldMatrix;
    private final Matrix44 m_previousCameraMatrix;
    private final Matrix44 m_previousProjectionMatrix;
    
    public FullScreenSprite() {
        super();
        this.m_previousWorldMatrix = Matrix44.Factory.newInstance();
        this.m_previousCameraMatrix = Matrix44.Factory.newInstance();
        this.m_previousProjectionMatrix = Matrix44.Factory.newInstance();
        this.m_sprite = EntitySprite.Factory.newPooledInstance();
        this.m_sprite.m_owner = this;
        final GLGeometrySprite geometry = GLGeometrySprite.Factory.newPooledInstance();
        this.m_sprite.setGeometry(geometry);
        this.m_sprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.m_sprite.setBounds(1.0f, -1.0f, 2, 2);
        this.m_sprite.setTextureCoordinates(0.0f, 0.0f, 1.0f, 1.0f);
        geometry.removeReference();
    }
    
    @Override
    public void clear() {
        this.m_sprite.removeReference();
        this.m_sprite = null;
    }
    
    @Override
    public void update(final int timeIncrement) {
        super.update(timeIncrement);
        final float s = this.getStrength();
        this.m_sprite.setColor(this.m_red * s, this.m_green * s, this.m_blue * s, this.m_alpha * s);
    }
    
    @Override
    public void render(final Renderer renderer) {
        final GL gl = ((GLRenderer)renderer).getDevice();
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        this.m_previousCameraMatrix.set(renderer.getCameraMatrix());
        this.m_previousWorldMatrix.set(renderer.getWorldMatrix());
        gl.glGetFloatv(2983, this.m_previousProjectionMatrix.getBuffer(), 0);
        renderer.setWorldMatrix(Matrix44.IDENTITY);
        renderer.setCameraMatrix(Matrix44.IDENTITY);
        stateManager.applyMatrixMode(gl, MatrixModes.PROJECTION, Matrix44.IDENTITY);
        this.m_sprite.render(renderer);
        renderer.setWorldMatrix(this.m_previousWorldMatrix);
        renderer.setCameraMatrix(this.m_previousCameraMatrix);
        stateManager.applyMatrixMode(gl, MatrixModes.PROJECTION, this.m_previousProjectionMatrix);
    }
    
    public void setTexture(final String textureFileName) {
        if (textureFileName != null) {
            final String textureFilePath = Engine.getInstance().getEffectPath() + "textures/" + textureFileName;
            final long name = 0xEE00EE0000000000L ^ Engine.getTextureName(textureFilePath);
            final Texture texture = TextureManager.getInstance().createTexture(RendererType.OpenGL.getRenderer(), name, textureFilePath, false);
            this.m_sprite.setTexture(texture);
        }
        else {
            this.m_sprite.setTexture(null);
        }
    }
    
    public void setColor(final float red, final float green, final float blue, final float alpha) {
        this.m_red = red;
        this.m_green = green;
        this.m_blue = blue;
        this.m_alpha = alpha;
    }
    
    public void setPosition(final float centerX, final float centerY) {
        this.m_sprite.getMatrix().setTranslation(centerX, centerY, 0.0f);
    }
    
    public void setSize(final float width, final float height) {
        this.m_sprite.getMatrix().setScale(width, height, 1.0f);
    }
    
    public void setBlendFunc(final BlendModes src, final BlendModes dst) {
        this.m_sprite.getGeometry().setBlendFunc(src, dst);
    }
}
