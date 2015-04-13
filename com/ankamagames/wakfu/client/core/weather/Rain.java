package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.alea.display.effects.cloud.*;
import com.ankamagames.wakfu.client.core.weather.postprocess.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

class Rain extends WeatherEffect
{
    private static final boolean BLOOM_ENABLED = false;
    private static final int MAX_PARTICLES_COUNT = 2048;
    private static final float INITIAL_PARTICLE_ACCELERATION = 1.5f;
    private int m_rainEffectId;
    private int m_rainDropEffectId;
    private boolean m_sunshineAdded;
    private long m_timeStop;
    private boolean m_stopped;
    private final ArrayList<FreeParticleSystem> m_particleSystems;
    private final CloudModifier m_cloudModifier;
    private PostProcessRain m_postprocess;
    private float m_highlight;
    private final RainParams m_params;
    
    protected Rain(final RainParams params) {
        super();
        this.m_sunshineAdded = false;
        this.m_particleSystems = new ArrayList<FreeParticleSystem>();
        this.m_cloudModifier = new CloudModifier();
        this.m_params = params;
    }
    
    Rain() {
        super();
        this.m_sunshineAdded = false;
        this.m_particleSystems = new ArrayList<FreeParticleSystem>();
        this.m_cloudModifier = new CloudModifier();
        this.m_params = new RainParams();
    }
    
    public void setHighlight(final float highlight) {
        this.m_highlight = highlight;
        if (this.m_postprocess != null) {
            this.m_postprocess.setHighlight(highlight);
        }
    }
    
    @Override
    void start(final IsoWorldScene scene) {
        super.start(scene);
        if (this.m_rainEffectId > 0) {
            return;
        }
        final IsoCamera camera = scene.getIsoCamera();
        Box boundingBox = new Box(-18.0f, 18.0f, -18.0f, 18.0f, 0.0f, 54.0f);
        final RainParticleEffect rainEffect = new RainParticleEffect();
        rainEffect.setBoundingBox(boundingBox);
        rainEffect.setCamera(camera);
        this.m_params.startRainParticleEffect(rainEffect);
        boundingBox = new Box(-18.0f, 18.0f, -18.0f, 18.0f, 0.0f, 18.0f);
        final RainDropParticleEffect rainDropEffect = new RainDropParticleEffect();
        rainDropEffect.setBoundingBox(boundingBox);
        rainDropEffect.setCamera(camera);
        this.m_params.startRainDropParticleEffect(rainDropEffect);
        EffectManager.getInstance().addWorldEffect(rainEffect);
        EffectManager.getInstance().addWorldEffect(rainDropEffect);
        this.m_rainEffectId = rainEffect.getId();
        this.m_rainDropEffectId = rainDropEffect.getId();
        IsoSceneLightManager.INSTANCE.addLightingModifier(this.m_cloudModifier);
        this.m_cloudModifier.setCamera(camera);
        this.m_isRunning = true;
    }
    
    private void enableBloomEffect(final boolean enabled) {
    }
    
    private boolean bloomEnabled() {
        return this.m_postprocess != null;
    }
    
    @Override
    void stop() {
        super.stop();
        if (this.m_rainEffectId == 0) {
            return;
        }
        EffectBase worldEffect = EffectManager.getInstance().getWorldEffect(this.m_rainEffectId);
        if (worldEffect != null) {
            worldEffect.activate(false);
            EffectManager.getInstance().removeWorldEffect(worldEffect);
            worldEffect.clear();
        }
        this.m_rainEffectId = 0;
        worldEffect = EffectManager.getInstance().getWorldEffect(this.m_rainDropEffectId);
        if (worldEffect != null) {
            worldEffect.activate(false);
            EffectManager.getInstance().removeWorldEffect(worldEffect);
            worldEffect.clear();
        }
        this.m_rainDropEffectId = 0;
        this.enableBloomEffect(false);
        if (this.m_sunshineAdded) {
            this.removeSunshine();
        }
        IsoSceneLightManager.INSTANCE.removeLightingModifier(this.m_cloudModifier);
        this.m_isRunning = false;
    }
    
    private void addSunshine() {
        final List<LocalPartition> localPartitionList = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getAllLocalPartitions();
        if (localPartitionList == null) {
            return;
        }
        final int apsId = 86;
        final int level = 0;
        final TLongArrayList partitions = new TLongArrayList();
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        for (int i = 0, size = localPartitionList.size(); i < size; ++i) {
            final LocalPartition localPartition = localPartitionList.get(i);
            final long key = MathHelper.getLongFromTwoInt(localPartition.getX(), localPartition.getY());
            if (!partitions.contains(key)) {
                partitions.add(key);
                final int x = localPartition.getX() * 18;
                final int y = localPartition.getY() * 18;
                final short z = TopologyMapManager.getNearestZ(x, y, player.getPosition().getZ());
                final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(86, 0);
                system.setPosition(x, y, z);
                IsoParticleSystemManager.getInstance().addParticleSystem(system);
                this.m_particleSystems.add(system);
            }
        }
        this.m_sunshineAdded = true;
    }
    
    private void removeSunshine() {
        for (int count = this.m_particleSystems.size(), i = 0; i < count; ++i) {
            final int id = this.m_particleSystems.get(i).getId();
            IsoParticleSystemManager.getInstance().removeParticleSystem(id, true);
        }
        this.m_particleSystems.clear();
        this.m_sunshineAdded = false;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            if (this.m_sunshineAdded) {
                this.removeSunshine();
            }
            final RainParticleEffect rainEffect = (RainParticleEffect)EffectManager.getInstance().getWorldEffect(this.m_rainEffectId);
            if (rainEffect != null) {
                rainEffect.setLivingParticleTime(400);
            }
        }
    }
    
    @Override
    void update(final IsoWorldScene scene, final float windDirection, final float windStrength) {
        if (!this.m_isRunning) {
            this.m_stopped = true;
            if (this.bloomEnabled()) {
                this.m_postprocess.setThreshold(0.0f);
            }
            return;
        }
        final float strength = this.m_strength.getCurrent();
        final long currentTime = System.currentTimeMillis();
        if (this.bloomEnabled()) {
            float threshold;
            if (currentTime - this.m_timeStop > 0L) {
                threshold = Math.min(1.0f, (currentTime - this.m_timeStop) / 10000.0f);
            }
            else {
                threshold = 1.0f;
            }
            this.m_postprocess.setThreshold((this.m_stopped ? (1.0f - threshold) : threshold) * this.getStrength());
        }
        final boolean stopped = strength <= 0.33f;
        if (stopped != this.m_stopped) {
            this.m_timeStop = currentTime;
            this.m_stopped = stopped;
        }
        if (strength <= 0.0f) {
            this.stop();
            return;
        }
        this.enableBloomEffect(true);
        final RainParticleEffect rainEffect = (RainParticleEffect)EffectManager.getInstance().getWorldEffect(this.m_rainEffectId);
        final RainDropParticleEffect rainDropEffect = (RainDropParticleEffect)EffectManager.getInstance().getWorldEffect(this.m_rainDropEffectId);
        if (rainEffect == null || rainDropEffect == null) {
            return;
        }
        final float wind = windStrength * windDirection;
        rainEffect.setAngle(-1.5707964f - wind * 0.7853982f, 0.117809735f * (wind + 0.3f));
        final int numParticles = (int)((this.m_visible && strength > 0.33f) ? (2048.0f * strength) : 0.0f);
        rainEffect.setNumUsableParticles(numParticles);
        rainDropEffect.setNumUsableParticles(numParticles);
        rainEffect.setParticleAcceleration(1.5f * (1.0f + strength * 1.5f));
        final float lightIntensity = SunLightModifier.INSTANCE.getDayLightIntensity();
        if (lightIntensity > 0.7f && this.m_strength.getNext() < this.m_strength.getCurrent() && this.m_strength.getNext() < 0.33f) {
            if (!this.m_sunshineAdded && this.m_visible) {
                this.addSunshine();
            }
        }
        else if (this.m_sunshineAdded) {
            this.removeSunshine();
        }
        final float bias = 1.0f - 0.5f * strength;
        final float noiseScale = (1.1f - strength) * MathHelper.lerp(0.5f, 1.0f, lightIntensity);
        this.m_cloudModifier.setNoiseBias(bias);
        this.m_cloudModifier.setNoiseScale(noiseScale);
        this.m_cloudModifier.setDirection(-wind * (2.0f - strength) * 5.0f, 0.0f);
        this.m_cloudModifier.setSharpness(1.0f);
        if (this.bloomEnabled()) {
            this.m_postprocess.setStrength(strength);
        }
    }
    
    protected static class RainParams
    {
        int numParticles;
        float numParticlesPerSpawn;
        float spawnFrequency;
        float particleLifeTime;
        float particleHeight;
        float particleVelocity;
        float particleAcceleration;
        float particleAlpha;
        float particleAngle;
        float particleAngleRandom;
        
        protected RainParams() {
            super();
            this.numParticles = 2048;
            this.numParticlesPerSpawn = 64.0f;
            this.spawnFrequency = 100.0f;
            this.particleLifeTime = 2000.0f;
            this.particleHeight = 40.0f;
            this.particleVelocity = 18.0f;
            this.particleAcceleration = 1.5f;
            this.particleAlpha = 0.1f;
            this.particleAngle = -1.5707964f;
            this.particleAngleRandom = 0.07853982f;
        }
        
        protected final void startRainParticleEffect(final RainParticleEffect rainEffect) {
            rainEffect.initialize(this.numParticles);
            rainEffect.setColor(1.0f, 1.0f, 1.0f, this.particleAlpha, 0.1f);
            rainEffect.setAngle(this.particleAngle, this.particleAngleRandom);
            rainEffect.start(this.numParticlesPerSpawn, this.spawnFrequency, this.particleLifeTime, this.particleHeight, this.particleVelocity, this.particleAcceleration);
        }
        
        protected final void startRainDropParticleEffect(final RainDropParticleEffect rainDropEffect) {
            rainDropEffect.initialize(this.numParticles);
            rainDropEffect.start(this.numParticlesPerSpawn, this.spawnFrequency);
        }
    }
}
