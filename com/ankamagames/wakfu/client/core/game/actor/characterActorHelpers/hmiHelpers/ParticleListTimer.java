package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

final class ParticleListTimer
{
    private static final int PARTICLE_DISPLAY_DELAY = 3000;
    private final ParticlesList m_particlesList;
    private Runnable m_process;
    private boolean m_isRunning;
    
    ParticleListTimer(final ParticlesList particlesList) {
        super();
        this.m_particlesList = particlesList;
    }
    
    public void startParticleDisplay() {
        this.m_isRunning = true;
        this.m_process = new Runnable() {
            @Override
            public void run() {
                ParticleListTimer.this.m_particlesList.stopCurrentAndStartNext();
            }
        };
        ProcessScheduler.getInstance().schedule(this.m_process, 3000L);
    }
    
    public void stopParticleDisplay() {
        ProcessScheduler.getInstance().remove(this.m_process);
        this.m_isRunning = false;
    }
    
    public boolean isRunning() {
        return this.m_isRunning;
    }
}
