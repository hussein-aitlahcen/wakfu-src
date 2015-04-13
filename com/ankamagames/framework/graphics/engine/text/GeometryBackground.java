package com.ankamagames.framework.graphics.engine.text;

import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public abstract class GeometryBackground extends Geometry
{
    protected float m_scaleFactorX;
    protected float m_scaleFactorY;
    protected short[] m_indices;
    protected short[] m_borderIndices;
    protected VertexBufferPCT m_vertexBuffer;
    protected IndexBuffer m_indexBuffer;
    protected VertexBufferPCT m_borderVertexBuffer;
    protected IndexBuffer m_borderIndexBuffer;
    private int m_width;
    private int m_height;
    private final Color m_color;
    private float m_leftMargin;
    private float m_rightMargin;
    private float m_topMargin;
    private float m_bottomMargin;
    private float m_borderWidth;
    private final Color m_borderColor;
    private float m_xOffset;
    private float m_yOffset;
    private float[][] m_positionOffsets;
    private float[][] m_sizeMultipliers;
    private boolean m_updateVertices;
    
    public GeometryBackground() {
        super();
        this.m_color = new Color(1.0f, 1.0f, 1.0f, 0.7f);
        this.m_borderColor = new Color(0.06f, 0.04f, 0.03f, 0.4f);
    }
    
    public void save(final OutputBitStream bitStream) throws IOException {
        assert false : "Currently not implemented";
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        assert false : "Currently not implemented";
    }
    
    @Override
    public void update(final float timeIncrement) {
    }
    
    public final int getWidth() {
        return this.m_width;
    }
    
    public final void setWidth(final int width) {
        if (width == this.m_width) {
            return;
        }
        this.m_width = width;
        this.setNeedUpdate();
    }
    
    public final int getHeight() {
        return this.m_height;
    }
    
    public final void setHeight(final int height) {
        if (height == this.m_height) {
            return;
        }
        this.m_height = height;
        this.setNeedUpdate();
    }
    
    public float getXOffset() {
        return this.m_xOffset;
    }
    
    public void setXOffset(final float xOffset) {
        if (this.m_xOffset == xOffset) {
            return;
        }
        this.m_xOffset = xOffset;
        this.setNeedUpdate();
    }
    
    public float getYOffset() {
        return this.m_yOffset;
    }
    
    public void setYOffset(final float yOffset) {
        if (this.m_yOffset == yOffset) {
            return;
        }
        this.m_yOffset = yOffset;
        this.setNeedUpdate();
    }
    
    public final float getBorderWidth() {
        return this.m_borderWidth;
    }
    
    public final void setBorderWidth(final float borderWidth) {
        if (this.m_borderWidth == borderWidth) {
            return;
        }
        this.m_borderWidth = borderWidth;
        this.setNeedUpdate();
    }
    
    public final Color getColor() {
        return this.m_color;
    }
    
    @Override
    public final void setColor(final float r, final float g, final float b, final float a) {
        this.m_color.setFromFloat(r, g, b, a);
        this.setNeedUpdate();
    }
    
    public final Color getBorderColor() {
        return this.m_borderColor;
    }
    
    public final void setBorderColor(final float r, final float g, final float b, final float a) {
        this.m_borderColor.setFromFloat(r, g, b, a);
        this.setNeedUpdate();
    }
    
    public final void setZoom(final float zoomX, final float zoomY) {
        this.m_scaleFactorX = zoomX;
        this.m_scaleFactorY = zoomY;
        this.setNeedUpdate();
    }
    
    public final void setShape(final float[][] positionOffsets, final float[][] sizeMultipliers) {
        assert positionOffsets != null : "positionOffsets can't be null";
        assert sizeMultipliers != null : "sizeMultipliers can't be null";
        assert positionOffsets.length == sizeMultipliers.length : "positionOffsets and sizeMultipliers must have the same size";
        assert positionOffsets.length > 0 : "positionOffsets must be greater than zero";
        assert sizeMultipliers.length > 0 : "sizeMultipliers must be greater than zero";
        this.m_positionOffsets = positionOffsets;
        this.m_sizeMultipliers = sizeMultipliers;
        this.setNeedUpdate();
    }
    
    public final void setBorderIndices(final IndexBuffer indices) {
        this.m_borderIndexBuffer = indices;
        this.setNeedUpdate();
    }
    
    public final void setBackgroundIndices(final IndexBuffer indices) {
        this.m_indexBuffer = indices;
        this.setNeedUpdate();
    }
    
    public final void setMargins(final float left, final float right, final float top, final float bottom) {
        if (this.m_leftMargin == left && this.m_rightMargin == right && this.m_topMargin == top && this.m_bottomMargin == bottom) {
            return;
        }
        this.m_leftMargin = left;
        this.m_rightMargin = right;
        this.m_topMargin = top;
        this.m_bottomMargin = bottom;
        this.setNeedUpdate();
    }
    
    public final float getLeftMargin() {
        return this.m_leftMargin;
    }
    
    public final float getRightMargin() {
        return this.m_rightMargin;
    }
    
    public final float getTopMargin() {
        return this.m_topMargin;
    }
    
    public final float getBottomMargin() {
        return this.m_bottomMargin;
    }
    
    protected final void setNeedUpdate() {
        this.m_updateVertices = true;
    }
    
    protected void updateVertices() {
        if (!this.m_updateVertices) {
            return;
        }
        this.m_updateVertices = false;
        this.recreateBuffer();
        final float width = this.m_width / this.m_scaleFactorX;
        final float height = this.m_height / this.m_scaleFactorY;
        final float w = width + this.m_leftMargin + this.m_rightMargin;
        final float h = height + this.m_topMargin + this.m_bottomMargin;
        final float r = this.m_color.getRed();
        final float g = this.m_color.getGreen();
        final float b = this.m_color.getBlue();
        final float a = this.m_color.getAlpha();
        final float r_border = this.m_color.getRed();
        final float g_border = this.m_color.getGreen();
        final float b_border = this.m_color.getBlue();
        final float a_border = this.m_color.getAlpha();
        this.m_vertexBuffer.begin();
        this.m_borderVertexBuffer.begin();
        for (int i = 0; i < this.m_positionOffsets.length; ++i) {
            final float x = this.m_positionOffsets[i][0] + this.m_sizeMultipliers[i][0] * w + this.m_xOffset;
            final float y = this.m_positionOffsets[i][1] + this.m_sizeMultipliers[i][1] * h + this.m_yOffset;
            this.m_vertexBuffer.pushVertex(x, y, 0.0f, 0.0f, r, g, b, a);
            this.m_borderVertexBuffer.pushVertex(x, y, 0.0f, 0.0f, r_border, g_border, b_border, a_border);
        }
        this.m_vertexBuffer.end();
        this.m_borderVertexBuffer.end();
    }
    
    private void recreateBuffer() {
        this.m_vertexBuffer = recreate(this.m_vertexBuffer, this.m_positionOffsets.length);
        this.m_borderVertexBuffer = recreate(this.m_borderVertexBuffer, this.m_positionOffsets.length);
    }
    
    private static VertexBufferPCT recreate(final VertexBufferPCT vb, final int newLength) {
        if (vb == null) {
            return VertexBufferPCT.Factory.newPooledInstance(newLength);
        }
        if (vb.getMaxVertices() < newLength) {
            vb.removeReference();
            return VertexBufferPCT.Factory.newPooledInstance(newLength);
        }
        return vb;
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        if (this.m_color != null) {
            this.m_color.setFromFloat(1.0f, 1.0f, 1.0f, 0.7f);
        }
        if (this.m_borderColor != null) {
            this.m_borderColor.setFromFloat(0.06f, 0.04f, 0.03f, 0.4f);
        }
        this.m_leftMargin = 0.0f;
        this.m_rightMargin = 0.0f;
        this.m_topMargin = 0.0f;
        this.m_bottomMargin = 0.0f;
        this.m_scaleFactorX = 1.0f;
        this.m_scaleFactorY = 1.0f;
        this.m_borderWidth = 1.0f;
        this.setNeedUpdate();
    }
    
    @Override
    protected void checkin() {
        this.clearBuffers();
        this.m_positionOffsets = null;
        this.m_sizeMultipliers = null;
        this.m_indices = null;
        this.m_borderIndices = null;
    }
    
    private void clearBuffers() {
        if (this.m_vertexBuffer != null) {
            this.m_vertexBuffer.removeReference();
            this.m_vertexBuffer = null;
        }
        this.m_indexBuffer = null;
        if (this.m_borderVertexBuffer != null) {
            this.m_borderVertexBuffer.removeReference();
            this.m_borderVertexBuffer = null;
        }
        this.m_borderIndexBuffer = null;
    }
}
