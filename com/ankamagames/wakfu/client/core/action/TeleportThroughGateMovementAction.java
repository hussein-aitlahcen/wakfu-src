package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public final class TeleportThroughGateMovementAction extends TeleportForMovementAction
{
    public TeleportThroughGateMovementAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long fighterId, final Point3 position) {
        super(uniqueId, actionType, actionId, fightId, fighterId, position, "AnimSort-Portail-Saut-Debut", "AnimSort-Portail-Saut-Fin");
    }
    
    private void playAps(final IsoWorldTarget target) {
        final FreeParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(1018808);
        particleSystem.setWorldPosition(target.getWorldX(), target.getWorldY(), target.getAltitude());
        particleSystem.setFightId(this.getFightId());
        IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
    }
    
    @Override
    protected void onStartAnim(final CharacterActor actor) {
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                TeleportThroughGateMovementAction.this.playAps(actor);
            }
        }, 500L, 1);
    }
    
    @Override
    protected void onEndAnim(final CharacterInfo fighter) {
        this.playAps(fighter.m_actor);
    }
}
