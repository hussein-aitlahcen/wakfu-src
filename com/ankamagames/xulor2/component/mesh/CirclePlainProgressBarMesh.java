package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.util.alignment.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.common.*;

public class CirclePlainProgressBarMesh extends AbstractProgressBarMesh
{
    private static byte BASE_STRIP_SIZE;
    private Entity3D m_entity;
    private VertexBufferPCT m_vertexBuffer;
    private IndexBuffer m_indexBuffer;
    private Color m_color;
    private float m_innerRadiusFactor;
    private float m_outerRadiusFactor;
    private byte m_stripSize;
    private float m_deltaAngle;
    private float m_fullCirclePercentage;
    protected Pixmap m_pixmap;
    
    public CirclePlainProgressBarMesh() {
        super();
        this.m_innerRadiusFactor = 0.5f;
        this.m_outerRadiusFactor = 1.0f;
        this.m_stripSize = CirclePlainProgressBarMesh.BASE_STRIP_SIZE;
        this.m_deltaAngle = 1.5707964f;
        this.m_fullCirclePercentage = 1.0f;
        this.m_pixmap = null;
    }
    
    @Override
    public float getDeltaAngle() {
        return this.m_deltaAngle;
    }
    
    @Override
    public void setDeltaAngle(final float deltaAngle) {
        this.m_deltaAngle = deltaAngle;
    }
    
    @Override
    public void setPosition(final Alignment9 position) {
    }
    
    @Override
    public Alignment9 getPosition() {
        return null;
    }
    
    @Override
    public final void setColor(final Color color) {
        if (this.m_color == color) {
            return;
        }
        this.m_color = color;
        this.updateColor();
    }
    
    @Override
    public void setModulationColor(final Color c) {
        super.setModulationColor(c);
        this.updateColor();
    }
    
    private void updateColor() {
        float a;
        float b;
        float r;
        float g;
        if (this.m_color == null) {
            g = (r = (b = (a = 1.0f)));
        }
        else {
            r = this.m_color.getRed();
            g = this.m_color.getGreen();
            b = this.m_color.getBlue();
            a = this.m_color.getAlpha();
        }
        if (this.m_modulationColor != null) {
            r *= this.m_modulationColor.getRed();
            g *= this.m_modulationColor.getGreen();
            b *= this.m_modulationColor.getBlue();
            a *= this.m_modulationColor.getAlpha();
        }
        for (int i = 0; i < this.m_vertexBuffer.getMaxVertices(); ++i) {
            this.m_vertexBuffer.setVertexColor(i, r, g, b, a);
        }
    }
    
    @Override
    public float getFullCirclePercentage() {
        return this.m_fullCirclePercentage;
    }
    
    @Override
    public void setFullCirclePercentage(float fullCirclePercentage) {
        if (fullCirclePercentage < 0.0f) {
            fullCirclePercentage = 0.0f;
        }
        else if (fullCirclePercentage > 1.0f) {
            fullCirclePercentage = 1.0f;
        }
        this.m_fullCirclePercentage = fullCirclePercentage;
    }
    
    @Override
    public final Color getColor() {
        return this.m_color;
    }
    
    public float getInnerRadiusFactor() {
        return this.m_innerRadiusFactor;
    }
    
    public void setInnerRadiusFactor(final float innerRadiusFactor) {
        this.m_innerRadiusFactor = innerRadiusFactor;
    }
    
    public float getOuterRadiusFactor() {
        return this.m_outerRadiusFactor;
    }
    
    public void setOuterRadiusFactor(final float outerRadiusFactor) {
        this.m_outerRadiusFactor = outerRadiusFactor;
    }
    
    public boolean isHorizontal() {
        return false;
    }
    
    @Override
    public void setHorizontal(final boolean horizontal) {
    }
    
    @Override
    public void setBorderColor(final Color c) {
    }
    
    @Override
    public Color getBorderColor() {
        return null;
    }
    
    @Override
    public void setBorder(final Insets border) {
    }
    
    @Override
    public Insets getBorder() {
        return null;
    }
    
    @Override
    public void setPixmaps(final Pixmap northWest, final Pixmap north, final Pixmap northEast, final Pixmap west, final Pixmap center, final Pixmap east, final Pixmap southWest, final Pixmap south, final Pixmap southEast) {
        this.m_pixmap = center;
        this.m_entity.setTexture(0, this.m_pixmap.getTexture());
        this.computeVertices(this.m_vertexBuffer, this.m_vertexBuffer.getNumVertices());
    }
    
    @Override
    public void setGeometry(final int x, final int y, final int width, final int height, final float value) {
        final float fullStripAngle = -(float)(6.283185307179586 * this.m_fullCirclePercentage) / this.m_stripSize;
        final int fullStrips = (int)Math.floor(this.m_stripSize * value) + 1;
        final float remainingAngle = -(float)(6.283185307179586 * this.m_fullCirclePercentage * value);
        final int vertices = (fullStrips + ((fullStrips <= this.m_stripSize) ? 1 : 0)) * 2;
        if (vertices < 4) {
            this.updateVerticesAndIndices(0);
            return;
        }
        this.updateVerticesAndIndices(vertices);
        final int centerX = width / 2 + x;
        final int centerY = height / 2 + y;
        final float radius = Math.min(width, height) / 2.0f;
        final float innerRadius = radius * this.m_innerRadiusFactor;
        final float outerRadius = radius * this.m_outerRadiusFactor;
        for (int i = 0; i < fullStrips; ++i) {
            final float cosA = (float)Math.cos(fullStripAngle * i + this.m_deltaAngle);
            final float sinA = (float)Math.sin(fullStripAngle * i + this.m_deltaAngle);
            final float innerX = cosA * innerRadius + centerX;
            final float innerY = sinA * innerRadius + centerY;
            final float outerX = cosA * outerRadius + centerX;
            final float outerY = sinA * outerRadius + centerY;
            this.m_vertexBuffer.setVertexPosition(i * 2, innerX, innerY);
            this.m_vertexBuffer.setVertexPosition(i * 2 + 1, outerX, outerY);
        }
        if (fullStrips < this.m_stripSize + 1) {
            final int i = fullStrips;
            final float cosA = (float)Math.cos(remainingAngle + this.m_deltaAngle);
            final float sinA = (float)Math.sin(remainingAngle + this.m_deltaAngle);
            final float innerX = cosA * innerRadius + centerX;
            final float innerY = sinA * innerRadius + centerY;
            final float outerX = cosA * outerRadius + centerX;
            final float outerY = sinA * outerRadius + centerY;
            this.m_vertexBuffer.setVertexPosition(i * 2, innerX, innerY);
            this.m_vertexBuffer.setVertexPosition(i * 2 + 1, outerX, outerY);
        }
    }
    
    @Override
    public final Entity getEntity() {
        return this.m_entity;
    }
    
    private void updateVerticesAndIndices(final int vertices) {
        if (vertices != this.m_vertexBuffer.getNumVertices()) {
            this.computeVertices(this.m_vertexBuffer, vertices);
        }
    }
    
    protected void computeVertices(final VertexBufferPCT buf, final int vertices) {
        buf.setNumVertices(vertices);
        if (this.m_pixmap != null) {
            boolean outer = false;
            for (int i = 0; i < vertices; ++i) {
                final boolean mirror = i % 4 < 2;
                outer = !outer;
                final float u = mirror ? this.m_pixmap.getRight() : this.m_pixmap.getLeft();
                final float v = outer ? this.m_pixmap.getTop() : this.m_pixmap.getBottom();
                buf.setVertexTexCoord0(i, u, v);
            }
        }
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null;
        assert this.m_vertexBuffer == null;
        assert this.m_indexBuffer == null;
        this.m_entity = Entity3D.Factory.newPooledInstance();
        final GeometryMesh geom = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
        final int size = (this.m_stripSize + 1) * 2;
        this.m_vertexBuffer = VertexBufferPCT.Factory.newInstance(size);
        this.m_indexBuffer = IndexBuffer.INDICES;
        this.m_vertexBuffer.setNumVertices(size);
        this.setColor(new Color(Color.WHITE));
        geom.create(GeometryMesh.MeshType.TriangleStrip, this.m_vertexBuffer, this.m_indexBuffer);
        this.m_entity.addGeometry(geom);
        geom.removeReference();
    }
    
    @Override
    public final void onCheckIn() {
        super.onCheckIn();
        this.m_vertexBuffer.removeReference();
        this.m_vertexBuffer = null;
        this.m_indexBuffer = null;
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_color = null;
        this.m_pixmap = null;
    }
    
    static {
        CirclePlainProgressBarMesh.BASE_STRIP_SIZE = 32;
    }
}
