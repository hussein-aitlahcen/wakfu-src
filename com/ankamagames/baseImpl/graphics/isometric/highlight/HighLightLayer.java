package com.ankamagames.baseImpl.graphics.isometric.highlight;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.*;

public class HighLightLayer
{
    private static final Logger m_logger;
    String m_name;
    public int m_priority;
    private Material m_material;
    private final TLongObjectHashMap<HighLightEntity> m_entities;
    final TLongHashSet m_addedToScene;
    private boolean m_visible;
    private Texture m_texture;
    private HighLightTextureApplication m_textureApplication;
    private static final TObjectProcedure<HighLightEntity> RELEASE_PROCEDURE;
    
    HighLightLayer(final String name, final Texture texture, final HighLightTextureApplication textureApplication) {
        super();
        this.m_entities = new TLongObjectHashMap<HighLightEntity>();
        this.m_addedToScene = new TLongHashSet();
        this.m_visible = true;
        assert texture != null;
        this.m_name = name;
        this.m_textureApplication = textureApplication;
        this.m_material = Material.Factory.newPooledInstance();
        (this.m_texture = texture).addReference();
    }
    
    public void foreachCoord(final TObjectProcedure<Point3> procedure) {
        this.m_entities.forEachKey(new TLongProcedure() {
            @Override
            public boolean execute(final long value) {
                return procedure.execute(HighLightManager.getCoord(value));
            }
        });
    }
    
    public HighLightTextureApplication getTextureApplication() {
        return this.m_textureApplication;
    }
    
    public void setTexture(final Texture texture, final HighLightTextureApplication textureApplication) {
        final boolean needRebuildSize = texture != this.m_texture;
        texture.addReference();
        this.m_texture.removeReference();
        this.m_texture = texture;
        this.m_textureApplication = textureApplication;
        final TLongObjectIterator<HighLightEntity> iter = this.m_entities.iterator();
        while (iter.hasNext()) {
            iter.advance();
            final HighLightEntity entity = iter.value();
            entity.setTexture(0, this.m_texture);
            if (needRebuildSize) {
                final GeometryMesh mesh = (GeometryMesh)entity.getGeometry(0);
                this.setGeometrySize(mesh);
                entity.m_transformed = false;
            }
        }
    }
    
    public boolean isEmpty() {
        return this.m_entities.isEmpty();
    }
    
    public float[] getColor() {
        return this.m_material.getDiffuseColor();
    }
    
    public void setColor(final float[] color) {
        this.m_material.setDiffuseColor(color);
        final TLongObjectIterator<HighLightEntity> iter = this.m_entities.iterator();
        while (iter.hasNext()) {
            iter.advance();
            final HighLightEntity entity = iter.value();
            final GeometryMesh mesh = (GeometryMesh)entity.getGeometry(0);
            mesh.setColor(color[0], color[1], color[2], color[3]);
            entity.m_transformed = false;
        }
    }
    
    final Point2i getInitialTextureSize() {
        return this.m_texture.getSize();
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
    }
    
    boolean contains(final long hash) {
        return this.m_entities.contains(hash);
    }
    
    public boolean contains(final int x, final int y, final short z) {
        return this.contains(HighLightManager.getHandle(x, y, z));
    }
    
    final HighLightEntity getEntity(final int x, final int y, final int z) {
        return this.getEntity(HighLightManager.getHandle(x, y, z));
    }
    
    HighLightEntity getEntity(final long handle) {
        return this.m_entities.get(handle);
    }
    
    public void clear() {
        if (!this.m_entities.isEmpty()) {
            this.m_entities.forEachValue(HighLightLayer.RELEASE_PROCEDURE);
        }
        this.m_entities.clear();
    }
    
    public void add(final int x, final int y, final short z) {
        final long handle = HighLightManager.getHandle(x, y, z);
        this.add(handle);
    }
    
    public void remove(final int x, final int y, final short z) {
        final long handle = HighLightManager.getHandle(x, y, z);
        this.remove(handle);
    }
    
    void add(final long handle) {
        if (this.m_entities.get(handle) != null) {
            return;
        }
        try {
            final HighLightEntity entity = HighLightEntity.Factory.newPooledInstance();
            final GeometryMesh geometryMesh = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
            assert entity.getNumReferences() == 0;
            assert geometryMesh.getNumReferences() == 0;
            geometryMesh.create(GeometryMesh.MeshType.Quad, 4, IndexBuffer.QUAD_INDICES);
            this.setGeometrySize(geometryMesh);
            geometryMesh.setColor(0.5f, 0.5f, 0.5f, 1.0f);
            final TransformerSRT transformer = new TransformerSRT();
            transformer.setIdentity();
            entity.getTransformer().addTransformer(transformer);
            final Material material = Material.WHITE_NO_SPECULAR;
            entity.addTexturedGeometry(geometryMesh, this.m_texture, material);
            geometryMesh.removeReference();
            entity.setEffect(EffectManager.getInstance().getBaseEffect(), FxConstants.TRANSFORM_TECHNIQUE, FxConstants.COLOR_SCALE_FOR_WORLD_PARAMS);
            this.m_entities.put(handle, entity);
        }
        catch (Exception e) {
            HighLightLayer.m_logger.error((Object)("probl\u00e8me cr\u00e9ation highlight entity layer=" + this.m_name), (Throwable)e);
        }
    }
    
    private void setGeometrySize(final GeometryMesh geometryMesh) {
        final VertexBufferPCT vertexBuffer = geometryMesh.getVertexBuffer();
        vertexBuffer.setNumVertices(4);
        final float right = this.m_texture.getSize().getX() / this.m_texture.getPowerOfTwoSize().getX();
        final float bottom = this.m_texture.getSize().getY() / this.m_texture.getPowerOfTwoSize().getY();
        vertexBuffer.setVertexTexCoord0(0, 0.0f, 0.0f);
        vertexBuffer.setVertexTexCoord0(1, 0.0f, bottom);
        vertexBuffer.setVertexTexCoord0(2, right, bottom);
        vertexBuffer.setVertexTexCoord0(3, right, 0.0f);
    }
    
    void remove(final long handle) {
        if (this.m_entities.isEmpty()) {
            return;
        }
        final HighLightEntity entity = this.m_entities.remove(handle);
        if (entity != null) {
            entity.removeReference();
            entity.m_transformed = false;
        }
    }
    
    void forEachEntity(final TObjectProcedure<HighLightEntity> procedure) {
        if (!this.m_entities.isEmpty()) {
            this.m_entities.forEachValue(procedure);
        }
    }
    
    void release() {
        this.clear();
        this.m_material.removeReference();
        this.m_texture.removeReference();
        this.m_material = null;
        this.m_texture = null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.m_name).append(" cellCount=").append(this.m_entities.size());
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HighLightLayer.class);
        RELEASE_PROCEDURE = new TObjectProcedure<HighLightEntity>() {
            @Override
            public boolean execute(final HighLightEntity entity) {
                entity.removeReference();
                entity.m_transformed = false;
                assert entity.getNumReferences() < 0;
                return true;
            }
        };
    }
}
