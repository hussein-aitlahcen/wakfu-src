package com.ankamagames.framework.kernel.core.maths;

public class Point2i
{
    private int m_x;
    private int m_y;
    
    public Point2i() {
        super();
    }
    
    public Point2i(final Point2i pt) {
        super();
        this.set(pt);
    }
    
    public Point2i(final int x, final int y) {
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
        final Point2i point = (Point2i)o;
        return this.m_x == point.m_x && this.m_y == point.m_y;
    }
    
    @Override
    public final int hashCode() {
        final long bits = 31L * (31L + this.m_x) + this.m_y;
        return (int)(bits ^ bits >> 32);
    }
    
    @Override
    public String toString() {
        return "{Point2i : (" + this.m_x + ", " + this.m_y + ") @" + Integer.toHexString(this.hashCode()) + "}";
    }
    
    public final boolean equals(final int x, final int y) {
        return this.m_x == x && this.m_y == y;
    }
    
    public final boolean equals(final Point2i point) {
        return this.m_x == point.m_x && this.m_y == point.m_y;
    }
    
    public final void set(final int x, final int y) {
        this.m_x = x;
        this.m_y = y;
    }
    
    public final void set(final Point2i point) {
        this.m_x = point.m_x;
        this.m_y = point.m_y;
    }
    
    public final int getX() {
        return this.m_x;
    }
    
    public final int getY() {
        return this.m_y;
    }
    
    public final void setX(final int x) {
        this.m_x = x;
    }
    
    public final void setY(final int y) {
        this.m_y = y;
    }
}
