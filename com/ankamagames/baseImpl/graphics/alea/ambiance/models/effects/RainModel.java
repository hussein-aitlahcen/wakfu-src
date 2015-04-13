package com.ankamagames.baseImpl.graphics.alea.ambiance.models.effects;

import com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class RainModel extends AbstractEffectModel
{
    public RainModel(final int typeId) {
        super(typeId, new ModelVar[] { new ModelVar.MString("texture"), new ModelVar.MFloat("vitesse X"), new ModelVar.MFloat("mult"), new ModelVar.MFloat("add"), new ModelVar.MFloat("sharpness") });
    }
    
    @Override
    public final EffectApplyer createApplyer() {
        return new Apply(this);
    }
    
    private static class Apply extends EffectApplyer
    {
        private RainParticleEffect m_effect;
        private String m_lastTexture;
        
        private Apply(final RainModel effectBlock) {
            super(effectBlock);
            this.m_lastTexture = "<unknow>";
        }
        
        @Override
        public final void apply() {
        }
        
        @Override
        public final void stop() {
            this.m_effect.reset();
            this.m_effect.clear();
            EffectManager.getInstance().removeWorldEffect(this.m_effect);
            this.m_effect = null;
        }
        
        @Override
        public final void start() {
            this.m_effect = new RainParticleEffect();
            this.m_lastTexture = "<unknow>";
            this.m_effect.initialize(2000);
            this.m_effect.setCamera(this.m_camera);
            this.m_effect.setBoundingBox(new Box(-18.0f, 18.0f, -18.0f, 18.0f, -18.0f, 18.0f));
            this.m_effect.activate(true);
            EffectManager.getInstance().addWorldEffect(this.m_effect);
        }
    }
}
