package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public abstract class AbstractQueueCollectAction extends AbstractLawMRUAction implements QueueCollectAction
{
    private FreeParticleSystem m_particle;
    protected MobileEndPathListener m_pathListener;
    
    @Override
    public void addParticle() {
        final AnimatedElementWithDirection ressource = this.getCollectedRessource();
        ressource.addVisibleChangedListener(this);
        (this.m_particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800255, 0)).setTarget(ressource, 1.0f, 2);
        IsoParticleSystemManager.getInstance().addParticleSystem(this.m_particle);
    }
    
    @Override
    public void removeParticle() {
        final AnimatedElementWithDirection ressource = this.getCollectedRessource();
        ressource.removeVisibleChangedListener(this);
        IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_particle.getId());
    }
    
    @Override
    public void onVisibleChanged(final boolean visible, final VisibleChangedListener.VisibleChangedCause cause) {
        this.m_particle.setVisible(visible);
    }
}
