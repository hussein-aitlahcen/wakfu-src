package com.ankamagames.baseImpl.graphics.isometric.particles;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class IsoParticleSystemFactory extends ParticleSystemFactory<IsoParticleSystem>
{
    protected static final Logger m_logger;
    private static IsoParticleSystemFactory m_instance;
    
    public static IsoParticleSystemFactory getInstance() {
        return IsoParticleSystemFactory.m_instance;
    }
    
    public static void setInstance(final IsoParticleSystemFactory instance) {
        IsoParticleSystemFactory.m_instance = instance;
    }
    
    public final FreeParticleSystem getFreeParticleSystem(final int particleSystemId) {
        return this.getFreeParticleSystem(this.createFileName(particleSystemId));
    }
    
    public final FreeParticleSystem getFreeParticleSystem(final int particleSystemId, final int systemLevel) {
        return this.getFreeParticleSystem(this.createFileName(particleSystemId), systemLevel);
    }
    
    @Override
    public final FreeParticleSystem getFreeParticleSystem(final String filename) {
        return this.getFreeParticleSystem(filename, IsoParticleSystemFactory.SYSTEM_WITHOUT_LEVEL);
    }
    
    public FreeParticleSystem getFreeParticleSystem(final String filename, final int systemLevel) {
        return this.getFreeParticleSystem(filename, systemLevel, false);
    }
    
    public FreeParticleSystem getFreeParticleSystem(final String filename, final int systemLevel, final boolean editable) {
        final FreeParticleSystem particleSystem = new FreeParticleSystem(editable);
        try {
            this.createParticleSystem(filename, systemLevel, particleSystem);
        }
        catch (Exception e) {
            IsoParticleSystemFactory.m_logger.error((Object)("particle " + filename), (Throwable)e);
        }
        return particleSystem;
    }
    
    public final CellParticleSystem getCellParticleSystem(final int particleSystemId) {
        return this.getCellParticleSystem(particleSystemId, IsoParticleSystemFactory.SYSTEM_WITHOUT_LEVEL);
    }
    
    public CellParticleSystem getCellParticleSystem(final int particleSystemId, final int particleSystemLevel) {
        try {
            final CellParticleSystem particleSystem = this.createCellParticleSystem();
            this.createParticleSystem(particleSystemId, particleSystemLevel, particleSystem);
            if (particleSystem.getDuration() != 0) {
                IsoParticleSystemFactory.m_logger.error((Object)("Le systeme de particule attach\u00e9 \u00e0 la cellule " + particleSystem.getX() + "/" + particleSystem.getY() + " poss\u00e8de une dur\u00e9e."));
            }
            return particleSystem;
        }
        catch (Exception e) {
            IsoParticleSystemFactory.m_logger.error((Object)("Impossible de charger le syst\u00e8me de particule : " + particleSystemId + " " + this.createFileName(particleSystemId)), (Throwable)e);
            return null;
        }
    }
    
    protected CellParticleSystem createCellParticleSystem() {
        return new CellParticleSystem(false);
    }
    
    public boolean canCreateParticleForFight(final int fightId) {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)IsoParticleSystemFactory.class);
        IsoParticleSystemFactory.m_instance = new IsoParticleSystemFactory();
    }
}
