package com.ankamagames.framework.graphics.engine.particleSystem.definitions;

import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class EmitterDefinition extends Affectorable
{
    public int m_id;
    public ArrayList<EmitterDefinition> m_emitterDefinitions;
    public final ArrayList<ParticleModel> m_models;
    public LightDefinition m_lightDefinition;
    public final float m_startSpawnTime;
    public final float m_endSpawnTime;
    public final int m_maxParticlesCount;
    public final int m_maxParticlesPerSpawn;
    public final float m_spawnFrequency;
    public final float m_spawnFrequencyRandom;
    public final float m_particleLifeTime;
    public final float m_particleLifeTimeRandom;
    public final float m_particleOffsetX;
    public final float m_particleOffsetY;
    public final float m_particleOffsetZ;
    public final float m_particleOffsetRandomX;
    public final float m_particleOffsetRandomY;
    public final float m_particleOffsetRandomZ;
    public final float m_particleVelocityX;
    public final float m_particleVelocityY;
    public final float m_particleVelocityZ;
    public final float m_particleVelocityRandomX;
    public final float m_particleVelocityRandomY;
    public final float m_particleVelocityRandomZ;
    public boolean m_geocentric;
    
    public EmitterDefinition(final float startSpawnTime, final float endSpawnTime, final int maxParticlesCount, final int maxParticlesPerSpawn, final float spawnFrequency, final float spawnFrequencyRandom, final float particleLifeTime, final float particleLifeTimeRandom, final float particleOffsetX, final float particleOffsetY, final float particleOffsetZ, final float particleOffsetRandomX, final float particleOffsetRandomY, final float particleOffsetRandomZ, final float particleVelocityX, final float particleVelocityY, final float particleVelocityZ, final float particleVelocityRandomX, final float particleVelocityRandomY, final float particleVelocityRandomZ, final boolean geocentric) {
        super();
        this.m_models = new ArrayList<ParticleModel>(1);
        this.m_startSpawnTime = startSpawnTime;
        this.m_endSpawnTime = endSpawnTime;
        this.m_maxParticlesCount = maxParticlesCount;
        this.m_maxParticlesPerSpawn = maxParticlesPerSpawn;
        this.m_spawnFrequency = spawnFrequency;
        this.m_spawnFrequencyRandom = spawnFrequencyRandom;
        this.m_particleLifeTime = particleLifeTime;
        this.m_particleLifeTimeRandom = particleLifeTimeRandom;
        this.m_particleOffsetX = particleOffsetX;
        this.m_particleOffsetY = particleOffsetY;
        this.m_particleOffsetZ = particleOffsetZ;
        this.m_particleOffsetRandomX = particleOffsetRandomX;
        this.m_particleOffsetRandomY = particleOffsetRandomY;
        this.m_particleOffsetRandomZ = particleOffsetRandomZ;
        this.m_particleVelocityX = particleVelocityX;
        this.m_particleVelocityY = particleVelocityY;
        this.m_particleVelocityZ = particleVelocityZ;
        this.m_particleVelocityRandomX = particleVelocityRandomX;
        this.m_particleVelocityRandomY = particleVelocityRandomY;
        this.m_particleVelocityRandomZ = particleVelocityRandomZ;
        this.m_geocentric = geocentric;
    }
    
    public void reset() {
        this.m_models.clear();
        this.resetAffectors();
        this.m_lightDefinition = null;
        this.m_geocentric = false;
    }
    
    public void addParticleModel(final ParticleModel model) {
        this.m_models.add(model);
    }
    
    public void initializeParticle(final ParticleSystem particleSystem, final Particle emitter, final Particle particle) {
        this.initializeParticle(emitter, particle);
        if (!emitter.m_geocentric) {
            particle.m_x += emitter.getX();
            particle.m_y += emitter.getY();
            particle.m_z += emitter.getZ();
        }
        if (this.m_emitterDefinitions != null) {
            particle.addEmitters(particleSystem, this.m_emitterDefinitions);
        }
    }
    
    public void initializeParticle(final Particle emitter, final Particle particle) {
        float offsetX = this.m_particleOffsetX;
        float offsetY = this.m_particleOffsetY;
        float offsetZ = this.m_particleOffsetZ;
        float velocityX = this.m_particleVelocityX;
        float velocityY = this.m_particleVelocityY;
        float velocityZ = this.m_particleVelocityZ;
        if (this.m_particleOffsetRandomX != 0.0f) {
            offsetX += (MathHelper.randomFloat() - 0.5f) * this.m_particleOffsetRandomX;
        }
        if (this.m_particleOffsetRandomY != 0.0f) {
            offsetY += (MathHelper.randomFloat() - 0.5f) * this.m_particleOffsetRandomY;
        }
        if (this.m_particleOffsetRandomZ != 0.0f) {
            offsetZ += (MathHelper.randomFloat() - 0.5f) * this.m_particleOffsetRandomZ;
        }
        if (this.m_particleVelocityRandomX != 0.0f) {
            velocityX += (MathHelper.randomFloat() - 0.5f) * this.m_particleVelocityRandomX;
        }
        if (this.m_particleVelocityRandomY != 0.0f) {
            velocityY += (MathHelper.randomFloat() - 0.5f) * this.m_particleVelocityRandomY;
        }
        if (this.m_particleVelocityRandomZ != 0.0f) {
            velocityZ += (MathHelper.randomFloat() - 0.5f) * this.m_particleVelocityRandomZ;
        }
        particle.m_x = offsetX;
        particle.m_y = offsetY;
        particle.m_z = offsetZ;
        particle.m_velocityX = velocityX;
        particle.m_velocityY = velocityY;
        particle.m_velocityZ = velocityZ;
        particle.m_lifeTime = this.m_particleLifeTime + MathHelper.randomFloat() * this.m_particleLifeTimeRandom;
        particle.m_life = 0.0f;
        particle.m_geocentric = this.m_geocentric;
        particle.m_parent = emitter;
    }
    
    public void addEmitterDefinition(final EmitterDefinition definition) {
        if (definition == null) {
            return;
        }
        if (this.m_emitterDefinitions == null) {
            this.m_emitterDefinitions = new ArrayList<EmitterDefinition>(1);
        }
        this.m_emitterDefinitions.add(definition);
    }
    
    public int getRandomParticleModelIndex() {
        final int size = this.m_models.size();
        if (size == 0) {
            return -1;
        }
        return MathHelper.random(size);
    }
    
    public void setLightDefinition(final LightDefinition lightDefinition) {
        this.m_lightDefinition = lightDefinition;
    }
    
    public Emitter createEmitter() {
        final Emitter emitter = Emitter.Factory.newPooledInstance();
        emitter.setDefinition(this);
        return emitter;
    }
}
