package com.ankamagames.framework.graphics.engine.text;

import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class GeometryText extends Geometry
{
    protected Font m_font;
    protected Color m_color;
    protected ArrayList<char[]> m_lines;
    protected int m_lineHeight;
    protected Vector4 m_position;
    protected float m_offsetX;
    protected float m_offsetY;
    protected float m_alignOffsetX;
    protected float m_alignOffsetY;
    protected float m_scale;
    
    protected GeometryText() {
        super();
        this.m_scale = 1.0f;
        this.m_position = new Vector4(0.0f, 0.0f, 0.0f, 1.0f);
        this.m_color = new Color(Color.BLACK);
        this.m_offsetX = 0.0f;
        this.m_offsetY = 0.0f;
    }
    
    public void save(final OutputBitStream bitStream) throws IOException {
        assert false : "Currently not implemented";
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        assert false : "Currently not implemented";
    }
    
    public abstract int getLineWidth(final String p0);
    
    public abstract int getLineHeight(final String p0);
    
    public abstract Point2i getLineDimensions(final String p0);
    
    public float getScale() {
        return this.m_scale;
    }
    
    public void setScale(final float scale) {
        this.m_scale = scale;
    }
    
    public float getAlignOffsetX() {
        return this.m_alignOffsetX;
    }
    
    public void setAlignOffsetX(final float alignOffsetX) {
        this.m_alignOffsetX = alignOffsetX;
    }
    
    public float getAlignOffsetY() {
        return this.m_alignOffsetY;
    }
    
    public void setAlignOffsetY(final float alignOffsetY) {
        this.m_alignOffsetY = alignOffsetY;
    }
    
    public void setFont(final Font font) {
        this.m_font = font;
    }
    
    public final void setColor(final int color) {
        this.m_color.set(color);
    }
    
    @Override
    public final void setColor(final float r, final float g, final float b, final float a) {
        this.m_color.setFromFloat(r, g, b, a);
    }
    
    public final Color getColor() {
        return this.m_color;
    }
    
    public final void setLines(final ArrayList<char[]> lines) {
        this.m_lines = lines;
    }
    
    public final void setLineHeight(final int lineHeight) {
        this.m_lineHeight = lineHeight;
    }
    
    public final void setPosition(final Vector4 position) {
        this.m_position.set(position);
    }
    
    public final float getOffsetX() {
        return this.m_offsetX;
    }
    
    public final void setOffsetX(final float offsetX) {
        this.m_offsetX = offsetX;
    }
    
    public final float getOffsetY() {
        return this.m_offsetY;
    }
    
    public final void setOffsetY(final float offsetY) {
        this.m_offsetY = offsetY;
    }
    
    public final void setOffset(final float offsetX, final float offsetY) {
        this.m_offsetX = offsetX;
        this.m_offsetY = offsetY;
    }
}
