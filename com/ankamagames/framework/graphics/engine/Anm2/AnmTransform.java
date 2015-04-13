package com.ankamagames.framework.graphics.engine.Anm2;

class AnmTransform
{
    float m_rotationSkewX0;
    float m_rotationSkewY0;
    float m_rotationSkewX1;
    float m_rotationSkewY1;
    float m_translationX;
    float m_translationY;
    boolean m_rotationIsIdentity;
    boolean m_translationIsIdentity;
    float m_red;
    float m_green;
    float m_blue;
    float m_alpha;
    byte m_customColorIndex;
    
    AnmTransform() {
        super();
        this.m_rotationIsIdentity = true;
        this.m_translationIsIdentity = true;
        this.m_rotationSkewX0 = 1.0f;
        this.m_rotationSkewY0 = 0.0f;
        this.m_rotationSkewX1 = 0.0f;
        this.m_rotationSkewY1 = 1.0f;
        this.m_translationX = 0.0f;
        this.m_translationY = 0.0f;
        this.m_red = 1.0f;
        this.m_green = 1.0f;
        this.m_blue = 1.0f;
        this.m_alpha = 1.0f;
        this.m_customColorIndex = 0;
    }
    
    public final void setRotationToId() {
        this.m_rotationIsIdentity = true;
        this.m_rotationSkewX0 = 1.0f;
        this.m_rotationSkewY0 = 0.0f;
        this.m_rotationSkewX1 = 0.0f;
        this.m_rotationSkewY1 = 1.0f;
    }
    
    public final void setRotationSkew(final float x0, final float y0, final float x1, final float y1) {
        this.m_rotationIsIdentity = (x0 == 1.0f && y0 == 0.0f && x1 == 0.0f && y1 == 1.0f);
        this.m_rotationSkewX0 = x0;
        this.m_rotationSkewY0 = y0;
        this.m_rotationSkewX1 = x1;
        this.m_rotationSkewY1 = y1;
    }
    
    public final void setTranslation(final float x, final float y) {
        this.m_translationIsIdentity = (x == 0.0f && y == 0.0f);
        this.m_translationX = x;
        this.m_translationY = y;
    }
}
