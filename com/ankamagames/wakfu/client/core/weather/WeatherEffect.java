package com.ankamagames.wakfu.client.core.weather;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

abstract class WeatherEffect
{
    protected static final Logger m_logger;
    protected boolean m_isRunning;
    protected boolean m_visible;
    protected final FloatParameter m_strength;
    private WeatherEffectListener m_listener;
    
    WeatherEffect() {
        super();
        this.m_isRunning = false;
        this.m_visible = true;
        this.m_strength = new FloatParameter(0.0f);
    }
    
    public void setListener(final WeatherEffectListener listener) {
        this.m_listener = listener;
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
    }
    
    boolean isRunning() {
        return this.m_isRunning;
    }
    
    void setStrength(final float strength) {
        this.m_strength.set(strength);
    }
    
    float getStrength() {
        return this.m_strength.getCurrent();
    }
    
    void fadeTo(final float factor, final int duration, final long currentTime) {
        this.m_strength.changeTo(factor, duration, currentTime);
    }
    
    void updateStrength(final long currentTime) {
        this.m_strength.update(currentTime);
    }
    
    void start(final IsoWorldScene scene) {
        if (this.m_listener != null && !this.m_isRunning) {
            this.m_listener.onStart();
        }
    }
    
    void stop() {
        if (this.m_listener != null && this.m_isRunning) {
            this.m_listener.onEnd();
        }
    }
    
    abstract void update(final IsoWorldScene p0, final float p1, final float p2);
    
    static {
        m_logger = Logger.getLogger((Class)WeatherEffect.class);
    }
}
