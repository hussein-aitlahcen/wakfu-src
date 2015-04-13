package com.ankamagames.framework.graphics.engine.light;

public class LightSourceManagerDelegate
{
    public static final LightSourceManagerDelegate INSTANCE;
    private LightSourceFactory m_lightSourceFactory;
    private LightSourceManager m_lightSourceManager;
    
    public void setUp(final LightSourceFactory lightSourceFactory, final LightSourceManager lightSourceManager) {
        this.m_lightSourceFactory = lightSourceFactory;
        this.m_lightSourceManager = lightSourceManager;
    }
    
    public LightSource createLightSource() {
        if (this.m_lightSourceFactory != null) {
            return this.m_lightSourceFactory.createLightSource();
        }
        return null;
    }
    
    public void addLight(final LightSource lightSource) {
        if (this.m_lightSourceManager != null) {
            this.m_lightSourceManager.addLight(lightSource);
        }
    }
    
    public void removeLight(final LightSource lightSource) {
        if (this.m_lightSourceManager != null) {
            this.m_lightSourceManager.removeLight(lightSource);
        }
    }
    
    static {
        INSTANCE = new LightSourceManagerDelegate();
    }
}
