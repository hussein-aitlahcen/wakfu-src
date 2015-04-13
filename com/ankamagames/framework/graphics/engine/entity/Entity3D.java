package com.ankamagames.framework.graphics.engine.entity;

import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.material.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import javax.media.opengl.*;

public class Entity3D extends Entity
{
    public static final ObjectFactory Factory;
    private final ArrayList<TexturedGeometry> m_texturedGeometries;
    
    protected Entity3D() {
        super();
        this.m_texturedGeometries = new ArrayList<TexturedGeometry>(2);
    }
    
    public void clear() {
        for (int texturedGeometriesSize = this.m_texturedGeometries.size(), i = 0; i < texturedGeometriesSize; ++i) {
            this.m_texturedGeometries.get(i).destroy();
        }
        this.m_texturedGeometries.clear();
    }
    
    public final int getNumGeometries() {
        return this.m_texturedGeometries.size();
    }
    
    public final int addGeometry(final Geometry geometry) {
        this.m_texturedGeometries.add(new TexturedGeometry(geometry, null, null));
        return this.m_texturedGeometries.size() - 1;
    }
    
    public final int addTexturedGeometry(final Geometry geometry, final Texture texture, final Material material) {
        this.m_texturedGeometries.add(new TexturedGeometry(geometry, texture, material));
        return this.m_texturedGeometries.size() - 1;
    }
    
    public final void removeTextureGeometry(final Geometry geometry) {
        for (int texturedGeometriesSize = this.m_texturedGeometries.size(), i = 0; i < texturedGeometriesSize; ++i) {
            final TexturedGeometry texturedGeometry = this.m_texturedGeometries.get(i);
            if (texturedGeometry.getGeometry() == geometry) {
                texturedGeometry.destroy();
                this.m_texturedGeometries.remove(texturedGeometry);
                break;
            }
        }
    }
    
    public final Geometry getGeometry(final int index) {
        return this.m_texturedGeometries.get(index).getGeometry();
    }
    
    public final void setTexture(final int index, final Texture texture) {
        assert index < this.m_texturedGeometries.size();
        this.m_texturedGeometries.get(index).setTexture(texture);
    }
    
    public final Texture getTexture(final int index) {
        return this.m_texturedGeometries.get(index).getTexture();
    }
    
    public final void updateMaterial(final Material material) {
        for (int count = this.m_texturedGeometries.size(), i = 0; i < count; ++i) {
            final TexturedGeometry geometry = this.m_texturedGeometries.get(i);
            if (geometry.getMaterial() == material) {
                geometry.setMaterialChanged();
            }
        }
    }
    
    public final Material getMaterial(final int index) {
        return this.m_texturedGeometries.get(index).getMaterial();
    }
    
    @Override
    public final void setColor(final float r, final float g, final float b, final float a) {
        for (final TexturedGeometry texturedGeometry : this.m_texturedGeometries) {
            final Geometry geometry = texturedGeometry.getGeometry();
            if (geometry == null) {
                continue;
            }
            geometry.setColor(r, g, b, a);
        }
    }
    
    @Override
    public void update(final float timeIncrement) {
    }
    
    @Override
    public void renderWithoutEffect(final Renderer renderer) {
        if (this.getNumGeometries() == 0) {
            return;
        }
        renderer.setWorldMatrix(this.getTransformer().getMatrix());
        final boolean useFixedPipeline = this.useFixedPipeline();
        this.m_preRenderStates.apply(renderer);
        final int texturedGeometriesSize = this.m_texturedGeometries.size();
        final GLRenderer glRenderer = (GLRenderer)renderer;
        for (int i = 0; i < texturedGeometriesSize; ++i) {
            this.m_texturedGeometries.get(i).render(glRenderer, useFixedPipeline);
        }
        this.m_postRenderStates.apply(renderer);
    }
    
    @Override
    protected void checkin() {
        super.checkin();
        this.clear();
        this.m_texturedGeometries.trimToSize();
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<Entity3D>
    {
        public ObjectFactory() {
            super(Entity3D.class);
        }
        
        @Override
        public Entity3D create() {
            return new Entity3D();
        }
    }
    
    private static class TexturedGeometry
    {
        private Geometry m_geometry;
        private Texture m_texture;
        private Material m_material;
        private boolean m_materialChanged;
        
        TexturedGeometry(final Geometry geometry, final Texture texture, final Material material) {
            super();
            this.setTexture(texture);
            if (geometry != null) {
                geometry.addReference();
            }
            this.m_geometry = geometry;
            this.setMaterial(material);
        }
        
        public final void destroy() {
            if (this.m_texture != null) {
                this.m_texture.removeReference();
                this.m_texture = null;
            }
            if (this.m_geometry != null) {
                this.m_geometry.removeReference();
                this.m_geometry = null;
            }
            if (this.m_material != null) {
                this.m_material.removeReference();
                this.m_material = null;
            }
        }
        
        Geometry getGeometry() {
            return this.m_geometry;
        }
        
        Texture getTexture() {
            return this.m_texture;
        }
        
        void setTexture(final Texture texture) {
            if (texture == this.m_texture) {
                return;
            }
            if (texture != null) {
                texture.addReference();
            }
            if (this.m_texture != null) {
                this.m_texture.removeReference();
            }
            this.m_texture = texture;
        }
        
        Material getMaterial() {
            return this.m_material;
        }
        
        void setMaterial(final Material material) {
            if (this.m_material == material) {
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
        
        void setMaterialChanged() {
            this.m_materialChanged = true;
        }
        
        public final void render(final GLRenderer renderer, final boolean useFixedPipeline) {
            if (this.m_texture != null && !this.m_texture.isReady()) {
                return;
            }
            if (this.m_geometry == null) {
                return;
            }
            final RenderStateManager renderStateManager = RenderStateManager.getInstance();
            renderStateManager.useTexture(renderer, (GLTexture)this.m_texture);
            if (this.m_material != null) {
                renderStateManager.applyMaterial(renderer.getDevice(), this.m_material);
                if (this.m_materialChanged && useFixedPipeline) {
                    this.m_materialChanged = false;
                    this.m_geometry.applyMaterial(this.m_material);
                }
            }
            this.m_geometry.render(renderer);
        }
    }
}
