package com.ankamagames.baseImpl.graphics.isometric.particles;

import com.ankamagames.framework.sound.group.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.debug.particlesAndLights.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class IsoParticleSystem extends ParticleSystem implements Maskable, ObservedSource, LitSceneObject
{
    public static final Rect BOUNDS;
    private short m_layerId;
    private int m_groupKey;
    private byte m_lod;
    private final Rect m_bounds;
    private boolean m_removedWhenFar;
    
    protected IsoParticleSystem(final boolean isEditable) {
        super(isEditable);
        this.m_bounds = new Rect(IsoParticleSystem.BOUNDS);
        this.m_removedWhenFar = true;
        MaskableHelper.setUndefined(this);
    }
    
    public void prepareParticlesBeforeRendering(final IsoWorldScene scene) {
        final int numGeometries = this.getNumGeometries();
        if (numGeometries == 0) {
            return;
        }
        this.setGlobalColor(scene.getIsoCamera().getLayerColor(this));
        if (this.m_globalColor[3] < 0.004f) {
            return;
        }
        scene.addEntity(this, this.m_renderRadius > 0.0f);
        for (int i = 0; i < numGeometries; ++i) {
            final GeometryMesh geometryMesh = (GeometryMesh)this.getGeometry(i);
            geometryMesh.getVertexBuffer().clear();
        }
        final float systemX = this.getX();
        final float systemY = this.getY();
        final float systemZ = this.getZ();
        this.m_cellX = systemX;
        this.m_cellY = systemY;
        this.m_cellZ = systemZ;
        this.m_height = 0.0f;
        this.m_minX = Integer.MAX_VALUE;
        this.m_minY = Integer.MAX_VALUE;
        this.m_maxX = Integer.MIN_VALUE;
        this.m_maxY = Integer.MIN_VALUE;
        float deltaX;
        float deltaY;
        float deltaZ;
        if (this.m_geocentric) {
            deltaX = systemX;
            deltaY = systemY;
            deltaZ = systemZ;
        }
        else {
            deltaY = (deltaX = (deltaZ = 0.0f));
        }
        this.drawParticleRecurse(this.m_root, deltaX, deltaY, deltaZ, scene, 0);
        this.updateGeometry();
        this.m_previousGeometry = null;
    }
    
    public final int totalParticleCount() {
        return this.totalParticleCount(this.m_root);
    }
    
    private int totalParticleCount(final Particle particle) {
        int count = 0;
        if (particle == null) {
            return count;
        }
        if (particle != this.m_root && particle.m_alpha > 0.004f) {
            ++count;
        }
        final Emitter[] emitters = particle.m_emitters;
        if (emitters == null) {
            return count;
        }
        for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
            final ParticleList particles = emitters[emitterIndex].m_children;
            if (particles != null) {
                for (int numParticles = particles.size(), particleIndex = 0; particleIndex < numParticles; ++particleIndex) {
                    final Particle p = particles.get(particleIndex);
                    count += this.totalParticleCount(p);
                }
            }
        }
        return count;
    }
    
    private void drawParticleRecurse(final Particle particle, final float deltaX, final float deltaY, final float deltaZ, final IsoWorldScene scene, final int level) {
        if (particle == null) {
            return;
        }
        if (ParticleSystem.particleBufferIsFull()) {
            return;
        }
        if (particle != this.m_root && particle.m_alpha > 0.004f) {
            final float isoX = particle.m_x + deltaX;
            final float isoY = particle.m_y + deltaY;
            final float isoZ = particle.m_z + deltaZ;
            final Point2 pt = IsoCameraFunc.getScreenPosition(scene, isoX, isoY, isoZ);
            this.addParticleGeometry(particle, pt.m_x, pt.m_y);
        }
        final Emitter[] emitters = particle.m_emitters;
        if (emitters == null) {
            return;
        }
        for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
            final Emitter emitter = emitters[emitterIndex];
            final ParticleList particles = emitter.m_children;
            if (particles != null) {
                float dx;
                float dy;
                float dz;
                if (!emitter.m_definition.m_geocentric) {
                    dx = deltaX;
                    dy = deltaY;
                    dz = deltaZ;
                }
                else {
                    dx = deltaX + particle.m_x;
                    dy = deltaY + particle.m_y;
                    dz = deltaZ + particle.m_z;
                }
                for (int numParticles = particles.size(), particleIndex = 0; particleIndex < numParticles; ++particleIndex) {
                    final Particle p = particles.get(particleIndex);
                    this.drawParticleRecurse(p, dx, dy, dz, scene, level + 1);
                }
            }
        }
    }
    
    @Override
    public final int getMaskKey() {
        return this.m_groupKey;
    }
    
    @Override
    public final short getLayerId() {
        return this.m_layerId;
    }
    
    @Override
    public final void setMaskKey(final int key, final short layerId) {
        this.m_groupKey = key;
        this.m_layerId = layerId;
    }
    
    public final byte getLod() {
        return this.m_lod;
    }
    
    public final void setLod(final byte lod) {
        this.m_lod = lod;
    }
    
    @Override
    protected void immediateKill() {
        super.immediateKill();
        ParticlesAndLightDebug.INSTANCE.removeParticleSystem(this);
    }
    
    @Override
    public float getObservedX() {
        return this.getX() - this.getY();
    }
    
    @Override
    public float getObservedY() {
        return -(this.getX() + this.getY());
    }
    
    @Override
    public float getObservedZ() {
        return this.getZ() / 4.8f;
    }
    
    @Override
    public boolean isPositionRelative() {
        return false;
    }
    
    @Override
    public int getGroupInstanceId() {
        return this.getMaskKey();
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        MaskableHelper.setUndefined(this);
        this.removeEffectForUI();
        this.m_removedWhenFar = true;
    }
    
    public boolean isMaskUndefined() {
        return MaskableHelper.isUndefined(this);
    }
    
    public int getDeltaZ() {
        return this.m_behindMobile ? LayerOrder.APS.getDeltaZ() : LayerOrder.APS_FRONT.getDeltaZ();
    }
    
    public boolean computeZOrderAndMaskKey(final IsoWorldScene scene) {
        return scene.initialize(this, this, this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this.getDeltaZ(), true);
    }
    
    @Override
    public int getWorldCellX() {
        return MathHelper.fastRound(this.getX());
    }
    
    @Override
    public int getWorldCellY() {
        return MathHelper.fastRound(this.getY());
    }
    
    @Override
    public short getWorldCellAltitude() {
        return (short)MathHelper.fastFloor(this.getZ());
    }
    
    @Override
    public void applyLighting(final float[] colors) {
        this.m_colorHelper.applyDelayed(colors);
    }
    
    public Rect getBounds() {
        return this.m_bounds;
    }
    
    public boolean isRemovedWhenFar() {
        return this.m_removedWhenFar;
    }
    
    public void setRemovedWhenFar(final boolean removedWhenFar) {
        this.m_removedWhenFar = removedWhenFar;
    }
    
    static {
        BOUNDS = new Rect(-512, 512, -512, 512);
    }
}
