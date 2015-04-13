package com.ankamagames.framework.graphics.engine.light;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class LightSource
{
    private static int NEXT_LIGHT_ID;
    protected int m_id;
    protected final LightColor m_baseColor;
    protected final LightColor m_saturation;
    protected float m_range;
    protected final float[] m_attenuation;
    protected Vector3 m_position;
    protected boolean m_nightOnly;
    protected float m_intensity;
    protected boolean m_enabled;
    protected boolean m_shutdown;
    protected int m_elapsedTime;
    protected int m_shutdownDuration;
    private ShutdownListener m_shutdownListener;
    
    protected LightSource() {
        this(new Vector3(0.0f, 0.0f, 0.0f));
    }
    
    protected LightSource(final Vector3 position) {
        this(position, 3.0f);
    }
    
    protected LightSource(final Vector3 position, final float range) {
        super();
        this.m_id = 0;
        this.m_baseColor = new LightColor(LightColor.ONE);
        this.m_saturation = new LightColor(LightColor.ZERO);
        this.m_attenuation = new float[] { 0.0f, 1.0f, 0.0f };
        this.m_intensity = 1.0f;
        this.m_id = LightSource.NEXT_LIGHT_ID++;
        this.m_enabled = true;
        this.m_shutdownDuration = 0;
        this.m_position = position;
        this.m_range = range;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setAttenuation(final float c, final float l, final float q) {
        this.m_attenuation[0] = c;
        this.m_attenuation[1] = l;
        this.m_attenuation[2] = q;
    }
    
    public float getIntensity() {
        return this.m_intensity;
    }
    
    public void setIntensity(final float intensity) {
        this.m_intensity = intensity;
    }
    
    public boolean isNightOnly() {
        return this.m_nightOnly;
    }
    
    public void setNightOnly(final boolean nightOnly) {
        this.m_nightOnly = nightOnly;
    }
    
    public void update(final int deltaTime) {
        if (this.m_baseColor.needUpdate()) {
            this.m_baseColor.update(deltaTime);
        }
        if (this.m_saturation.needUpdate()) {
            this.m_saturation.update(deltaTime);
        }
        this.m_elapsedTime += deltaTime;
        if (this.m_shutdownDuration > 0 && this.m_elapsedTime > this.m_shutdownDuration) {
            final boolean b = false;
            this.m_shutdownDuration = (b ? 1 : 0);
            this.m_elapsedTime = (b ? 1 : 0);
            this.m_shutdown = true;
            if (this.m_shutdownListener != null) {
                this.m_shutdownListener.onShutdown();
            }
        }
    }
    
    public float getBaseColorRed() {
        return this.m_baseColor.getRed();
    }
    
    public float getBaseColorGreen() {
        return this.m_baseColor.getGreen();
    }
    
    public float getBaseColorBlue() {
        return this.m_baseColor.getBlue();
    }
    
    public float getSaturationRed() {
        return this.m_saturation.getRed();
    }
    
    public float getSaturationGreen() {
        return this.m_saturation.getGreen();
    }
    
    public float getSaturationBlue() {
        return this.m_saturation.getBlue();
    }
    
    public void setBaseColor(final float r, final float g, final float b) {
        this.m_baseColor.set(r, g, b);
    }
    
    public void setSaturation(final float r, final float g, final float b) {
        this.m_saturation.set(r, g, b);
    }
    
    public void modifySpecular(final float r, final float g, final float b) {
        this.m_saturation.set(r, g, b);
    }
    
    public void baseColorFadeTo(final float r, final float g, final float b, final int duration) {
        this.m_baseColor.fadeTo(r, g, b, duration);
    }
    
    public void saturationFadeTo(final float r, final float g, final float b, final int duration) {
        this.m_saturation.fadeTo(r, g, b, duration);
    }
    
    public Vector3 getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final Vector3 position) {
        this.m_position.setCurrent(position);
    }
    
    public float getRange() {
        return this.m_range;
    }
    
    public void setRange(final float range) {
        assert range >= 0.0f;
        this.m_range = range;
    }
    
    public LightColor getBaseColor() {
        return this.m_baseColor;
    }
    
    public LightColor getSaturation() {
        return this.m_saturation;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public void shutdown(final int duration) {
        if (this.m_enabled) {
            this.m_elapsedTime = 0;
            this.baseColorFadeTo(0.0f, 0.0f, 0.0f, this.m_shutdownDuration = duration);
            this.saturationFadeTo(0.0f, 0.0f, 0.0f, duration);
        }
        else {
            this.m_shutdown = true;
        }
    }
    
    public boolean isShutdown() {
        return this.m_shutdown;
    }
    
    public ShutdownListener getShutdownListener() {
        return this.m_shutdownListener;
    }
    
    public void setShutdownListener(final ShutdownListener shutdownListener) {
        this.m_shutdownListener = shutdownListener;
    }
    
    static {
        LightSource.NEXT_LIGHT_ID = 1;
    }
    
    public interface ShutdownListener
    {
        void onShutdown();
    }
}
