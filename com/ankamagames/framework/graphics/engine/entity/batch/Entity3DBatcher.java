package com.ankamagames.framework.graphics.engine.entity.batch;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.geometry.*;

public class Entity3DBatcher
{
    private VertexBufferPCT m_vertexBuffer;
    private GLTexture m_lastTexture;
    private Entity3D m_entity;
    private int m_numGeometry;
    private BlendModes m_srcBlend;
    private BlendModes m_destBlend;
    
    public Entity3DBatcher(final BlendModes srcBlend, final BlendModes destBlend) {
        super();
        this.m_vertexBuffer = null;
        this.setBlendFunc(srcBlend, destBlend);
    }
    
    public Entity3DBatcher() {
        this(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
    }
    
    public final void setBlendFunc(final BlendModes srcBlend, final BlendModes destBlend) {
        this.m_srcBlend = srcBlend;
        this.m_destBlend = destBlend;
    }
    
    public final void release() {
        if (this.m_vertexBuffer != null) {
            this.m_vertexBuffer.removeReference();
            this.m_vertexBuffer = null;
        }
    }
    
    public final void beginAddGeometry(final Entity3D entity, final int numGeom) {
        final int numVertice = numGeom * 4;
        this.m_numGeometry = numGeom;
        if (this.m_vertexBuffer != null && this.m_vertexBuffer.getMaxVertices() != numVertice) {
            this.m_vertexBuffer.removeReference();
            this.m_vertexBuffer = null;
        }
        if (this.m_vertexBuffer == null) {
            this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(numVertice);
        }
        this.m_vertexBuffer.rewind();
        this.m_entity = entity;
    }
    
    public final void endAddGeometry() {
        this.createGeometry();
        this.m_entity = null;
        this.m_lastTexture = null;
    }
    
    private void createGeometry() {
        assert this.m_vertexBuffer != null;
        assert this.m_entity != null;
        if (this.m_vertexBuffer.getNumVertices() != 0) {
            final GLGeometryMesh geom = GLGeometryMesh.Factory.newPooledInstance();
            geom.setBlendFunc(this.m_srcBlend, this.m_destBlend);
            geom.create(GeometryMesh.MeshType.Quad, this.m_vertexBuffer, IndexBuffer.INDICES);
            this.m_entity.addTexturedGeometry(geom, this.m_lastTexture, null);
            geom.removeReference();
        }
    }
    
    public final void fillBuffer(final int left, final int top, final int width, final int height, final Pixmap pixmap, final float texCoordTop, final float texCoordLeft, final float texCoordBottom, final float texCoordRight, final Color color) {
        assert width != 0 && height != 0;
        assert pixmap != null;
        assert this.m_vertexBuffer.getNumVertices() + 4 <= this.m_vertexBuffer.getMaxVertices();
        if (this.m_lastTexture != null && this.m_lastTexture != pixmap.getTexture()) {
            this.createGeometry();
            this.m_vertexBuffer.removeReference();
            this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(this.m_numGeometry * 4);
        }
        GeometrySprite.setPositions(this.m_vertexBuffer, top, left, width, height);
        if (color != null) {
            GeometrySprite.setColor(this.m_vertexBuffer, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
        else {
            GeometrySprite.setColor(this.m_vertexBuffer, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        GeometrySprite.setTextureCoordinates(this.m_vertexBuffer, texCoordTop, texCoordLeft, texCoordBottom, texCoordRight, pixmap.getRotation());
        this.m_lastTexture = (GLTexture)pixmap.getTexture();
        --this.m_numGeometry;
        this.m_vertexBuffer.setNumVertices(4 + this.m_vertexBuffer.getNumVertices());
    }
    
    public final void fillBuffer(final int left, final int top, final int width, final int height, final Pixmap pixmap, final Color color) {
        if (pixmap == null) {
            --this.m_numGeometry;
            return;
        }
        this.fillBuffer(left, top, width, height, pixmap, pixmap.getTop(), pixmap.getLeft(), pixmap.getBottom(), pixmap.getRight(), color);
    }
}
