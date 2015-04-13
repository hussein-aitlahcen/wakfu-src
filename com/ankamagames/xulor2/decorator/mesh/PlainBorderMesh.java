package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.common.*;

public class PlainBorderMesh extends AbstractBorderMesh
{
    private static final IndexBuffer m_indexBuffer;
    private VertexBufferPCT m_vertexBuffer;
    private Entity3D m_entity;
    private Color m_color;
    private Color m_modulationColor;
    
    public final void setColor(final Color color) {
        assert color != null : "Color can't be null";
        if (this.m_color == color) {
            return;
        }
        this.m_color = color;
        this.updateColor();
    }
    
    public final Color getColor() {
        return this.m_color;
    }
    
    public final void setModulationColor(final Color color) {
        if (this.m_modulationColor == color) {
            return;
        }
        this.m_modulationColor = color;
        this.updateColor();
    }
    
    public final Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    private void updateColor() {
        float r = (this.m_color != null) ? this.m_color.getRed() : 1.0f;
        float g = (this.m_color != null) ? this.m_color.getGreen() : 1.0f;
        float b = (this.m_color != null) ? this.m_color.getBlue() : 1.0f;
        float a = (this.m_color != null) ? this.m_color.getAlpha() : 1.0f;
        r *= ((this.m_modulationColor != null) ? this.m_modulationColor.getRed() : 1.0f);
        g *= ((this.m_modulationColor != null) ? this.m_modulationColor.getGreen() : 1.0f);
        b *= ((this.m_modulationColor != null) ? this.m_modulationColor.getBlue() : 1.0f);
        a *= ((this.m_modulationColor != null) ? this.m_modulationColor.getAlpha() : 1.0f);
        final float[] colors = new float[this.m_vertexBuffer.getMaxVertices() * 4];
        for (int i = 0; i < colors.length; i += 4) {
            colors[i] = r;
            colors[i + 1] = g;
            colors[i + 2] = b;
            colors[i + 3] = a;
        }
        this.m_vertexBuffer.setColorBuffer(colors);
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        final int left = margin.left + border.left;
        final int right = margin.right + border.right;
        final int top = margin.top + border.top;
        final int bottom = margin.bottom + border.bottom;
        int i = 0;
        final float[] positions = new float[this.m_vertexBuffer.getMaxVertices() * 2];
        positions[i++] = margin.left;
        positions[i++] = margin.bottom;
        positions[i++] = left;
        positions[i++] = margin.bottom;
        positions[i++] = left;
        positions[i++] = bottom;
        positions[i++] = margin.left;
        positions[i++] = bottom;
        positions[i++] = left;
        positions[i++] = size.height - top;
        positions[i++] = margin.left;
        positions[i++] = size.height - top;
        positions[i++] = margin.left;
        positions[i++] = size.height - margin.top;
        positions[i++] = left;
        positions[i++] = size.height - margin.top;
        positions[i++] = size.width - right;
        positions[i++] = size.height - margin.top;
        positions[i++] = size.width - margin.right;
        positions[i++] = size.height - margin.top;
        positions[i++] = size.width - margin.right;
        positions[i++] = size.height - top;
        positions[i++] = size.width - right;
        positions[i++] = size.height - top;
        positions[i++] = size.width - right;
        positions[i++] = bottom;
        positions[i++] = size.width - margin.right;
        positions[i++] = bottom;
        positions[i++] = size.width - margin.right;
        positions[i++] = margin.bottom;
        positions[i++] = size.width - right;
        positions[i] = margin.bottom;
        this.m_vertexBuffer.setPositionBuffer(positions);
    }
    
    @Override
    public Entity getEntity() {
        return this.m_entity;
    }
    
    @Override
    public void onCheckIn() {
        this.m_color = null;
        this.m_modulationColor = null;
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_vertexBuffer.removeReference();
        this.m_vertexBuffer = null;
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null : "Object is already checked out";
        assert this.m_vertexBuffer == null;
        this.m_entity = Entity3D.Factory.newPooledInstance();
        final GeometryMesh geometry = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
        this.m_entity.addGeometry(geometry);
        geometry.removeReference();
        this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(16);
        geometry.create(GeometryMesh.MeshType.Triangle, this.m_vertexBuffer, PlainBorderMesh.m_indexBuffer);
    }
    
    static {
        final short[] indices = { 5, 4, 7, 6, 5, 7, 4, 11, 8, 7, 4, 8, 11, 10, 9, 8, 11, 9, 3, 2, 4, 5, 3, 4, 12, 13, 10, 11, 12, 10, 0, 1, 2, 3, 0, 2, 1, 15, 12, 2, 1, 12, 15, 14, 13, 12, 15, 13 };
        m_indexBuffer = new IndexBuffer(indices);
    }
}
