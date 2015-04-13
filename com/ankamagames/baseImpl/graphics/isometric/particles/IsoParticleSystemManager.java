package com.ankamagames.baseImpl.graphics.isometric.particles;

import org.apache.log4j.*;
import java.util.concurrent.*;
import com.ankamagames.baseImpl.graphics.isometric.debug.particlesAndLights.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import java.util.*;

public class IsoParticleSystemManager<S extends IsoWorldScene> implements RenderProcessHandler<S>, LitScene
{
    private static final Logger m_logger;
    private static final int PARTICLE_SYSTEM_MIN_CELL_COUNT_FOR_DESTROY = 54;
    private static final IsoParticleSystemManager m_instance;
    protected MaskableElementAddedListener m_elementAddedListener;
    private final Map<Integer, IsoParticleSystem> m_particleSystems;
    private byte m_currentLod;
    private volatile boolean m_particleSystemsAllowed;
    private final ArrayList<IsoParticleSystem> m_visibleParticleSystems;
    
    public IsoParticleSystemManager() {
        super();
        this.m_particleSystems = new ConcurrentHashMap<Integer, IsoParticleSystem>();
        this.m_particleSystemsAllowed = true;
        this.m_visibleParticleSystems = new ArrayList<IsoParticleSystem>(50);
    }
    
    public static IsoParticleSystemManager getInstance() {
        return IsoParticleSystemManager.m_instance;
    }
    
    public void setParticleSystemsAllowed(final boolean particleSystemsAllowed) {
        this.m_particleSystemsAllowed = particleSystemsAllowed;
    }
    
    public boolean isParticleSystemsAllowed() {
        return this.m_particleSystemsAllowed;
    }
    
    public void addCellParticleSystem(final CellParticleSystem particleSystem) {
        if (this.containCellParticleSystem(particleSystem)) {
            return;
        }
        this.addParticleSystem(particleSystem);
    }
    
    public boolean containCellParticleSystem(final IsoParticleSystem particleSystem) {
        for (final IsoParticleSystem system : this.m_particleSystems.values()) {
            if (system.getId() == particleSystem.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public Iterator<IsoParticleSystem> particleSystemIterator() {
        return this.m_particleSystems.values().iterator();
    }
    
    public IsoParticleSystem getParticleSystem(final int id) {
        return this.m_particleSystems.get(id);
    }
    
    public void addParticleSystem(final IsoParticleSystem particleSystem) {
        if (!this.m_particleSystemsAllowed) {
            return;
        }
        assert !this.m_particleSystems.containsKey(particleSystem.getId());
        particleSystem.addReference();
        this.m_particleSystems.put(particleSystem.getId(), particleSystem);
        this.fireElementAdded(particleSystem, (int)particleSystem.getX(), (int)particleSystem.getY(), (int)particleSystem.getZ());
        ParticlesAndLightDebug.INSTANCE.addParticleSystem(particleSystem);
    }
    
    public void removeParticleSystem(final int id) {
        this.removeParticleSystem(id, false);
    }
    
    public void removeParticleSystem(final int id, final boolean stopBeforeKill) {
        final IsoParticleSystem particleSystem = this.m_particleSystems.get(id);
        if (particleSystem == null) {
            return;
        }
        if (stopBeforeKill) {
            particleSystem.stopAndKill();
        }
        else {
            particleSystem.kill();
        }
    }
    
    public void setParticleVisible(final int id, final boolean visible) {
        final IsoParticleSystem particleSystem = this.m_particleSystems.get(id);
        if (particleSystem != null) {
            particleSystem.setVisible(visible);
        }
    }
    
    public void clearParticleSystems() {
        for (final IsoParticleSystem isoParticleSystem : this.m_particleSystems.values()) {
            isoParticleSystem.immediateKill();
        }
        if (!this.m_particleSystemsAllowed) {
            return;
        }
        final Iterator<IsoParticleSystem> it = this.m_particleSystems.values().iterator();
        while (it.hasNext()) {
            final IsoParticleSystem system = it.next();
            if (system.getNumReferences() <= 0) {
                system.removeReference();
                it.remove();
            }
        }
    }
    
    @Override
    public void process(final S scene, final int deltaTime) {
        if (!this.m_particleSystemsAllowed) {
            return;
        }
        float timeIncrement = deltaTime / 1000.0f;
        if (timeIncrement > 0.066f) {
            timeIncrement = 0.066f;
        }
        for (final IsoParticleSystem isoParticleSystem : this.m_particleSystems.values()) {
            if (isoParticleSystem.getNumReferences() == 0) {
                continue;
            }
            if (isoParticleSystem.getLod() > this.m_currentLod) {
                continue;
            }
            isoParticleSystem.update(timeIncrement);
        }
    }
    
    @Override
    public void prepareBeforeRendering(final S scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        if (!this.m_particleSystemsAllowed) {
            return;
        }
        this.m_visibleParticleSystems.clear();
        final Iterator<IsoParticleSystem> it = this.m_particleSystems.values().iterator();
        final int screenIsoWorldX = (int)centerScreenIsoWorldX;
        final int screenIsoWorldY = (int)centerScreenIsoWorldY;
        final Rect screenBounds = scene.getIsoCamera().getScreenBounds();
        while (it.hasNext()) {
            final IsoParticleSystem system = it.next();
            if (system.getNumReferences() <= 0) {
                system.removeReference();
                it.remove();
            }
            else {
                if (system.getLod() > this.m_currentLod) {
                    continue;
                }
                boolean maskSetted = true;
                switch (this.isVisible(system, scene, screenBounds, screenIsoWorldX, screenIsoWorldY)) {
                    case ON_SCREEN: {
                        if (system.isMaskUndefined()) {
                            maskSetted = system.computeZOrderAndMaskKey(scene);
                        }
                        this.m_visibleParticleSystems.add(system);
                        system.prepareParticlesBeforeRendering(scene);
                        break;
                    }
                    case NO: {
                        try {
                            system.stopAndKill(1);
                        }
                        catch (Exception e) {
                            while (system.getNumReferences() >= 0) {
                                system.removeReference();
                            }
                            it.remove();
                            IsoParticleSystemManager.m_logger.error((Object)"probl\u00e8me lors de l'arr\u00eat d'un syst\u00e8me de particule", (Throwable)e);
                        }
                        break;
                    }
                }
                if (maskSetted) {
                    continue;
                }
                MaskableHelper.setUndefined(system);
            }
        }
    }
    
    private VISIBILITY isVisible(final IsoParticleSystem system, final S scene, final Rect screenBounds, final int centerScreenIsoWorldX, final int centerScreenIsoWorldY) {
        final float distanceX = system.getX() - centerScreenIsoWorldX;
        final float distanceY = system.getY() - centerScreenIsoWorldY;
        if (Math.abs(distanceX) > 54.0f || Math.abs(distanceY) > 54.0f) {
            return system.isRemovedWhenFar() ? VISIBILITY.NO : VISIBILITY.NO_BUT_PROCESS;
        }
        if (!system.isVisible()) {
            return VISIBILITY.NO_BUT_PROCESS;
        }
        final Rect bounds = new Rect(system.getBounds());
        final Point2 pt = IsoCameraFunc.getScreenPosition(scene, system.getX(), system.getY(), system.getZ());
        bounds.translate((int)pt.m_x, (int)pt.m_y);
        if (screenBounds.containsOrIntersect(bounds)) {
            return VISIBILITY.ON_SCREEN;
        }
        return VISIBILITY.NO_BUT_PROCESS;
    }
    
    public void removeParticleSytemsOnCell(final int cellX, final int cellY) {
        for (final IsoParticleSystem system : this.m_particleSystems.values()) {
            if ((int)system.getX() == cellX && (int)system.getY() == cellY) {
                system.kill();
            }
        }
    }
    
    private void fireElementAdded(final IsoParticleSystem particleSystem, final int x, final int y, final int z) {
        MaskableHelper.setUndefined(particleSystem);
    }
    
    public void clearParticlesOnTarget(final IsoWorldTarget target) {
        for (final IsoParticleSystem system : this.m_particleSystems.values()) {
            if (system instanceof FreeParticleSystem) {
                final FreeParticleSystem isoSystem = (FreeParticleSystem)system;
                if (isoSystem.getTarget() != target) {
                    continue;
                }
                this.removeParticleSystem(system.getId());
            }
        }
    }
    
    public void setCurrentLod(final byte currentLod) {
        this.m_currentLod = currentLod;
    }
    
    public int countParticle() {
        int count = 0;
        for (final IsoParticleSystem system : this.m_particleSystems.values()) {
            count += system.totalParticleCount();
        }
        return count;
    }
    
    @Override
    public void queryObjects(final AbstractCamera camera, final ArrayList<LitSceneObject> objects) {
        objects.addAll(this.m_visibleParticleSystems);
    }
    
    static {
        m_logger = Logger.getLogger((Class)IsoParticleSystemManager.class);
        m_instance = new IsoParticleSystemManager();
    }
    
    private enum VISIBILITY
    {
        ON_SCREEN, 
        NO_BUT_PROCESS, 
        NO;
    }
}
