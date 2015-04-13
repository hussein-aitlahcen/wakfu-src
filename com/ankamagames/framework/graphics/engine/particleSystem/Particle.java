package com.ankamagames.framework.graphics.engine.particleSystem;

import com.ankamagames.framework.kernel.core.common.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.particleSystem.definitions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.models.*;
import com.sun.opengl.util.texture.*;
import com.ankamagames.framework.graphics.engine.geometry.*;

public class Particle extends MemoryObject
{
    public static final ObjectFactory Factory;
    public float m_x;
    public float m_y;
    public float m_z;
    public float m_lastX;
    public float m_lastY;
    public float m_lastZ;
    public float m_velocityX;
    public float m_velocityY;
    public float m_velocityZ;
    public float m_angleX;
    public float m_angleY;
    public float m_angleZ;
    public float m_baseAngleX;
    public float m_baseAngleY;
    public float m_baseAngleZ;
    public float m_keyFramedLife;
    public float m_life;
    public float m_lifeTime;
    public float m_angle;
    public float m_scaleX;
    public float m_scaleY;
    public float m_red;
    public float m_green;
    public float m_blue;
    public float m_alpha;
    public float m_halfWidth;
    public float m_halfHeight;
    public float m_hotX;
    public float m_hotY;
    public float m_textureTop;
    public float m_textureLeft;
    public float m_textureBottom;
    public float m_textureRight;
    public int m_batchId;
    public ParticleModel m_model;
    public Particle m_parent;
    public Emitter m_sourceEmitter;
    public boolean m_geocentric;
    public Emitter[] m_emitters;
    
    Particle() {
        super();
        this.initialize();
    }
    
    public void reset() {
        if (this.m_emitters != null) {
            final Emitter[] emitters = this.m_emitters;
            for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                final Emitter emitter = emitters[emitterIndex];
                emitter.clear();
                emitter.removeReference();
            }
            this.m_emitters = null;
        }
        this.m_model = null;
    }
    
    public void addEmitters(final ParticleSystem particleSystem, final ArrayList<EmitterDefinition> emitterDefinitions) {
        final int emitterCount = emitterDefinitions.size();
        if (this.m_emitters == null) {
            this.m_emitters = new Emitter[emitterCount];
        }
        if (particleSystem.isEditable()) {
            final ParticleSystem.EditableData editableData = particleSystem.getEditableData();
            for (int i = 0; i < emitterCount; ++i) {
                final Emitter emitter = emitterDefinitions.get(i).createEmitter();
                emitter.m_parent = this;
                editableData.addEmitter(this.m_emitters[i] = emitter);
            }
        }
        else {
            for (int j = 0; j < emitterCount; ++j) {
                final Emitter emitter2 = emitterDefinitions.get(j).createEmitter();
                emitter2.m_parent = this;
                this.m_emitters[j] = emitter2;
            }
        }
    }
    
    public void update(final ParticleSystem particleSystem, final float timeIncrement) {
        this.m_life += timeIncrement;
        this.m_x += this.m_velocityX * timeIncrement;
        this.m_y += this.m_velocityY * timeIncrement;
        this.m_z += this.m_velocityZ * timeIncrement;
        if (this.m_model != null && this.m_model.isSequence()) {
            final ParticleBitmapSequenceModel model = (ParticleBitmapSequenceModel)this.m_model;
            final TextureCoords textureCoords = model.getTextureCoordinates((int)(1000.0f * timeIncrement));
            this.m_textureTop = textureCoords.top();
            this.m_textureLeft = textureCoords.left();
            this.m_textureBottom = textureCoords.bottom();
            this.m_textureRight = textureCoords.right();
        }
        if (this.m_emitters == null) {
            return;
        }
        final Emitter[] emitters = this.m_emitters;
        for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
            emitters[emitterIndex].update(particleSystem, timeIncrement);
        }
    }
    
    public boolean isAlive() {
        if (this.m_life <= this.m_lifeTime && this.m_lifeTime != Float.MAX_VALUE) {
            return true;
        }
        if (this.m_emitters != null && this.m_parent == null) {
            final Emitter[] emitters = this.m_emitters;
            for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                if (emitters[emitterIndex].isAlive()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void kill(final ParticleSystem particleSystem) {
        if (this.m_emitters == null) {
            return;
        }
        final Emitter[] emitters = this.m_emitters;
        for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
            final Emitter emitter = emitters[emitterIndex];
            emitter.clearLights();
            if (emitter.m_geometries != null) {
                for (int numGeometries = emitter.m_geometries.size(), geometryIndex = 0; geometryIndex < numGeometries; ++geometryIndex) {
                    final GeometryMesh geometryMesh = emitter.m_geometries.get(geometryIndex);
                    particleSystem.removeTextureGeometry(geometryMesh);
                }
                emitter.m_geometries.clear();
            }
            final ParticleList children = emitter.m_children;
            if (children != null) {
                for (int i = 0, size2 = children.size(); i < size2; ++i) {
                    children.get(i).kill(particleSystem);
                }
            }
            emitter.removeReference();
        }
        this.m_emitters = null;
    }
    
    public final boolean isEmitter() {
        return this.m_emitters != null;
    }
    
    public float getX() {
        if (this.m_geocentric || this.m_parent == null || this.m_parent.m_geocentric) {
            return this.m_x;
        }
        return this.m_parent.m_geocentric ? this.m_parent.getX() : 0.0f;
    }
    
    public float getY() {
        if (this.m_geocentric || this.m_parent == null || this.m_parent.m_geocentric) {
            return this.m_y;
        }
        return this.m_parent.m_geocentric ? this.m_parent.getY() : 0.0f;
    }
    
    public float getZ() {
        if (this.m_geocentric || this.m_parent == null || this.m_parent.m_geocentric) {
            return this.m_z;
        }
        return this.m_parent.m_geocentric ? this.m_parent.getZ() : 0.0f;
    }
    
    @Override
    protected void checkout() {
        this.initialize();
    }
    
    @Override
    protected void checkin() {
        this.reset();
        this.m_parent = null;
        this.m_sourceEmitter = null;
    }
    
    private void initialize() {
        this.m_lastX = Float.NaN;
        this.m_lastY = Float.NaN;
        this.m_lastZ = Float.NaN;
        this.m_keyFramedLife = 0.0f;
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<Particle>
    {
        public ObjectFactory() {
            super(Particle.class);
        }
        
        @Override
        public Particle create() {
            return new Particle();
        }
    }
}
