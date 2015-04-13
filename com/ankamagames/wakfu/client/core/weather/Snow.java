package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.particles.snow.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.wakfu.client.alea.ambiance.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.shaders.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.framework.kernel.core.maths.*;

class Snow extends WeatherEffect
{
    private static final int MAX_PARTICLES_COUNT = 2800;
    private int m_snowEffectId;
    private int m_freezeEffectId;
    
    @Override
    void start(final IsoWorldScene scene) {
        super.start(scene);
        if (this.m_snowEffectId > 0) {
            return;
        }
        final int numParticles = 2800;
        final IsoCamera camera = scene.getIsoCamera();
        final Box boundingBox = new Box(-15.0f, 15.0f, -15.0f, 15.0f, 0.0f, 36.0f);
        final SnowParticleEffect snowEffect = new SnowParticleEffect();
        snowEffect.initialize(2800);
        snowEffect.setBoundingBox(boundingBox);
        snowEffect.setCamera(camera);
        final float particleSize = 2.5f;
        final float particleLifeTime = 60000.0f;
        final float spawnFrequency = 100.0f;
        final float numParticlesPerSpawn = 128.0f;
        snowEffect.start(128.0f, 100.0f, 60000.0f, 2.5f);
        EffectManager.getInstance().addWorldEffect(snowEffect);
        this.m_snowEffectId = snowEffect.getId();
        this.m_isRunning = true;
    }
    
    private void startFreeze() {
        if (this.m_freezeEffectId != 0) {
            return;
        }
        final ShaderEffect effect = new ShaderEffect(Shaders.getShaderPath() + "freeze.cgfx", "freeze");
        Shaders.start(effect, new StrengthHandler(0.0f) {
            @Override
            public float getStrength() {
                return Snow.this.m_strength.getCurrent();
            }
        });
        this.m_freezeEffectId = effect.getId();
    }
    
    private void stopFreeze() {
        if (this.m_freezeEffectId == 0) {
            return;
        }
        Shaders.removeEffect(this.m_freezeEffectId);
        this.m_freezeEffectId = 0;
    }
    
    @Override
    void stop() {
        super.stop();
        if (this.m_snowEffectId != 0) {
            final EffectBase worldEffect = EffectManager.getInstance().getWorldEffect(this.m_snowEffectId);
            if (worldEffect != null) {
                worldEffect.activate(false);
                EffectManager.getInstance().removeWorldEffect(worldEffect);
                worldEffect.clear();
            }
            this.m_snowEffectId = 0;
        }
        this.stopFreeze();
        this.m_isRunning = false;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            final SnowParticleEffect snowEffect = (SnowParticleEffect)EffectManager.getInstance().getWorldEffect(this.m_snowEffectId);
            if (snowEffect != null) {
                snowEffect.setLivingParticleTime(400);
            }
        }
    }
    
    @Override
    void update(final IsoWorldScene scene, final float windDirection, final float windStrength) {
        if (!this.m_isRunning) {
            return;
        }
        final float strength = this.m_strength.getCurrent();
        if (strength == 0.0f) {
            this.stop();
            return;
        }
        this.startFreeze();
        final SnowParticleEffect snowEffect = (SnowParticleEffect)EffectManager.getInstance().getWorldEffect(this.m_snowEffectId);
        if (snowEffect != null) {
            final int numParticles = (int)((this.m_visible && strength > 0.33f) ? (2800.0f * strength) : 0.0f);
            snowEffect.setNumUsableParticles(numParticles);
            final float str = 0.5235988f * Math.min(1.0f, 0.4f + windStrength);
            final float min = 1.5707964f - str;
            final float max = 1.5707964f + str;
            snowEffect.setWind(0.05f * (float)Math.cos(MathHelper.random(min, max)));
        }
    }
}
