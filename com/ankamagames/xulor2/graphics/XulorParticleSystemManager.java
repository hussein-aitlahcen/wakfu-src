package com.ankamagames.xulor2.graphics;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.baseImpl.graphics.isometric.debug.particlesAndLights.*;
import java.util.*;

public class XulorParticleSystemManager
{
    public static final XulorParticleSystemManager INSTANCE;
    private final LinkedList<ParticleSystem> m_particleSystems;
    private volatile boolean m_particleSystemsAllowed;
    
    private XulorParticleSystemManager() {
        super();
        this.m_particleSystemsAllowed = true;
        this.m_particleSystems = new LinkedList<ParticleSystem>();
    }
    
    public void setParticleSystemsAllowed(final boolean particleSystemsAllowed) {
        this.m_particleSystemsAllowed = particleSystemsAllowed;
    }
    
    public boolean isParticleSystemsAllowed() {
        return this.m_particleSystemsAllowed;
    }
    
    public void addParticleSystem(final ParticleSystem particleSystem) {
        this.m_particleSystems.add(particleSystem);
        ParticlesAndLightDebug.INSTANCE.addParticleSystem(particleSystem);
    }
    
    public void process(final int deltaTime) {
        if (!this.m_particleSystemsAllowed) {
            return;
        }
        final float delay = deltaTime / 1000.0f;
        final Iterator<ParticleSystem> it = this.m_particleSystems.iterator();
        while (it.hasNext()) {
            final ParticleSystem particleSystem = it.next();
            if (particleSystem.getNumReferences() < 0) {
                ParticlesAndLightDebug.INSTANCE.removeParticleSystem(particleSystem);
                it.remove();
            }
            else {
                particleSystem.update(delay);
            }
        }
    }
    
    static {
        INSTANCE = new XulorParticleSystemManager();
    }
}
