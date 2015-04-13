package com.ankamagames.framework.kernel.core.maths;

public final class Vector4 extends Base4
{
    public Vector4() {
        super();
    }
    
    public Vector4(final Vector4 v) {
        super(v);
    }
    
    public Vector4(final float x, final float y, final float z) {
        super();
        this.set(x, y, z);
    }
    
    public Vector4(final float x, final float y, final float z, final float w) {
        super(x, y, z, w);
    }
    
    public void set(final float x, final float y, final float z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public float getNormeSquare() {
        return this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ();
    }
    
    public float getNormeSquare4() {
        return this.getNormeSquare() + this.getW() * this.getW();
    }
    
    public float getNorme() {
        return (float)Math.sqrt(this.getNormeSquare());
    }
    
    public float getNorme4() {
        return (float)Math.sqrt(this.getNormeSquare4());
    }
    
    public float normalize() {
        final float norme = this.getNorme();
        if (norme != 0.0f) {
            final float nf = 1.0f / norme;
            this.m_x *= nf;
            this.m_y *= nf;
            this.m_z *= nf;
            this.m_w *= nf;
        }
        return norme;
    }
    
    public float dotProduct(final Vector4 v) {
        return this.getX() * v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
    }
    
    public void crossProduct(final Vector4 v1, final Vector4 v2) {
        this.set(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(), v1.getZ() * v2.getX() - v1.getX() * v2.getZ(), v1.getX() * v2.getY() - v1.getY() * v2.getX(), 0.0f);
    }
    
    public Vector4 multiply(final float f) {
        return new Vector4(this.getX() * f, this.getY() * f, this.getZ() * f, this.getW() * f);
    }
    
    public Vector4 divide(final float f) {
        assert f != 0.0f;
        return new Vector4(this.getX() / f, this.getY() / f, this.getZ() / f, this.getW() / f);
    }
    
    public Vector4 add(final Vector4 v) {
        return new Vector4(this.getX() + v.getX(), this.getY() + v.getY(), this.getZ() + v.getZ(), this.getW() + v.getW());
    }
    
    public Vector4 minus(final Vector4 v) {
        return new Vector4(this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ(), this.getW() - v.getW());
    }
    
    public Vector4 unaryMinus() {
        return new Vector4(-this.getX(), -this.getY(), -this.getZ(), -this.getW());
    }
    
    public void setMultiply(final float f) {
        this.set(this.getX() * f, this.getY() * f, this.getZ() * f, this.getW() * f);
    }
    
    public void setDivide(final float f) {
        assert f != 0.0f;
        this.set(this.getX() / f, this.getY() / f, this.getZ() / f, this.getW() / f);
    }
    
    public void setAdd(final Vector4 v) {
        this.set(v.getX(), v.getY(), v.getZ(), v.getW());
    }
    
    public void setAdd(final float x, final float y, final float z, final float w) {
        this.set(this.getX() + x, this.getY() + y, this.getZ() + z, this.getW() + w);
    }
    
    public void setMinus(final Vector4 v) {
        this.set(this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ(), this.getW() - v.getW());
    }
    
    public void setAddMult(final float f, final Vector4 v) {
        this.set(this.getX() + f * v.getX(), this.getY() + f * v.getY(), this.getZ() + f * v.getZ(), this.getW() + f * v.getW());
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("V4{x=");
        buffer.append(this.m_x).append(", y=").append(this.m_y).append(", z=").append(this.m_z).append(", w=").append(this.m_w).append("}");
        return buffer.toString();
    }
}
