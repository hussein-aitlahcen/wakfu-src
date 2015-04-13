package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.image.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.common.*;

public class PlainProgressBarMesh extends AbstractProgressBarMesh
{
    private Entity3D m_entity;
    private Color m_color;
    private Color m_borderColor;
    private final Insets m_border;
    private boolean m_horizontal;
    private VertexBufferPCT m_vertexBuffer;
    private VertexBufferPCT m_centerVertexBuffer;
    private static final IndexBuffer m_indexBuffer;
    private static final IndexBuffer m_centerIndexBuffer;
    
    public PlainProgressBarMesh() {
        super();
        this.m_border = new Insets(0, 0, 0, 0);
        this.m_horizontal = true;
    }
    
    @Override
    public float getDeltaAngle() {
        return 0.0f;
    }
    
    @Override
    public void setDeltaAngle(final float deltaAngle) {
    }
    
    @Override
    public void setPosition(final Alignment9 position) {
    }
    
    @Override
    public Alignment9 getPosition() {
        return null;
    }
    
    @Override
    public void setPixmaps(final Pixmap northWest, final Pixmap north, final Pixmap northEast, final Pixmap west, final Pixmap center, final Pixmap east, final Pixmap southWest, final Pixmap south, final Pixmap southEast) {
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
    public void setBorderColor(final Color c) {
        if (this.m_borderColor == c) {
            return;
        }
        this.m_borderColor = c;
        this.updateColor();
    }
    
    @Override
    public Color getBorderColor() {
        return this.m_borderColor;
    }
    
    @Override
    public void setBorder(final Insets border) {
        this.m_border.set(border.top, border.left, border.bottom, border.right);
    }
    
    @Override
    public Insets getBorder() {
        return this.m_border;
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
        for (int i = 0; i < this.m_centerVertexBuffer.getMaxVertices(); ++i) {
            this.m_centerVertexBuffer.setVertexColor(i, r, g, b, a);
        }
        float ba;
        float bb;
        float br;
        float bg;
        if (this.m_borderColor == null) {
            bg = (br = (bb = (ba = 1.0f)));
        }
        else {
            br = this.m_borderColor.getRed();
            bg = this.m_borderColor.getGreen();
            bb = this.m_borderColor.getBlue();
            ba = this.m_borderColor.getAlpha();
        }
        if (this.m_modulationColor != null) {
            br *= this.m_modulationColor.getRed();
            bg *= this.m_modulationColor.getGreen();
            bb *= this.m_modulationColor.getBlue();
            ba *= this.m_modulationColor.getAlpha();
        }
        for (int j = 0; j < this.m_vertexBuffer.getMaxVertices(); ++j) {
            this.m_vertexBuffer.setVertexColor(j, br, bg, bb, ba);
        }
    }
    
    @Override
    public final Color getColor() {
        return this.m_color;
    }
    
    @Override
    public void setFullCirclePercentage(final float percentage) {
    }
    
    @Override
    public float getFullCirclePercentage() {
        return 0.0f;
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    @Override
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
    }
    
    @Override
    public void setGeometry(final int x, final int y, int width, int height, final float value) {
        width *= (int)(this.m_horizontal ? value : 1.0f);
        height *= (int)(this.m_horizontal ? 1.0f : value);
        width = Math.max(width, this.m_border.left + this.m_border.right);
        height = Math.max(height, this.m_border.top + this.m_border.bottom);
        final int top = y + height;
        this.m_vertexBuffer.setVertexPosition(0, x, y);
        this.m_vertexBuffer.setVertexPosition(1, x + this.m_border.left, y);
        this.m_vertexBuffer.setVertexPosition(2, x + this.m_border.left, y + this.m_border.bottom);
        this.m_vertexBuffer.setVertexPosition(3, x, y + this.m_border.bottom);
        this.m_vertexBuffer.setVertexPosition(4, x + this.m_border.left, top - this.m_border.top);
        this.m_vertexBuffer.setVertexPosition(5, x, top - this.m_border.top);
        this.m_vertexBuffer.setVertexPosition(6, x, top);
        this.m_vertexBuffer.setVertexPosition(7, x + this.m_border.left, top);
        this.m_vertexBuffer.setVertexPosition(8, x + width - this.m_border.right, top);
        this.m_vertexBuffer.setVertexPosition(9, x + width, top);
        this.m_vertexBuffer.setVertexPosition(10, x + width, top - this.m_border.top);
        this.m_vertexBuffer.setVertexPosition(11, x + width - this.m_border.right, top - this.m_border.top);
        this.m_vertexBuffer.setVertexPosition(12, x + width - this.m_border.right, y + this.m_border.bottom);
        this.m_vertexBuffer.setVertexPosition(13, x + width, y + this.m_border.bottom);
        this.m_vertexBuffer.setVertexPosition(14, x + width, y);
        this.m_vertexBuffer.setVertexPosition(15, x + width - this.m_border.right, y);
        this.m_centerVertexBuffer.setVertexPosition(0, x + this.m_border.left, y + this.m_border.bottom);
        this.m_centerVertexBuffer.setVertexPosition(1, x + width - this.m_border.right, y + this.m_border.bottom);
        this.m_centerVertexBuffer.setVertexPosition(2, x + width - this.m_border.right, top - this.m_border.top);
        this.m_centerVertexBuffer.setVertexPosition(3, x + this.m_border.left, top - this.m_border.top);
    }
    
    @Override
    public final Entity getEntity() {
        return this.m_entity;
    }
    
    @Override
    public final void onCheckOut() {
        assert this.m_entity == null : "Object is already checked out";
        this.m_entity = Entity3D.Factory.newPooledInstance();
        GeometryMesh geometry = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
        this.m_entity.addGeometry(geometry);
        geometry.removeReference();
        assert this.m_vertexBuffer == null;
        this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(16);
        geometry.create(GeometryMesh.MeshType.Triangle, this.m_vertexBuffer, PlainProgressBarMesh.m_indexBuffer);
        geometry = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
        this.m_entity.addGeometry(geometry);
        geometry.removeReference();
        assert this.m_centerVertexBuffer == null;
        this.m_centerVertexBuffer = VertexBufferPCT.Factory.newPooledInstance(4);
        geometry.create(GeometryMesh.MeshType.Triangle, this.m_centerVertexBuffer, PlainProgressBarMesh.m_centerIndexBuffer);
    }
    
    @Override
    public final void onCheckIn() {
        super.onCheckIn();
        this.m_color = null;
        this.m_borderColor = null;
        this.m_border.set(0, 0, 0, 0);
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_vertexBuffer.removeReference();
        this.m_vertexBuffer = null;
        this.m_centerVertexBuffer.removeReference();
        this.m_centerVertexBuffer = null;
    }
    
    static {
        final short[] indices = { 5, 4, 7, 6, 5, 7, 4, 11, 8, 7, 4, 8, 11, 10, 9, 8, 11, 9, 3, 2, 4, 5, 3, 4, 12, 13, 10, 11, 12, 10, 0, 1, 2, 3, 0, 2, 1, 15, 12, 2, 1, 12, 15, 14, 13, 12, 15, 13 };
        m_indexBuffer = new IndexBuffer(indices);
        final short[] centerIndices = { 0, 1, 2, 3, 0, 2 };
        m_centerIndexBuffer = new IndexBuffer(centerIndices);
    }
}
