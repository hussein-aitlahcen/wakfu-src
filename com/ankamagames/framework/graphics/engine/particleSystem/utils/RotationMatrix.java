package com.ankamagames.framework.graphics.engine.particleSystem.utils;

public class RotationMatrix
{
    public float m_alpha;
    public float m_beta;
    public float m_gamma;
    public float m_x;
    public float m_y;
    public float m_z;
    float[] rm;
    
    public void changeAngle(final float rotA, final float rotB, final float rotC) {
        this.m_alpha = rotA;
        this.m_beta = rotB;
        this.m_gamma = rotC;
        final float sinA = (float)Math.sin(this.m_alpha);
        final float sinB = (float)Math.sin(this.m_beta);
        final float sinG = (float)Math.sin(this.m_gamma);
        final float cosA = (float)Math.cos(this.m_alpha);
        final float cosB = (float)Math.cos(this.m_beta);
        final float cosG = (float)Math.cos(this.m_gamma);
        this.rm = new float[] { cosA * cosB, cosA * sinB * sinG - sinA * cosG, cosA * sinB * cosG + sinA * sinG, sinA * cosB, sinA * sinB * sinG + cosA * cosG, sinA * sinB * cosG - cosA * sinG, -sinB, cosB * sinG, cosB * cosG };
    }
    
    public void transform(final float x, final float y, final float z, final float cX, final float cY, final float cZ) {
        final float px = x - cX;
        final float py = y - cY;
        final float pz = z - cZ;
        this.m_x = this.rm[0] * px + this.rm[1] * py + this.rm[2] * pz;
        this.m_y = this.rm[3] * px + this.rm[4] * py + this.rm[5] * pz;
        this.m_z = this.rm[6] * px + this.rm[7] * py + this.rm[8] * pz;
        this.m_x += cX;
        this.m_y += cY;
        this.m_z += cZ;
    }
}
