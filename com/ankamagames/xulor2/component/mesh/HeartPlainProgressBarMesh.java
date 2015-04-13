package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.common.*;

public class HeartPlainProgressBarMesh extends AbstractProgressBarMesh
{
    private Entity3D m_entity;
    private VertexBufferPCT m_vertexBuffer;
    private IndexBuffer m_indexBuffer;
    private Color m_color;
    private Alignment9 m_heartPosition;
    
    public HeartPlainProgressBarMesh() {
        super();
        this.m_heartPosition = Alignment9.CENTER;
    }
    
    @Override
    public float getDeltaAngle() {
        return 0.0f;
    }
    
    @Override
    public void setDeltaAngle(final float deltaAngle) {
    }
    
    @Override
    public void setPixmaps(final Pixmap northWest, final Pixmap north, final Pixmap northEast, final Pixmap west, final Pixmap center, final Pixmap east, final Pixmap southWest, final Pixmap south, final Pixmap southEast) {
    }
    
    @Override
    public void setBorderColor(final Color c) {
    }
    
    @Override
    public Insets getBorder() {
        return null;
    }
    
    @Override
    public Color getBorderColor() {
        return null;
    }
    
    @Override
    public void setBorder(final Insets border) {
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
    public final Color getColor() {
        return this.m_color;
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
    
    public boolean isHorizontal() {
        return false;
    }
    
    @Override
    public void setHorizontal(final boolean horizontal) {
    }
    
    @Override
    public void setFullCirclePercentage(final float percentage) {
    }
    
    @Override
    public float getFullCirclePercentage() {
        return 0.0f;
    }
    
    @Override
    public void setPosition(final Alignment9 position) {
        this.m_heartPosition = position;
    }
    
    @Override
    public Alignment9 getPosition() {
        return this.m_heartPosition;
    }
    
    private float[] generateCoordinatesFactors(final float value) {
        float[] coords = null;
        if (value == 0.0f) {
            coords = new float[0];
        }
        else if (value <= 0.3f) {
            coords = new float[6];
        }
        else if (value <= 0.6f) {
            coords = new float[10];
        }
        else if (value <= 0.9f) {
            coords = new float[14];
        }
        else {
            coords = new float[18];
        }
        if (value > 0.0f) {
            final float factor = Math.min(1.0f, value / 0.3f);
            coords[0] = 0.5f;
            coords[1] = 0.0f;
            coords[2] = 0.5f - 0.3f * factor;
            coords[3] = 0.3f * factor;
            coords[4] = 1.0f - coords[2];
            coords[5] = coords[3];
        }
        if (value > 0.3f) {
            final float factor = Math.min(1.0f, (value - 0.3f) / 0.3f);
            coords[6] = 0.2f * (1.0f - factor);
            coords[7] = 0.3f + 0.3f * factor;
            coords[8] = 1.0f - coords[6];
            coords[9] = coords[7];
        }
        if (value > 0.6f) {
            final float factor = Math.min(1.0f, (value - 0.6f) / 0.3f);
            coords[10] = 0.0f;
            coords[11] = 0.6f + 0.3f * factor;
            coords[12] = 1.0f;
            coords[13] = coords[11];
        }
        if (value > 0.9f) {
            final float factor = Math.min(1.0f, (value - 0.9f) / 0.1f);
            coords[14] = 0.3f * factor;
            coords[15] = 0.9f + 0.1f * factor;
            coords[16] = 1.0f - coords[14];
            coords[17] = coords[15];
        }
        for (int i = 0, size = coords.length; i < size; i += 2) {
            if (this.m_heartPosition.isWest()) {
                coords[i] = Math.min(coords[i], 0.5f);
            }
            else if (this.m_heartPosition.isEast()) {
                coords[i] = Math.max(coords[i], 0.5f);
            }
        }
        return coords;
    }
    
    @Override
    public void setGeometry(final int x, final int y, final int width, final int height, final float value) {
        final float[] coords = this.generateCoordinatesFactors(value);
        this.updateVerticesAndIndices(coords.length / 2);
        for (int vertices = coords.length / 2, i = 0; i < vertices; ++i) {
            final float vx = x + width * coords[i * 2];
            final float vy = y + height * coords[i * 2 + 1];
            this.m_vertexBuffer.setVertexPosition(i, vx, vy);
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
    
    private void computeVertices(final VertexBufferPCT buf, final int vertices) {
        buf.setNumVertices(vertices);
    }
    
    @Override
    public final void onCheckOut() {
        assert this.m_entity == null;
        assert this.m_vertexBuffer == null;
        assert this.m_indexBuffer == null;
        this.m_entity = Entity3D.Factory.newPooledInstance();
        final GeometryMesh geom = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
        this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(9);
        this.m_indexBuffer = IndexBuffer.INDICES;
        this.m_vertexBuffer.setNumVertices(0);
        this.setColor(new Color(Color.BLACK));
        geom.create(GeometryMesh.MeshType.TriangleStrip, this.m_vertexBuffer, this.m_indexBuffer);
        this.m_entity.addGeometry(geom);
        geom.removeReference();
    }
    
    @Override
    public final void onCheckIn() {
        super.onCheckIn();
        this.m_color = null;
        this.m_vertexBuffer.removeReference();
        this.m_vertexBuffer = null;
        this.m_indexBuffer = null;
        this.m_entity.removeReference();
        this.m_entity = null;
    }
}
