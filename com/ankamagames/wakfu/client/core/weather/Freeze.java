package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.alea.ambiance.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.shaders.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;

class Freeze extends WeatherEffect
{
    private int m_freezeEffectId;
    
    @Override
    void start(final IsoWorldScene scene) {
        super.start(scene);
        this.m_isRunning = true;
    }
    
    private void startFreeze() {
        if (this.m_freezeEffectId != 0) {
            return;
        }
        final ShaderEffect effect = new ShaderEffect(Shaders.getShaderPath() + "freeze.cgfx", "freeze");
        Shaders.start(effect, new StrengthHandler(0.0f) {
            @Override
            public float getStrength() {
                return Freeze.this.m_strength.getCurrent();
            }
        });
        this.m_freezeEffectId = effect.getId();
    }
    
    private void stopFreeze() {
        if (this.m_freezeEffectId == 0) {
            return;
        }
        Shaders.removeEffect(this.m_freezeEffectId);
        this.m_freezeEffectId = 0;
    }
    
    @Override
    void stop() {
        super.stop();
        this.stopFreeze();
        this.m_isRunning = false;
    }
    
    @Override
    void update(final IsoWorldScene scene, final float windDirection, final float windStrength) {
        if (!this.m_isRunning) {
            return;
        }
        final float strength = this.m_strength.getCurrent();
        if (strength == 0.0f) {
            this.stop();
            return;
        }
        this.startFreeze();
    }
}
