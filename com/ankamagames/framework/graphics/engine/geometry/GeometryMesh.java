package com.ankamagames.framework.graphics.engine.geometry;

import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.*;
import java.nio.*;
import java.util.*;

public abstract class GeometryMesh extends Geometry
{
    protected IndexBuffer m_indexBuffer;
    protected VertexBufferPCT m_vertexBuffer;
    protected MeshType m_meshType;
    protected float[] m_colors;
    protected int m_nbColor;
    private static final float[] _tmpColor;
    
    protected GeometryMesh() {
        super();
        this.m_meshType = MeshType.Triangle;
    }
    
    public void create(final MeshType meshType, final int maxVertices, final IndexBuffer indexBuffer) {
        this.m_meshType = meshType;
        if (this.m_vertexBuffer != null) {
            this.m_vertexBuffer.removeReference();
        }
        assert maxVertices <= indexBuffer.getMaxIndices();
        this.m_indexBuffer = indexBuffer;
        this.setColorsFromVertexBuffer(this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(maxVertices));
    }
    
    public void create(final MeshType meshType, final VertexBufferPCT vertexBuffer, final IndexBuffer indexBuffer) {
        this.m_meshType = meshType;
        vertexBuffer.addReference();
        if (this.m_vertexBuffer != null) {
            this.m_vertexBuffer.removeReference();
        }
        this.m_vertexBuffer = vertexBuffer;
        this.m_indexBuffer = indexBuffer;
        this.setColorsFromVertexBuffer(this.m_vertexBuffer);
    }
    
    public void create(final MeshType meshType, final VertexBufferPCT vertexBuffer, final IndexBuffer indexBuffer, final float[] colors) {
        this.m_meshType = meshType;
        assert this.m_vertexBuffer == null;
        assert this.m_indexBuffer == null;
        this.m_vertexBuffer = vertexBuffer;
        this.m_indexBuffer = indexBuffer;
        this.m_vertexBuffer.addReference();
        this.releaseColors();
        final int numVertices = vertexBuffer.getNumVertices();
        if (numVertices != 0) {
            this.allocateColorFromBuffer(colors, numVertices);
        }
    }
    
    public final VertexBufferPCT getVertexBuffer() {
        return this.m_vertexBuffer;
    }
    
    public final MeshType getMeshType() {
        return this.m_meshType;
    }
    
    @Override
    public void setColor(final float r, final float g, final float b, final float a) {
        if (this.m_vertexBuffer == null) {
            return;
        }
        if (this.m_vertexBuffer.getColorBuffer() == null) {
            return;
        }
        GeometryMesh._tmpColor[0] = r;
        GeometryMesh._tmpColor[1] = g;
        GeometryMesh._tmpColor[2] = b;
        GeometryMesh._tmpColor[3] = a;
        for (int i = 0; i < this.m_vertexBuffer.getNumVertices(); ++i) {
            this.m_vertexBuffer.setVertexColor(i, GeometryMesh._tmpColor);
        }
        this.setColorsFromVertexBuffer(this.m_vertexBuffer);
    }
    
    @Override
    public void applyMaterial(final Material material) {
        final int numVertices = this.m_vertexBuffer.getNumVertices();
        if (this.m_nbColor != numVertices) {
            return;
        }
        final float[] diffuse = material.getDiffuseColor();
        final float[] specular = material.getSpecularColor();
        this.m_vertexBuffer.rewindColorBuffer();
        for (int i = 0; i < numVertices; ++i) {
            GeometryMesh._tmpColor[0] = this.m_colors[i * 4] * diffuse[0] + specular[0];
            GeometryMesh._tmpColor[1] = this.m_colors[i * 4 + 1] * diffuse[1] + specular[1];
            GeometryMesh._tmpColor[2] = this.m_colors[i * 4 + 2] * diffuse[2] + specular[2];
            GeometryMesh._tmpColor[3] = this.m_colors[i * 4 + 3] * diffuse[3];
            this.m_vertexBuffer.putColorBuffer(GeometryMesh._tmpColor);
        }
    }
    
    @Override
    public void update(final float timeIncrement) {
    }
    
    @Override
    public abstract void render(final Renderer p0);
    
    public static int getNumVertices(final MeshType primitiveType, final int numPrimitives) {
        switch (primitiveType) {
            case Line: {
                return numPrimitives * 2;
            }
            case LineStrip: {
                return 2 + (numPrimitives - 1);
            }
            case Point: {
                return numPrimitives;
            }
            case Quad: {
                return numPrimitives * 4;
            }
            case Triangle: {
                return numPrimitives * 3;
            }
            case TriangleFan:
            case TriangleStrip: {
                return 3 + (numPrimitives - 1);
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        this.m_meshType = MeshType.Triangle;
    }
    
    @Override
    protected void checkin() {
        if (this.m_vertexBuffer != null) {
            this.m_vertexBuffer.removeReference();
            this.m_vertexBuffer = null;
        }
        this.m_indexBuffer = null;
        this.releaseColors();
        this.m_colors = null;
    }
    
    private void setColorsFromVertexBuffer(final VertexBufferPCT vertexBuffer) {
        final int numVertices = vertexBuffer.getNumVertices();
        if (numVertices == 0) {
            this.releaseColors();
            return;
        }
        this.releaseColors();
        final FloatBuffer buffer = vertexBuffer.getColorBuffer();
        this.allocateColors(numVertices);
        buffer.get(this.m_colors);
    }
    
    private void releaseColors() {
        this.m_nbColor = 0;
    }
    
    protected void allocateColors(final int nbColors) {
        final int floatBufferSize = nbColors * 4;
        if (this.m_colors == null || this.m_colors.length != floatBufferSize) {
            final float[] colors = new float[floatBufferSize];
            if (this.m_colors != null) {
                System.arraycopy(this.m_colors, 0, colors, 0, Math.min(this.m_colors.length, colors.length));
                for (int i = this.m_colors.length; i < floatBufferSize; ++i) {
                    colors[i] = 0.0f;
                }
            }
            else {
                Arrays.fill(colors, 0.0f);
            }
            this.m_colors = colors;
        }
        this.m_nbColor = nbColors;
    }
    
    public final void allocateColorFromBuffer(final float[] colorBuffer, final int numVertices) {
        this.allocateColors(numVertices);
        System.arraycopy(colorBuffer, 0, this.m_colors, 0, numVertices * 4);
    }
    
    static {
        _tmpColor = new float[4];
    }
    
    public enum MeshType
    {
        Point, 
        Line, 
        LineStrip, 
        Triangle, 
        TriangleStrip, 
        TriangleFan, 
        Quad;
    }
}
