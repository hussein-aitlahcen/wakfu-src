package com.ankamagames.baseImpl.graphics.alea.display.effects;

public abstract class Particle
{
    public float m_x;
    public float m_y;
    public float m_z;
    public float m_velocityX;
    public float m_velocityY;
    public float m_velocityZ;
    public float m_accelerationX;
    public float m_accelerationY;
    public float m_accelerationZ;
    public float m_red;
    public float m_green;
    public float m_blue;
    public float m_alpha;
    public float m_life;
    public float m_lifeTime;
    public float m_top;
    public float m_left;
    public float m_right;
    public float m_bottom;
    public float m_width;
    public float m_height;
    public float m_angle;
    
    public abstract void update(final float p0);
}
