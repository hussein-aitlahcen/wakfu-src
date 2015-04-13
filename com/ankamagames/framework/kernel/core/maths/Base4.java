package com.ankamagames.framework.kernel.core.maths;

public class Base4
{
    public float m_x;
    public float m_y;
    public float m_z;
    public float m_w;
    
    public Base4() {
        super();
    }
    
    public Base4(final Base4 b) {
        super();
        this.copy(b);
    }
    
    public Base4(final float x, final float y, final float z, final float w) {
        super();
        this.set(x, y, z, w);
    }
    
    public final void set(final float x, final float y, final float z, final float w) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_w = w;
    }
    
    public final void set(final Base4 b) {
        this.copy(b);
    }
    
    public final void setX(final float x) {
        this.m_x = x;
    }
    
    public final void setY(final float y) {
        this.m_y = y;
    }
    
    public final void setZ(final float z) {
        this.m_z = z;
    }
    
    public final void setW(final float w) {
        this.m_w = w;
    }
    
    public final void copy(final Base4 b) {
        this.m_x = b.m_x;
        this.m_y = b.m_y;
        this.m_z = b.m_z;
        this.m_w = b.m_w;
    }
    
    public final float getX() {
        return this.m_x;
    }
    
    public final float getY() {
        return this.m_y;
    }
    
    public final float getZ() {
        return this.m_z;
    }
    
    public final float getW() {
        return this.m_w;
    }
    
    public final boolean isEqual(final Base4 b) {
        return MathHelper.isEqual(this.m_x, b.m_x) && MathHelper.isEqual(this.m_y, b.m_y) && MathHelper.isEqual(this.m_z, b.m_z) && MathHelper.isEqual(this.m_w, b.m_w);
    }
}
