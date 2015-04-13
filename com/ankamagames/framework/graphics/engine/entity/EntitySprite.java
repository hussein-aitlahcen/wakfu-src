package com.ankamagames.framework.graphics.engine.entity;

import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.states.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class EntitySprite extends Entity
{
    public static final ObjectFactory Factory;
    public static final int TYPE = 2;
    private GeometrySprite m_geometry;
    private Texture m_texture;
    private Texture m_texture2;
    private Texture m_texture3;
    private Material m_material;
    private boolean m_materialChanged;
    
    public void setGeometry(final GeometrySprite geometry) {
        if (geometry == this.m_geometry) {
            return;
        }
        geometry.addReference();
        if (this.m_geometry != null) {
            this.m_geometry.removeReference();
        }
        this.m_geometry = geometry;
    }
    
    public GeometrySprite getGeometry() {
        return this.m_geometry;
    }
    
    public void setTextureCoordinates(final float top, final float left, final float bottom, final float right) {
        this.m_geometry.setTextureCoordinates(top, left, bottom, right);
    }
    
    public void setTexture(final Texture texture) {
        if (texture != null) {
            texture.addReference();
        }
        if (this.m_texture != null) {
            this.m_texture.removeReference();
        }
        this.m_texture = texture;
    }
    
    public void setTexture2(final Texture texture) {
        if (texture != null) {
            texture.addReference();
        }
        if (this.m_texture2 != null) {
            this.m_texture2.removeReference();
        }
        this.m_texture2 = texture;
    }
    
    public void setTexture3(final Texture texture) {
        if (texture != null) {
            texture.addReference();
        }
        if (this.m_texture3 != null) {
            this.m_texture3.removeReference();
        }
        this.m_texture3 = texture;
    }
    
    public Texture getTexture() {
        return this.m_texture;
    }
    
    public Material getMaterial() {
        return this.m_material;
    }
    
    public void setMaterial(final Material material) {
        if (this.m_material != null && this.m_material.equal(material)) {
            return;
        }
        if (material != null) {
            material.addReference();
        }
        if (this.m_material != null) {
            this.m_material.removeReference();
        }
        this.m_material = material;
        this.m_materialChanged = true;
    }
    
    public void updateMaterial() {
        this.m_materialChanged = true;
    }
    
    public void setBounds(final float top, final float left, final int width, final int height) {
        this.m_geometry.setBounds(top, left, width, height);
    }
    
    public void setTopLeft(final float top, final float left) {
        this.m_geometry.setTopLeft(top, left);
    }
    
    public void setSize(final int width, final int height) {
        this.m_geometry.setSize(width, height);
    }
    
    @Override
    public void setColor(final float r, final float g, final float b, final float a) {
        this.m_geometry.setColor(r, g, b, a);
    }
    
    public void setColor(final Position position, final float r, final float g, final float b, final float a) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        this.m_geometry.setColor(position, r, g, b, a);
    }
    
    public void setColor(final Position position, final Color color) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        this.m_geometry.setColor(position, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public float getLeft() {
        return this.m_geometry.getLeft();
    }
    
    public float getRight() {
        return this.m_geometry.getRight();
    }
    
    public float getTop() {
        return this.m_geometry.getTop();
    }
    
    public float getBottom() {
        return this.m_geometry.getBottom();
    }
    
    public float getHalfWidth() {
        return this.m_geometry.getHalfWidth();
    }
    
    public float getHalfHeight() {
        return this.m_geometry.getHalfHeight();
    }
    
    public int getWidth() {
        return this.m_geometry.getWidth();
    }
    
    public int getHeight() {
        return this.m_geometry.getHeight();
    }
    
    public float getCenterX() {
        return this.m_geometry.getCenterX();
    }
    
    public float getCenterY() {
        return this.m_geometry.getCenterY();
    }
    
    @Override
    public void update(final float timeIncrement) {
    }
    
    @Override
    public void renderWithoutEffect(final Renderer renderer) {
        if (this.m_texture != null && !this.m_texture.isReady()) {
            return;
        }
        final RenderStateManager renderStateManager = RenderStateManager.getInstance();
        renderer.setWorldMatrix(this.getTransformer().getMatrix());
        if (this.m_materialChanged && this.useFixedPipeline()) {
            this.m_materialChanged = false;
            this.m_geometry.applyMaterial(this.m_material);
        }
        this.m_preRenderStates.apply(renderer);
        final GL gl = ((GLRenderer)renderer).getDevice();
        if (this.m_texture2 != null) {
            renderStateManager.applyActiveTextureIndex(gl, 1);
            renderStateManager.applyClientActiveTextureIndex(gl, 1);
            renderStateManager.useTexture(renderer, (GLTexture)this.m_texture2);
        }
        if (this.m_texture3 != null) {
            renderStateManager.applyActiveTextureIndex(gl, 2);
            renderStateManager.applyClientActiveTextureIndex(gl, 2);
            renderStateManager.useTexture(renderer, (GLTexture)this.m_texture3);
        }
        renderStateManager.applyActiveTextureIndex(gl, 0);
        renderStateManager.applyClientActiveTextureIndex(gl, 0);
        renderStateManager.useTexture(renderer, (GLTexture)this.m_texture);
        renderStateManager.applyStates(renderer);
        if (this.m_material != null) {
            renderStateManager.applyMaterial(gl, this.m_material);
        }
        this.m_geometry.render(renderer);
        this.m_postRenderStates.apply(renderer);
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        assert this.m_material == null;
        (this.m_material = Material.Factory.newPooledInstance()).copy(Material.WHITE_NO_SPECULAR);
        this.m_materialChanged = false;
    }
    
    @Override
    protected void checkin() {
        super.checkin();
        if (this.m_material != null) {
            this.m_material.removeReference();
            this.m_material = null;
        }
        if (this.m_texture != null) {
            this.m_texture.removeReference();
            this.m_texture = null;
        }
        if (this.m_texture2 != null) {
            this.m_texture2.removeReference();
            this.m_texture2 = null;
        }
        if (this.m_texture3 != null) {
            this.m_texture3.removeReference();
            this.m_texture3 = null;
        }
        if (this.m_geometry != null) {
            this.m_geometry.removeReference();
            this.m_geometry = null;
        }
    }
    
    @Override
    public boolean fill(final VertexBufferPCT vertexBuffer) {
        if (vertexBuffer.getNumVertices() + this.m_geometry.getVertexBuffer().getNumVertices() > vertexBuffer.getMaxVertices()) {
            return false;
        }
        vertexBuffer.put(this.m_geometry.getVertexBuffer());
        return true;
    }
    
    @Override
    public void applyStates(final RenderStateManager stateManager) {
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        final RenderStateManager renderStateManager = RenderStateManager.getInstance();
        renderer.setWorldMatrix(this.getTransformer().getMatrix());
        if (this.m_materialChanged && this.useFixedPipeline()) {
            this.m_materialChanged = false;
            this.m_geometry.applyMaterial(this.m_material);
        }
        this.m_preRenderStates.apply(renderer);
        final GL gl = renderer.getDevice();
        if (this.m_texture2 != null) {
            renderStateManager.applyActiveTextureIndex(gl, 1);
            renderStateManager.applyClientActiveTextureIndex(gl, 1);
            renderStateManager.useTexture(renderer, (GLTexture)this.m_texture2);
        }
        if (this.m_texture3 != null) {
            renderStateManager.applyActiveTextureIndex(gl, 2);
            renderStateManager.applyClientActiveTextureIndex(gl, 2);
            renderStateManager.useTexture(renderer, (GLTexture)this.m_texture3);
        }
        renderStateManager.applyActiveTextureIndex(gl, 0);
        renderStateManager.applyClientActiveTextureIndex(gl, 0);
        renderStateManager.useTexture(renderer, (GLTexture)this.m_texture);
        renderStateManager.applyStates(renderer);
        if (this.m_material != null) {
            renderStateManager.applyMaterial(gl, this.m_material);
        }
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<EntitySprite>
    {
        public ObjectFactory() {
            super(EntitySprite.class);
        }
        
        @Override
        public EntitySprite create() {
            return new EntitySprite(null);
        }
    }
    
    public enum Position
    {
        BottomLeft, 
        TopLeft, 
        BottomRight, 
        TopRight;
    }
}
