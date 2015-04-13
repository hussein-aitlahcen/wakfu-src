package com.ankamagames.framework.graphics.engine.particleSystem;

import com.ankamagames.framework.kernel.core.common.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.particleSystem.definitions.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Emitter extends MemoryObject
{
    public static final ObjectFactory Factory;
    public float m_elapsedTime;
    public float m_keyFrameTime;
    public float m_timeForSpawn;
    public boolean m_isVisible;
    public boolean m_stopEmitting;
    public ArrayList<GeometryMesh> m_geometries;
    public ParticleList m_children;
    public Particle m_parent;
    public EmitterDefinition m_definition;
    public LightDefinition m_lightDefinition;
    public Particle m_lightParticle;
    private Light m_light;
    private float m_randomFrequency;
    
    private Emitter() {
        super();
        this.m_randomFrequency = 0.0f;
        this.clear();
    }
    
    public void setDefinition(final EmitterDefinition emitterDefinition) {
        this.m_definition = emitterDefinition;
        this.setLightDefinition(emitterDefinition.m_lightDefinition);
    }
    
    public void update(final ParticleSystem particleSystem, final float timeIncrement) {
        this.m_elapsedTime += timeIncrement;
        if (this.m_children != null) {
            for (int childIndex = 0, size = this.m_children.size(); childIndex < size; ++childIndex) {
                final Particle particle = this.m_children.get(childIndex);
                if (!particle.isAlive()) {
                    particle.kill(particleSystem);
                    particle.removeReference();
                    this.m_children.remove(childIndex);
                }
            }
            this.m_children.compact();
        }
        if (this.canSpawnParticles()) {
            this.spawnParticles(particleSystem, timeIncrement);
        }
        if (this.m_lightDefinition != null) {
            if (this.m_lightDefinition.hasAffector()) {
                for (int numAffectors = this.m_lightDefinition.getAffectorsSize(), affectorIndex = 0; affectorIndex < numAffectors; ++affectorIndex) {
                    final Affector affector = this.m_lightDefinition.getAffectors(affectorIndex);
                    affector.update(timeIncrement, this.m_parent, this.m_lightParticle, particleSystem);
                }
            }
            if (this.m_lightDefinition.hasKeyFramedAffector()) {
                for (int numLightKeyFramedAffectors = this.m_lightDefinition.getKeyFramedAffectorsSize(), affectorIndex = 0; affectorIndex < numLightKeyFramedAffectors; ++affectorIndex) {
                    final Affector affector = this.m_lightDefinition.getKeyFramedAffectors(affectorIndex);
                    affector.update(0.03f, this.m_parent, this.m_lightParticle, particleSystem);
                }
            }
            this.m_light.update(timeIncrement, this, particleSystem);
        }
        if (this.m_children == null) {
            return;
        }
        final int numChildren = this.m_children.size();
        if (this.m_definition.hasKeyFramedAffector()) {
            if (numChildren != 0) {
                this.m_keyFrameTime += timeIncrement;
            }
            final int numKeyFramedAffectors = this.m_definition.getKeyFramedAffectorsSize();
            while (this.m_keyFrameTime >= 0.03f) {
                for (int childIndex2 = 0; childIndex2 < numChildren; ++childIndex2) {
                    final Particle particle2 = this.m_children.get(childIndex2);
                    final float life = particle2.m_life;
                    particle2.m_life = particle2.m_keyFramedLife;
                    if (particle2.m_keyFramedLife <= particle2.m_lifeTime) {
                        for (int affectorIndex2 = 0; affectorIndex2 < numKeyFramedAffectors; ++affectorIndex2) {
                            final Affector affector2 = this.m_definition.getKeyFramedAffectors(affectorIndex2);
                            affector2.update(0.03f, this.m_parent, particle2, particleSystem);
                        }
                    }
                    final Particle particle3 = particle2;
                    particle3.m_keyFramedLife += 0.03f;
                    particle2.m_life = life;
                }
                this.m_keyFrameTime -= 0.03f;
            }
        }
        if (this.m_definition.hasAffector()) {
            for (int numAffectors2 = this.m_definition.getAffectorsSize(), affectorIndex3 = 0; affectorIndex3 < numAffectors2; ++affectorIndex3) {
                final Affector affector3 = this.m_definition.getAffectors(affectorIndex3);
                for (int childIndex3 = numChildren - 1; childIndex3 >= 0 && !affector3.update(timeIncrement, this.m_parent, this.m_children.get(childIndex3), particleSystem); --childIndex3) {}
            }
            for (int childIndex2 = 0; childIndex2 < numChildren; ++childIndex2) {
                this.m_children.get(childIndex2).update(particleSystem, timeIncrement);
            }
        }
        else {
            for (int childIndex4 = 0; childIndex4 < numChildren; ++childIndex4) {
                this.m_children.get(childIndex4).update(particleSystem, timeIncrement);
            }
        }
    }
    
    public boolean isAlive() {
        if (this.willSpawnParticles()) {
            return true;
        }
        if (this.canSpawnParticles()) {
            return true;
        }
        if (this.m_children == null) {
            return false;
        }
        for (int numChildren = this.m_children.size(), childIndex = 0; childIndex < numChildren; ++childIndex) {
            final Particle particle = this.m_children.get(childIndex);
            if (particle.m_life < particle.m_lifeTime) {
                return true;
            }
            if (particle.isEmitter()) {
                final Emitter[] emitters = particle.m_emitters;
                for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                    if (emitters[emitterIndex].isAlive()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean willSpawnParticles() {
        return this.m_elapsedTime < this.m_definition.m_startSpawnTime;
    }
    
    public boolean canSpawnParticles() {
        return (this.m_parent == null || this.m_parent.m_life > 0.0f) && !this.m_stopEmitting && this.m_isVisible && this.m_elapsedTime >= this.m_definition.m_startSpawnTime && (this.m_definition.m_endSpawnTime == 0.0f || this.m_elapsedTime <= this.m_definition.m_endSpawnTime);
    }
    
    public void spawnParticles(final ParticleSystem particleSystem, final float timeIncrement) {
        if (this.m_definition.m_spawnFrequency == 0.0f) {
            return;
        }
        this.m_timeForSpawn += timeIncrement;
        final float frequency = this.m_definition.m_spawnFrequency + this.m_randomFrequency;
        if (this.m_children == null) {
            this.m_children = new ParticleList(this.m_definition.m_maxParticlesCount);
        }
        else {
            if (this.m_timeForSpawn < frequency) {
                return;
            }
            if (this.m_children.isFull()) {
                return;
            }
            this.m_timeForSpawn -= frequency;
        }
        this.m_randomFrequency = this.m_definition.m_spawnFrequencyRandom * MathHelper.randomFloat();
        for (int particleSpawned = 0; particleSpawned < this.m_definition.m_maxParticlesPerSpawn; ++particleSpawned) {
            final int modelIndex = this.m_definition.getRandomParticleModelIndex();
            if (modelIndex >= 0) {
                final ParticleModel model = this.m_definition.m_models.get(modelIndex);
                if (model != null) {
                    final Particle particle = model.generateParticle(particleSystem);
                    if (particle != null) {
                        this.m_children.add(particle);
                        particle.m_batchId = modelIndex;
                        particle.m_sourceEmitter = this;
                        this.m_definition.initializeParticle(particleSystem, this.m_parent, particle);
                        model.initializeParticle(particle);
                        if (particle.isEmitter()) {
                            final Emitter[] emitters = particle.m_emitters;
                            for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                                final Emitter emitter = emitters[emitterIndex];
                                if (emitter.canSpawnParticles()) {
                                    emitter.spawnParticles(particleSystem, timeIncrement);
                                }
                            }
                        }
                        if (this.m_children.isFull()) {
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public void stopEmitting(final boolean stop) {
        this.m_stopEmitting = stop;
        if (this.m_children == null) {
            return;
        }
        for (int numChildren = this.m_children.size(), i = 0; i < numChildren; ++i) {
            final Particle particle = this.m_children.get(i);
            if (particle.isEmitter()) {
                for (int emitterIndex = 0, size = particle.m_emitters.length; emitterIndex < size; ++emitterIndex) {
                    final Emitter emitter = particle.m_emitters[emitterIndex];
                    emitter.stopEmitting(stop);
                }
            }
        }
    }
    
    public void reset() {
        if (this.m_children != null) {
            for (int numChildren = this.m_children.size(), i = 0; i < numChildren; ++i) {
                final Particle particle = this.m_children.get(i);
                if (particle.isEmitter()) {
                    final Emitter[] emitters = particle.m_emitters;
                    for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                        emitters[emitterIndex].reset();
                    }
                }
                particle.removeReference();
            }
            this.m_children.clear();
        }
        if (this.m_lightParticle != null) {
            this.m_lightParticle.m_x = 0.0f;
            this.m_lightParticle.m_y = 0.0f;
            this.m_lightParticle.m_z = 0.0f;
            this.m_lightParticle.m_velocityX = 0.0f;
            this.m_lightParticle.m_velocityY = 0.0f;
            this.m_lightParticle.m_velocityZ = 0.0f;
            this.m_lightParticle.m_life = 0.0f;
            this.m_lightParticle.m_lifeTime = 0.0f;
        }
        this.m_elapsedTime = 0.0f;
        this.m_isVisible = true;
        this.m_stopEmitting = false;
        this.m_timeForSpawn = 0.0f;
    }
    
    public void clear() {
        if (this.m_children != null) {
            for (int numChildren = this.m_children.size(), i = 0; i < numChildren; ++i) {
                final Particle particle = this.m_children.get(i);
                if (particle.isEmitter()) {
                    final Emitter[] emitters = particle.m_emitters;
                    for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                        emitters[emitterIndex].clear();
                    }
                }
                particle.removeReference();
            }
            this.m_children.clear();
            this.m_children = null;
        }
        if (this.m_geometries != null) {
            this.m_geometries.clear();
            this.m_geometries = null;
        }
        this.setLightDefinition(null);
        this.m_elapsedTime = 0.0f;
        this.m_keyFrameTime = 0.0f;
        this.m_isVisible = true;
        this.m_stopEmitting = false;
        this.m_timeForSpawn = 0.0f;
        this.m_randomFrequency = 0.0f;
        this.m_parent = null;
        this.m_definition = null;
    }
    
    public void clearLights() {
        if (this.m_lightDefinition == null || this.m_light == null) {
            return;
        }
        this.m_light.unregister();
        this.m_light = null;
    }
    
    public void setLightDefinition(final LightDefinition lightDefinition) {
        if (lightDefinition == this.m_lightDefinition) {
            return;
        }
        if (this.m_light != null) {
            this.m_light.unregister();
            this.m_light = null;
            this.m_lightParticle.reset();
            this.m_lightParticle.removeReference();
            this.m_lightParticle = null;
        }
        this.m_lightDefinition = lightDefinition;
        if (this.m_lightDefinition != null) {
            (this.m_light = this.m_lightDefinition.createLight()).register();
            this.m_lightParticle = this.m_lightDefinition.createLightParticle();
            this.m_definition.initializeParticle(this.m_parent, this.m_lightParticle);
            final Particle lightParticle = this.m_lightParticle;
            lightParticle.m_lifeTime -= 0.1f;
            this.m_lightParticle.m_parent = this.m_parent;
        }
    }
    
    @Override
    protected void checkout() {
    }
    
    @Override
    protected void checkin() {
        this.clear();
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<Emitter>
    {
        public ObjectFactory() {
            super(Emitter.class);
        }
        
        @Override
        public Emitter create() {
            return new Emitter(null);
        }
    }
}
