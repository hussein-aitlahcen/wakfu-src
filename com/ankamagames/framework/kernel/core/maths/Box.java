package com.ankamagames.framework.kernel.core.maths;

public class Box
{
    float m_xMin;
    float m_xMax;
    float m_yMin;
    float m_yMax;
    float m_zMin;
    float m_zMax;
    
    public Box(final float xMin, final float xMax, final float yMin, final float yMax, final float zMin, final float zMax) {
        super();
        this.m_xMin = Float.MIN_VALUE;
        this.m_xMax = Float.MAX_VALUE;
        this.m_yMin = Float.MIN_VALUE;
        this.m_yMax = Float.MAX_VALUE;
        this.m_zMin = Float.MIN_VALUE;
        this.m_zMax = Float.MAX_VALUE;
        this.set(xMin, xMax, yMin, yMax, zMin, zMax);
    }
    
    public Box() {
        super();
        this.m_xMin = Float.MIN_VALUE;
        this.m_xMax = Float.MAX_VALUE;
        this.m_yMin = Float.MIN_VALUE;
        this.m_yMax = Float.MAX_VALUE;
        this.m_zMin = Float.MIN_VALUE;
        this.m_zMax = Float.MAX_VALUE;
    }
    
    public static Box InfiniteBox() {
        return new Box();
    }
    
    public final void set(final float xMin, final float xMax, final float yMin, final float yMax, final float zMin, final float zMax) {
        this.setxMin(xMin);
        this.setxMax(xMax);
        this.setyMin(yMin);
        this.setyMax(yMax);
        this.setzMin(zMin);
        this.setzMax(zMax);
    }
    
    public final float getxMax() {
        return this.m_xMax;
    }
    
    public final void setxMax(final float xMax) {
        if (this.m_xMin > xMax) {
            this.m_xMax = this.m_xMin;
            this.m_xMin = xMax;
        }
        else {
            this.m_xMax = xMax;
        }
    }
    
    public final float getxMin() {
        return this.m_xMin;
    }
    
    public final void setxMin(final float xMin) {
        if (this.m_xMax < xMin) {
            this.m_xMin = this.m_xMax;
            this.m_xMax = xMin;
        }
        else {
            this.m_xMin = xMin;
        }
    }
    
    public final float getyMax() {
        return this.m_yMax;
    }
    
    public final void setyMax(final float yMax) {
        if (this.m_yMin > yMax) {
            this.m_yMax = this.m_yMin;
            this.m_yMin = yMax;
        }
        else {
            this.m_yMax = yMax;
        }
    }
    
    public final float getyMin() {
        return this.m_yMin;
    }
    
    public final void setyMin(final float yMin) {
        if (this.m_yMax < yMin) {
            this.m_yMin = this.m_yMax;
            this.m_yMax = yMin;
        }
        else {
            this.m_yMin = yMin;
        }
    }
    
    public final float getzMax() {
        return this.m_zMax;
    }
    
    public final void setzMax(final float zMax) {
        if (this.m_zMin > zMax) {
            this.m_zMax = this.m_zMin;
            this.m_zMin = zMax;
        }
        else {
            this.m_zMax = zMax;
        }
    }
    
    public final float getzMin() {
        return this.m_zMin;
    }
    
    public final void setzMin(final float zMin) {
        if (this.m_zMax < zMin) {
            this.m_zMin = this.m_zMax;
            this.m_zMax = zMin;
        }
        else {
            this.m_zMin = zMin;
        }
    }
    
    public final boolean containsPoint(final float x, final float y, final float z) {
        return this.m_xMin <= x && x <= this.m_xMax && this.m_yMin <= y && y <= this.m_yMax && this.m_zMin <= z && z <= this.m_zMax;
    }
    
    public final boolean intersect(final Box box) {
        return box != null && this.m_xMin <= box.getxMax() && box.getxMin() <= this.m_xMax && this.m_yMin <= box.getyMax() && box.getyMin() <= this.m_yMax && this.m_zMin <= box.getzMax() && box.getzMin() <= this.m_zMax;
    }
    
    public final boolean containsBox(final Box box) {
        return this.m_xMin <= box.getxMin() && box.getxMax() <= this.m_xMax && this.m_yMin <= box.getyMin() && box.getyMax() <= this.m_yMax && this.m_zMin <= box.getzMin() && box.getzMax() <= this.m_zMax;
    }
    
    public final float width() {
        return this.m_xMax - this.m_xMin;
    }
    
    public final float height() {
        return this.m_yMax - this.m_xMin;
    }
}
