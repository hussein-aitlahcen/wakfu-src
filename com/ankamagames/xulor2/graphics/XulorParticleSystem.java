package com.ankamagames.xulor2.graphics;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper.*;

public final class XulorParticleSystem extends ParticleSystem
{
    public XulorParticleSystem() {
        super(false);
    }
    
    public void prepareParticlesBeforeRendering(final EntityGroup parent) {
        final double systemX = this.getX();
        final double systemY = this.getY();
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        if (this.m_geocentric) {
            deltaX += (float)systemX;
            deltaY += (float)systemY;
        }
        if (this.getNumGeometries() == 0) {
            return;
        }
        parent.addChild(this);
        for (int i = 0; i < this.getNumGeometries(); ++i) {
            final GeometryMesh geometryMesh = (GeometryMesh)this.getGeometry(i);
            geometryMesh.getVertexBuffer().clear();
        }
        this.drawParticleRecurse(this.m_root, deltaX, deltaY, 0);
        this.updateGeometry();
        this.m_previousGeometry = null;
    }
    
    private void drawParticleRecurse(final Particle particle, final float deltaX, final float deltaY, final int level) {
        if (ParticleSystem.particleBufferIsFull()) {
            return;
        }
        if (particle != this.m_root && particle.m_alpha > 0.004f) {
            this.addParticleGeometry(particle, particle.m_x + deltaX, particle.m_y + deltaY);
        }
        if (particle.m_emitters == null) {
            return;
        }
        final Emitter[] emitters = particle.m_emitters;
        for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
            final Emitter emitter = emitters[emitterIndex];
            final ParticleList particles = emitter.m_children;
            if (particles != null) {
                final int numParticles = particles.size();
                if (emitter.m_definition.m_geocentric) {
                    for (int particleIndex = 0; particleIndex < numParticles; ++particleIndex) {
                        final Particle p = particles.get(particleIndex);
                        this.drawParticleRecurse(p, particle.m_x + deltaX, particle.m_y + deltaY, level + 1);
                    }
                }
                else {
                    for (int particleIndex = 0; particleIndex < numParticles; ++particleIndex) {
                        final Particle p = particles.get(particleIndex);
                        this.drawParticleRecurse(p, deltaX, deltaY, level + 1);
                    }
                }
            }
        }
    }
    
    @Override
    public void setColorHelperProvider(final ColorHelper helper) {
        this.m_colorHelper = ImmediateColor.getInstance();
    }
}
