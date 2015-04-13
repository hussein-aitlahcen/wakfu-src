package com.ankamagames.framework.kernel.core.maths;

public final class Quaternion extends Base4
{
    public Quaternion() {
        super();
    }
    
    public Quaternion(final Quaternion q) {
        super(q);
    }
    
    public Quaternion(final float x, final float y, final float z, final float w) {
        super(x, y, z, w);
    }
    
    public Quaternion(final Vector3 v, final float angle) {
        super();
        this.set(v, angle);
    }
    
    public float getNorme() {
        return (float)Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ() + this.getW() * this.getW());
    }
    
    public void normalize() {
        assert this.getNorme() > 0.0f : "Unable to normalize the quaternion since the norme is null";
        final float norme = this.getNorme();
        this.m_x /= norme;
        this.m_y /= norme;
        this.m_z /= norme;
        this.m_w /= norme;
    }
    
    public void setIdentity() {
        this.set(0.0f, 0.0f, 0.0f, 1.0f);
    }
    
    public void set(final Vector3 v, final float angle) {
        final float halfAngle = angle * 0.5f;
        final float angleSin = MathHelper.sin(halfAngle);
        this.set(v.getX() * angleSin, v.getY() * angleSin, v.getZ() * angleSin, MathHelper.cos(halfAngle));
        this.normalize();
    }
    
    public void set(final float ax, final float ay, final float az) {
        final Quaternion qx = new Quaternion(new Vector3(MathHelper.sin(ax * 0.5f), 0.0f, 0.0f), MathHelper.cos(ax * 0.5f));
        final Quaternion qy = new Quaternion(new Vector3(0.0f, MathHelper.sin(ay * 0.5f), 0.0f), MathHelper.cos(ay * 0.5f));
        final Quaternion qz = new Quaternion(new Vector3(0.0f, 0.0f, MathHelper.sin(az * 0.5f)), MathHelper.cos(az * 0.5f));
        this.set(qx.multiply(qy).multiply(qz));
    }
    
    public Quaternion getConjugate() {
        return new Quaternion(-this.getX(), -this.getY(), -this.getZ(), this.getW());
    }
    
    public Quaternion getInverse() {
        final Quaternion q = this.getConjugate();
        final float sqNorme = q.getX() * q.getX() + q.getY() * q.getY() + q.getZ() * q.getZ() + q.getW() * q.getW();
        final Quaternion quaternion = q;
        quaternion.m_x /= sqNorme;
        final Quaternion quaternion2 = q;
        quaternion2.m_y /= sqNorme;
        final Quaternion quaternion3 = q;
        quaternion3.m_z /= sqNorme;
        final Quaternion quaternion4 = q;
        quaternion4.m_w /= sqNorme;
        return q;
    }
    
    public Quaternion multiply(final Quaternion q) {
        return new Quaternion(this.getW() * q.getX() + this.getX() * q.getW() + this.getY() * q.getZ() - this.getZ() * q.getY(), this.getW() * q.getY() + this.getY() * q.getW() + this.getZ() * q.getX() - this.getX() * q.getZ(), this.getW() * q.getZ() + this.getZ() * q.getW() + this.getX() * q.getY() - this.getY() * q.getX(), this.getW() * q.getW() - this.getX() * q.getX() - this.getY() * q.getY() - this.getZ() * q.getZ());
    }
    
    public Quaternion divide(final Quaternion q) {
        return this.multiply(q.getInverse());
    }
    
    public void setMupltiply(final Quaternion q) {
        this.set(this.multiply(q));
    }
    
    public void setDivide(final Quaternion q) {
        this.set(this.divide(q));
    }
    
    public void slerp(final Quaternion q0, final Quaternion q1, final float f) {
        final float cosom = q0.getX() * q1.getX() + q0.getY() * q1.getY() + q0.getZ() * q1.getZ() + q0.getW() * q1.getW();
        if (1.0f + cosom > 1.0E-5f) {
            float scale0;
            float scale;
            if (1.0f - cosom > 1.0E-5f) {
                final float omega = MathHelper.acos(cosom);
                final float sinom = MathHelper.sin(omega);
                scale0 = MathHelper.sin((1.0f - f) * omega) / sinom;
                scale = MathHelper.sin(f * omega) / sinom;
            }
            else {
                scale0 = 1.0f - f;
                scale = f;
            }
            this.set(scale0 * q0.getX() + scale * q1.getX(), scale0 * q0.getY() + scale * q1.getY(), scale0 * q0.getZ() + scale * q1.getZ(), scale0 * q0.getW() + scale * q1.getW());
        }
        else {
            final float scale0 = MathHelper.sin((1.0f - f) * 1.5707964f);
            final float scale = MathHelper.sin(f * 1.5707964f);
            this.set(scale0 * q0.getX() - scale * q1.getY(), scale0 * q0.getY() + scale * q1.getX(), scale0 * q0.getZ() - scale * q1.getW(), scale0 * q0.getW() + scale * q1.getZ());
        }
    }
}
