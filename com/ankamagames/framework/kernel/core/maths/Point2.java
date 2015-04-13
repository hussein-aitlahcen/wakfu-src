package com.ankamagames.framework.kernel.core.maths;

public class Point2
{
    public float m_x;
    public float m_y;
    
    public Point2() {
        super();
    }
    
    public Point2(final Point2 pt) {
        super();
        this.set(pt);
    }
    
    public Point2(final float x, final float y) {
        super();
        this.set(x, y);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Point2 point = (Point2)o;
        return this.m_x == point.m_x && this.m_y == point.m_y;
    }
    
    @Override
    public final int hashCode() {
        final long bits = 31L * (31L + (long)this.m_x) + (long)this.m_y;
        return (int)(bits ^ bits >> 32);
    }
    
    @Override
    public String toString() {
        return "{Point2i : (" + this.m_x + ", " + this.m_y + ") @" + Integer.toHexString(this.hashCode()) + "}";
    }
    
    public final boolean equals(final float x, final float y) {
        return this.m_x == x && this.m_y == y;
    }
    
    public final boolean equals(final Point2 point) {
        return this.m_x == point.m_x && this.m_y == point.m_y;
    }
    
    public final void set(final float x, final float y) {
        this.m_x = x;
        this.m_y = y;
    }
    
    public final void set(final Point2 point) {
        this.m_x = point.m_x;
        this.m_y = point.m_y;
    }
    
    public final float getX() {
        return this.m_x;
    }
    
    public final float getY() {
        return this.m_y;
    }
    
    public final void setX(final float x) {
        this.m_x = x;
    }
    
    public final void setY(final float y) {
        this.m_y = y;
    }
}
