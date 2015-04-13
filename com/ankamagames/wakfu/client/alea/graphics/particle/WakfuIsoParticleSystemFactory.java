package com.ankamagames.wakfu.client.alea.graphics.particle;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import java.io.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public class WakfuIsoParticleSystemFactory extends IsoParticleSystemFactory
{
    @Override
    public WakfuFreeParticleSystem getFreeParticleSystem(final String filename, final int systemLevel) {
        final WakfuFreeParticleSystem particleSystem = new WakfuFreeParticleSystem(false);
        try {
            this.createParticleSystem(filename, systemLevel, particleSystem);
        }
        catch (FileNotFoundException e2) {
            WakfuIsoParticleSystemFactory.m_logger.error((Object)("FICHIER APS MANQUANT : " + filename));
        }
        catch (Exception e) {
            WakfuIsoParticleSystemFactory.m_logger.error((Object)"Erreur \u00e0 la cr\u00e9ation d'une particule : ", (Throwable)e);
        }
        return particleSystem;
    }
    
    @Override
    public boolean canCreateParticleForFight(final int fightId) {
        return fightId == -1 || FightVisibilityManager.getInstance().isParticuleVisibleForFight(fightId);
    }
}
