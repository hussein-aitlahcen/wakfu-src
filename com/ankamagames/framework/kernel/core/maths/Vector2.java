package com.ankamagames.framework.kernel.core.maths;

public class Vector2
{
    public float m_x;
    public float m_y;
    
    public Vector2() {
        this(0.0f, 0.0f);
    }
    
    public Vector2(final Vector2 v) {
        this(v.m_x, v.m_y);
    }
    
    public Vector2(final float[] v) {
        this(v[0], v[1]);
    }
    
    public Vector2(final Point2 startPoint, final Point2 endPoint) {
        super();
        this.m_x = endPoint.getX() - startPoint.getX();
        this.m_y = endPoint.getY() - startPoint.getY();
    }
    
    public Vector2(final float x, final float y) {
        super();
        this.m_x = x;
        this.m_y = y;
    }
    
    public Vector2(final int startX, final int startY, final int endX, final int endY) {
        super();
        this.m_x = endX - startX;
        this.m_y = endY - startY;
    }
    
    public void set(final float[] coords) {
        this.m_x = coords[0];
        this.m_y = coords[1];
    }
    
    public void set(final float x, final float y) {
        this.m_x = x;
        this.m_y = y;
    }
    
    public void set(final Vector2 v) {
        this.m_x = v.m_x;
        this.m_y = v.m_y;
    }
    
    public float getX() {
        return this.m_x;
    }
    
    public void setX(final float x) {
        this.m_x = x;
    }
    
    public float getY() {
        return this.m_y;
    }
    
    public void setY(final float y) {
        this.m_y = y;
    }
    
    public Vector2 add(final Vector2 v) {
        return new Vector2(v.m_x + this.m_x, v.m_y + this.m_y);
    }
    
    public void setAdd(final Vector2 v) {
        this.m_x += v.m_x;
        this.m_y += v.m_y;
    }
    
    public final float getDistanceSquared(final Vector2 v) {
        final float dx = this.m_x - v.m_x;
        final float dy = this.m_y - v.m_y;
        return dx * dx + dy * dy;
    }
    
    public Vector2 sub(final Vector2 v) {
        return new Vector2(this.m_x - v.m_x, this.m_y - v.m_y);
    }
    
    public void setSub(final Vector2 v) {
        this.m_x -= v.m_x;
        this.m_y -= v.m_y;
    }
    
    public Vector2 mul(final Vector2 v) {
        return new Vector2(this.m_x * v.m_x + this.m_x * v.m_y, this.m_y * v.m_x + this.m_y * v.m_y);
    }
    
    public Vector2 mul(final float s) {
        return new Vector2(s * this.m_x, s * this.m_y);
    }
    
    public void setMul(final float s) {
        this.m_x *= s;
        this.m_y *= s;
    }
    
    public Vector2 mulD(final float d) {
        return new Vector2((int)(d * this.m_x), (int)(d * this.m_y));
    }
    
    public float det(final Vector2 v) {
        return this.m_x * v.m_y - v.m_x * this.m_y;
    }
    
    public float dot(final Vector2 v) {
        return this.m_x * v.m_x + this.m_y * v.m_y;
    }
    
    public float sqrLength() {
        return sqrLength(this.m_x, this.m_y);
    }
    
    public float length() {
        return length(this.m_x, this.m_y);
    }
    
    public Vector2 normalize() {
        return this.mul(1.0f / this.length());
    }
    
    public void normalizeCurrent() {
        float l = this.length();
        if (l == 0.0f) {
            l = 0.001f;
        }
        final float nf = 1.0f / l;
        this.m_x *= nf;
        this.m_y *= nf;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Vector2)) {
            return false;
        }
        final Vector2 v = (Vector2)obj;
        return v.m_x == this.m_x && v.m_y == this.m_y;
    }
    
    @Override
    public String toString() {
        return "V2 : [" + this.m_x + " ; " + this.m_y + "]";
    }
    
    @Override
    public int hashCode() {
        final long bits = 31L * (31L + (long)this.m_x) + (long)this.m_y;
        return (int)(bits ^ bits >> 32);
    }
    
    public static float length(final float dx, final float dy) {
        return MathHelper.sqrt(dx * dx + dy * dy);
    }
    
    public static float sqrLength(final float dx, final float dy) {
        return dx * dx + dy * dy;
    }
}
