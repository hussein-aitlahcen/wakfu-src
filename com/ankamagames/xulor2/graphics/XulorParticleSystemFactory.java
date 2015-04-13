package com.ankamagames.xulor2.graphics;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class XulorParticleSystemFactory extends ParticleSystemFactory<XulorParticleSystem>
{
    private static final XulorParticleSystemFactory m_instance;
    private static final Logger m_logger;
    
    public static XulorParticleSystemFactory getInstance() {
        return XulorParticleSystemFactory.m_instance;
    }
    
    @Override
    public final XulorParticleSystem getFreeParticleSystem(final String filename) {
        return this.getFreeParticleSystem(filename, XulorParticleSystemFactory.SYSTEM_WITHOUT_LEVEL);
    }
    
    public XulorParticleSystem getFreeParticleSystem(final String filename, final int systemLevel) {
        final XulorParticleSystem particleSystem = new XulorParticleSystem();
        try {
            this.createParticleSystem(filename, systemLevel, particleSystem);
        }
        catch (Exception e) {
            XulorParticleSystemFactory.m_logger.error((Object)("erreur de cr\u00e9ation de particule " + filename), (Throwable)e);
            return null;
        }
        return particleSystem;
    }
    
    static {
        m_instance = new XulorParticleSystemFactory();
        m_logger = Logger.getLogger((Class)XulorParticleSystemFactory.class);
    }
}
