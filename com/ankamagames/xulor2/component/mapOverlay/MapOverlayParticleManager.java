package com.ankamagames.xulor2.component.mapOverlay;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import java.util.*;

public class MapOverlayParticleManager
{
    private static final Logger m_logger;
    private final HashMap<DisplayableMapPoint, XulorParticleSystem> m_particles;
    
    public MapOverlayParticleManager() {
        super();
        this.m_particles = new HashMap<DisplayableMapPoint, XulorParticleSystem>();
    }
    
    @Nullable
    public XulorParticleSystem getParticleSystem(final DisplayableMapPoint point) {
        final String particlePath = point.getParticlePath();
        if (particlePath == null) {
            return null;
        }
        final XulorParticleSystem particleSystem = this.m_particles.get(point);
        if (particleSystem != null) {
            return particleSystem;
        }
        return this.createSystem(point, particlePath);
    }
    
    @Nullable
    private XulorParticleSystem createSystem(final DisplayableMapPoint point, final String particlePath) {
        final XulorParticleSystem system = XulorParticleSystemFactory.getInstance().getFreeParticleSystem(particlePath);
        if (system == null) {
            return null;
        }
        XulorParticleSystemManager.INSTANCE.addParticleSystem(system);
        this.m_particles.put(point, system);
        system.addReference();
        return system;
    }
    
    public void clearParticleLinkedTo(final MapOverlayPointList points) {
        for (int i = 0, size = points.size(); i < size; ++i) {
            final DisplayableMapPoint item = points.getPoint(i);
            if (item.getParticlePath() != null) {
                final XulorParticleSystem particleSystem = this.m_particles.remove(item);
                if (particleSystem != null) {
                    particleSystem.removeReference();
                }
            }
        }
    }
    
    public void clear() {
        for (final XulorParticleSystem ps : this.m_particles.values()) {
            ps.removeReference();
        }
        this.m_particles.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapOverlayParticleManager.class);
    }
}
