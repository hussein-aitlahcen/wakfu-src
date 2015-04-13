package com.ankamagames.framework.graphics.engine.light;

import com.ankamagames.framework.kernel.core.maths.*;

public class LightColor
{
    public static final LightColor ONE;
    public static final LightColor ZERO;
    protected float m_red;
    protected float m_green;
    protected float m_blue;
    protected float m_targetRed;
    protected float m_targetGreen;
    protected float m_targetBlue;
    protected int m_elapsedTime;
    protected int m_targetChangeDuration;
    
    public LightColor(final LightColor color) {
        this(color.m_red, color.m_green, color.m_blue);
    }
    
    public LightColor(final float red, final float green, final float blue) {
        super();
        this.m_red = red;
        this.m_targetRed = red;
        this.m_green = green;
        this.m_targetGreen = green;
        this.m_blue = blue;
        this.m_targetBlue = blue;
        this.m_targetChangeDuration = 0;
        this.m_elapsedTime = 0;
    }
    
    public float getRed() {
        return this.m_red;
    }
    
    public float getGreen() {
        return this.m_green;
    }
    
    public float getBlue() {
        return this.m_blue;
    }
    
    public void fadeTo(final float red, final float green, final float blue, final int duration) {
        this.m_targetRed = red;
        this.m_targetGreen = green;
        this.m_targetBlue = blue;
        this.m_targetChangeDuration = duration;
        this.m_elapsedTime = 0;
    }
    
    public void set(final float r, final float g, final float b) {
        this.m_targetRed = r;
        this.m_red = r;
        this.m_targetGreen = g;
        this.m_green = g;
        this.m_targetBlue = b;
        this.m_blue = b;
        this.m_targetChangeDuration = 0;
        this.m_elapsedTime = 0;
    }
    
    public void update(final int deltaTime) {
        if (this.m_targetChangeDuration == 0) {
            return;
        }
        this.m_elapsedTime += deltaTime;
        if (this.m_elapsedTime < this.m_targetChangeDuration) {
            final float f = this.m_elapsedTime / this.m_targetChangeDuration;
            this.m_red = MathHelper.lerp(this.m_red, this.m_targetRed, f);
            this.m_green = MathHelper.lerp(this.m_green, this.m_targetGreen, f);
            this.m_blue = MathHelper.lerp(this.m_blue, this.m_targetBlue, f);
        }
        else {
            this.m_red = this.m_targetRed;
            this.m_green = this.m_targetGreen;
            this.m_blue = this.m_targetBlue;
            this.m_targetChangeDuration = 0;
        }
    }
    
    public boolean needUpdate() {
        return this.m_targetChangeDuration != 0;
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("{");
        buffer.append(this.m_red).append(" ; ");
        buffer.append(this.m_green).append(" ; ");
        buffer.append(this.m_blue).append("}");
        return buffer.toString();
    }
    
    static {
        ONE = new LightColor(1.0f, 1.0f, 1.0f);
        ZERO = new LightColor(0.0f, 0.0f, 0.0f);
    }
}
