package com.ankamagames.baseImpl.graphics.alea.display.effects;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.*;

public abstract class FullScreenParticleEffect extends Effect
{
    public float m_spawnFrequency;
    public float m_numParticlePerSpawn;
    protected final Entity3D m_entity;
    protected VertexBufferPCT m_vertexBuffer;
    protected static final int MAX_PARTICLES = 4096;
    protected static final float[] m_particleGeomPosition;
    protected static final float[] m_particleGeomColor;
    protected static final float[] m_particleGeomTexCoord;
    protected final ParticleFactory m_particleFactory;
    protected FreeList m_particleList;
    protected int m_numParticles;
    protected int m_numLivingParticles;
    protected int m_numUsableParticles;
    protected TIntArrayList m_livingParticles;
    protected Particle[] m_particlePool;
    protected Box m_box;
    protected float m_elapsedTime;
    protected float m_lastSpawnTime;
    
    public FullScreenParticleEffect(final ParticleFactory particleFactory) {
        super();
        this.m_entity = Entity3D.Factory.newInstance();
        this.m_particleFactory = particleFactory;
    }
    
    private void initialize(int numMaxParticles, final int numUsableParticles) {
        numMaxParticles = Math.min(numMaxParticles, 4096);
        this.m_numLivingParticles = 0;
        this.m_numParticles = 0;
        this.m_numUsableParticles = Math.min(numUsableParticles, numMaxParticles);
        this.m_particlePool = new Particle[numMaxParticles];
        for (int i = 0; i < numMaxParticles; ++i) {
            this.m_particlePool[i] = this.m_particleFactory.newParticle();
        }
        this.m_particleList = new FreeList(numMaxParticles);
        this.m_livingParticles = new TIntArrayList(numMaxParticles);
        final int numVertices = numMaxParticles * 4;
        this.m_vertexBuffer = VertexBufferPCT.Factory.newInstance(numVertices);
        final GLGeometryMesh geometry = GLGeometryMesh.Factory.newPooledInstance();
        geometry.create(GeometryMesh.MeshType.Quad, this.m_vertexBuffer, IndexBuffer.INDICES);
        geometry.setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
        this.m_entity.addGeometry(geometry);
        geometry.removeReference();
    }
    
    public void initialize(final int numMaxParticles) {
        this.initialize(numMaxParticles, numMaxParticles);
    }
    
    public void setNumUsableParticles(final int numUsableParticles) {
        this.m_numUsableParticles = Math.min(numUsableParticles, this.m_particlePool.length);
    }
    
    @Override
    public void clear() {
        this.m_entity.removeReference();
    }
    
    @Override
    public void reset() {
        this.m_elapsedTime = 0.0f;
        this.m_lastSpawnTime = 0.0f;
        if (this.m_particleList != null) {
            for (int i = 0; i < this.m_numLivingParticles; ++i) {
                this.m_particleList.checkin(this.m_livingParticles.get(i) - 1);
            }
        }
        if (this.m_livingParticles != null) {
            this.m_livingParticles.clear();
        }
        this.m_numLivingParticles = 0;
    }
    
    @Override
    public void update(final int timeIncrement) {
        this.m_elapsedTime += timeIncrement;
        if (this.m_camera == null) {
            return;
        }
        this.killParticleOutOfBox();
        final float deltaT = timeIncrement / 1000.0f;
        this.m_lastSpawnTime += deltaT;
        this.spawnNewParticles();
        this.updateLivingParticles(deltaT);
        this.m_vertexBuffer.clear();
        int numParticles = 0;
        int texCoordIndex = 0;
        int colorIndex = 0;
        int positionIndex = 0;
        for (int particleIndex = 0; particleIndex < this.m_numLivingParticles; ++particleIndex) {
            final int particleId = this.m_livingParticles.getQuick(particleIndex) - 1;
            final Particle particle = this.m_particlePool[particleId];
            final float x = (particle.m_x - particle.m_y) * 43.0f;
            final float y = -(particle.m_x + particle.m_y) * 21.5f + particle.m_z * 10.0f;
            float factor = 1.0f;
            if (!this.useZoomRatio()) {
                factor = 1.5f / this.m_camera.getZoomResolutionFactor();
            }
            final float width = particle.m_width * factor;
            final float height = particle.m_height * factor;
            if (this.useRotation()) {
                final float angleCos = MathHelper.cosf(particle.m_angle);
                final float angleSin = MathHelper.sinf(particle.m_angle);
                final float txAxisX = angleCos * height;
                final float txAxisY = angleSin * height;
                final float tyAxisX = -angleSin * width;
                final float tyAxisY = angleCos * width;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x - txAxisX - tyAxisX;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y - txAxisY - tyAxisY;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x - txAxisX + tyAxisX;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y - txAxisY + tyAxisY;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x + txAxisX + tyAxisX;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y + txAxisY + tyAxisY;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x + txAxisX - tyAxisX;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y + txAxisY - tyAxisY;
            }
            else {
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x - width;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y - height;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x - width;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y + height;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x + width;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y + height;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = x + width;
                FullScreenParticleEffect.m_particleGeomPosition[positionIndex++] = y - height;
            }
            final float alpha = particle.m_alpha;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_red;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_green;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_blue;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = alpha;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_red;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_green;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_blue;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = alpha;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_red;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_green;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_blue;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = alpha;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_red;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_green;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = particle.m_blue;
            FullScreenParticleEffect.m_particleGeomColor[colorIndex++] = alpha;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_left;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_bottom;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_left;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_top;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_right;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_top;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_right;
            FullScreenParticleEffect.m_particleGeomTexCoord[texCoordIndex++] = particle.m_bottom;
            ++numParticles;
        }
        this.m_vertexBuffer.addPositions(FullScreenParticleEffect.m_particleGeomPosition, positionIndex);
        this.m_vertexBuffer.addColors(FullScreenParticleEffect.m_particleGeomColor, colorIndex);
        this.m_vertexBuffer.addTexCoords(FullScreenParticleEffect.m_particleGeomTexCoord, texCoordIndex);
        this.m_vertexBuffer.setNumVertices(numParticles * 4);
    }
    
    private void updateLivingParticles(final float deltaT) {
        for (int particleIndex = 0; particleIndex < this.m_numLivingParticles; ++particleIndex) {
            final int particleId = this.m_livingParticles.getQuick(particleIndex) - 1;
            final Particle particle = this.m_particlePool[particleId];
            this.updateParticle(particle, deltaT);
        }
    }
    
    private void spawnNewParticles() {
        if (this.m_lastSpawnTime > this.m_spawnFrequency) {
            for (int numParticleSpawned = 0; numParticleSpawned < this.m_numParticlePerSpawn && this.m_numLivingParticles < this.m_numUsableParticles; ++numParticleSpawned) {
                final int particleId = this.m_particleList.checkout();
                this.m_livingParticles.add(particleId + 1);
                ++this.m_numLivingParticles;
                final Particle particle = this.m_particlePool[particleId];
                this.initializeParticle(particle);
            }
            this.m_lastSpawnTime -= this.m_spawnFrequency;
        }
    }
    
    private void killParticleOutOfBox() {
        final float positionZ = this.m_camera.getAltitude();
        int particleIndex = 0;
        while (particleIndex < this.m_numLivingParticles) {
            final int particleId = this.m_livingParticles.getQuick(particleIndex) - 1;
            final Particle particle = this.m_particlePool[particleId];
            final float z = particle.m_z - positionZ;
            if (z <= this.m_box.getzMax() && z >= this.m_box.getzMin() && particle.m_life <= particle.m_lifeTime) {
                ++particleIndex;
            }
            else {
                --this.m_numLivingParticles;
                this.m_livingParticles.remove(particleIndex);
                this.m_particleList.checkin(particleId);
            }
        }
    }
    
    @Override
    public void render(final Renderer renderer) {
        this.m_entity.render(renderer);
    }
    
    public void setBoundingBox(final Box box) {
        this.m_box = box;
    }
    
    protected abstract boolean useRotation();
    
    protected abstract boolean useZoomRatio();
    
    protected abstract void initializeParticle(final Particle p0);
    
    protected abstract void updateParticle(final Particle p0, final float p1);
    
    public final void setLivingParticleTime(final int time) {
        final float t = time / 1000.0f;
        for (int particleIndex = 0; particleIndex < this.m_numLivingParticles; ++particleIndex) {
            final int particleId = this.m_livingParticles.getQuick(particleIndex) - 1;
            if (this.m_particlePool[particleId].m_lifeTime > t) {
                this.m_particlePool[particleId].m_lifeTime = t;
            }
        }
    }
    
    static {
        m_particleGeomPosition = new float[32768];
        m_particleGeomColor = new float[65536];
        m_particleGeomTexCoord = new float[32768];
    }
}
