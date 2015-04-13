package com.ankamagames.framework.kernel.core.maths;

import java.nio.*;

public class Rect
{
    public static final int SERIALIZED_LENGTH = 16;
    public int m_xMin;
    public int m_xMax;
    public int m_yMin;
    public int m_yMax;
    
    public Rect(final Rect rect) {
        super();
        this.set(rect);
    }
    
    public Rect(final int xMin, final int xMax, final int yMin, final int yMax) {
        super();
        this.set(xMin, xMax, yMin, yMax);
    }
    
    public Rect() {
        super();
        this.m_xMin = Integer.MIN_VALUE;
        this.m_xMax = Integer.MAX_VALUE;
        this.m_yMin = Integer.MIN_VALUE;
        this.m_yMax = Integer.MAX_VALUE;
    }
    
    public final ByteBuffer serialize(final ByteBuffer buf) {
        buf.putInt(this.m_xMin);
        buf.putInt(this.m_yMin);
        buf.putInt(this.m_xMax);
        buf.putInt(this.m_yMax);
        return buf;
    }
    
    public final ByteBuffer unserialize(final ByteBuffer buf) {
        this.m_xMin = buf.getInt();
        this.m_yMin = buf.getInt();
        this.m_xMax = buf.getInt();
        this.m_yMax = buf.getInt();
        return buf;
    }
    
    public final void set(final int xMin, final int xMax, final int yMin, final int yMax) {
        this.m_xMin = xMin;
        this.m_xMax = xMax;
        this.m_yMin = yMin;
        this.m_yMax = yMax;
    }
    
    public final void set(final Rect rect) {
        this.m_xMin = rect.m_xMin;
        this.m_xMax = rect.m_xMax;
        this.m_yMin = rect.m_yMin;
        this.m_yMax = rect.m_yMax;
    }
    
    public final void translate(final int x, final int y) {
        this.m_xMin += x;
        this.m_xMax += x;
        this.m_yMin += y;
        this.m_yMax += y;
    }
    
    public final int getXMin() {
        return this.m_xMin;
    }
    
    public final void setXMin(final int xMin) {
        this.m_xMin = xMin;
    }
    
    public final int getXMax() {
        return this.m_xMax;
    }
    
    public final void setXMax(final int xMax) {
        this.m_xMax = xMax;
    }
    
    public final int getYMin() {
        return this.m_yMin;
    }
    
    public final void setYMin(final int yMin) {
        this.m_yMin = yMin;
    }
    
    public final int getYMax() {
        return this.m_yMax;
    }
    
    public final void setYMax(final int yMax) {
        this.m_yMax = yMax;
    }
    
    public final int width() {
        return this.m_xMax - this.m_xMin + 1;
    }
    
    public final int height() {
        return this.m_yMax - this.m_yMin + 1;
    }
    
    public final boolean contains(final int x, final int y) {
        return x >= this.m_xMin && x <= this.m_xMax && y >= this.m_yMin && y <= this.m_yMax;
    }
    
    public final boolean containsOrIntersect(final Rect r) {
        return this.containsOrIntersect(r.m_xMin, r.m_xMax, r.m_yMin, r.m_yMax);
    }
    
    public final boolean containsOrIntersect(final int xMin, final int xMax, final int yMin, final int yMax) {
        return xMin <= this.m_xMax && xMax >= this.m_xMin && yMin <= this.m_yMax && yMax >= this.m_yMin;
    }
    
    public final void includeVertex(final int x, final int y) {
        this.m_xMin = Math.min(x, this.m_xMin);
        this.m_xMax = Math.max(x, this.m_xMax);
        this.m_yMin = Math.min(y, this.m_yMin);
        this.m_yMax = Math.max(y, this.m_yMax);
    }
    
    public final void includeRect(final Rect rect) {
        this.m_xMin = Math.min(rect.m_xMin, this.m_xMin);
        this.m_xMax = Math.max(rect.m_xMax, this.m_xMax);
        this.m_yMin = Math.min(rect.m_yMin, this.m_yMin);
        this.m_yMax = Math.max(rect.m_yMax, this.m_yMax);
    }
    
    @Override
    public String toString() {
        return "(" + this.m_xMin + ", " + this.m_yMin + ") - (" + this.m_xMax + ", " + this.m_yMax + ")";
    }
    
    public double getCenterX() {
        return (this.m_xMin + this.m_xMax) * 0.5;
    }
    
    public double getCenterY() {
        return (this.m_yMin + this.m_yMax) * 0.5;
    }
}
