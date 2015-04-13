package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.graphics.engine.*;

public class SunEffect extends Effect
{
    private FreeParticleSystem m_system;
    private int m_lastLevel;
    
    public SunEffect() {
        super();
        this.m_lastLevel = Integer.MAX_VALUE;
    }
    
    @Override
    public void stop(final int timeBeforeKill) {
        super.stop(timeBeforeKill);
        this.removeParticle();
    }
    
    @Override
    public void clear() {
        this.removeParticle();
    }
    
    @Override
    public void reset() {
        super.reset();
        this.removeParticle();
    }
    
    private void removeParticle() {
        if (this.m_system != null) {
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_system.getId());
            this.m_system = null;
        }
        this.m_lastLevel = Integer.MAX_VALUE;
    }
    
    @Override
    public void update(final int timeIncrement) {
        super.update(timeIncrement);
        final int level = MathHelper.clamp(Math.round(this.getStrength() * 100.0f), 0, 100);
        if (this.m_lastLevel != level) {
            this.removeParticle();
            this.createSystem(this.m_lastLevel = level);
        }
        if (this.m_system != null) {
            this.m_system.setFightId(WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFightId());
            this.m_system.setVisible(WeatherEffectsVisibilityManager.INSTANCE.isWeatherEffectsVisible());
        }
    }
    
    private void createSystem(final int level) {
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(900030, level);
        final AbstractCamera cam = WakfuClientInstance.getInstance().getWorldScene().getCam();
        system.setTarget(cam.getIsoTarget());
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
        this.m_system = system;
    }
    
    @Override
    public void render(final Renderer renderer) {
    }
    
    @Override
    public String toString() {
        return "SunEffect{m_system=" + this.m_system + ", m_lastLevel=" + this.m_lastLevel + '}';
    }
}
