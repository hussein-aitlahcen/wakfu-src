package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.fx.*;

class Storm extends Rain
{
    private static final RainParams RAIN_PARAMS;
    private ThunderEffect m_thunderEffect;
    
    Storm() {
        super(Storm.RAIN_PARAMS);
    }
    
    @Override
    void start(final IsoWorldScene scene) {
        super.start(scene);
        (this.m_thunderEffect = new ThunderEffect()).activate(true);
        this.m_thunderEffect.setCamera(scene.getIsoCamera());
        EffectManager.getInstance().addWorldEffect(this.m_thunderEffect);
    }
    
    @Override
    void stop() {
        super.stop();
        if (this.m_thunderEffect != null) {
            this.m_thunderEffect.activate(false);
            this.m_thunderEffect = null;
        }
    }
    
    @Override
    void fadeTo(final float factor, final int duration, final long currentTime) {
        super.fadeTo(factor, duration, currentTime);
        if (factor == 0.0f && this.m_thunderEffect != null) {
            this.m_thunderEffect.activate(false);
            this.m_thunderEffect = null;
        }
    }
    
    @Override
    void update(final IsoWorldScene scene, final float windDirection, final float windStrength) {
        super.update(scene, windDirection, windStrength);
    }
    
    static {
        RAIN_PARAMS = new RainParams();
        Storm.RAIN_PARAMS.particleVelocity = 10.0f;
        Storm.RAIN_PARAMS.particleAlpha = 0.05f;
        Storm.RAIN_PARAMS.particleHeight = 80.0f;
        Storm.RAIN_PARAMS.particleAngleRandom = 1.0f;
    }
}
