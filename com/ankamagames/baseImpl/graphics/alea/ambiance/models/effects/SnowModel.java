package com.ankamagames.baseImpl.graphics.alea.ambiance.models.effects;

import com.ankamagames.baseImpl.graphics.alea.display.effects.particles.snow.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class SnowModel extends AbstractEffectModel
{
    public SnowModel(final int typeId) {
        super(typeId, new ModelVar[] { new ModelVar.MString("texture"), new ModelVar.MFloat("vitesse X"), new ModelVar.MFloat("mult"), new ModelVar.MFloat("add"), new ModelVar.MFloat("sharpness") });
    }
    
    @Override
    public final EffectApplyer createApplyer() {
        return new Apply(this);
    }
    
    private static class Apply extends EffectApplyer
    {
        private SnowParticleEffect m_effect;
        private String m_lastTexture;
        
        private Apply(final SnowModel model) {
            super(model);
            this.m_lastTexture = "<unknow>";
        }
        
        @Override
        public final void apply() {
        }
        
        @Override
        public final void stop() {
            EffectManager.getInstance().removeWorldEffect(this.m_effect);
            this.m_effect.reset();
            this.m_effect.clear();
        }
        
        @Override
        public final void start() {
            this.m_effect = new SnowParticleEffect();
            this.m_lastTexture = "<unknow>";
            this.m_effect.initialize(2000);
            this.m_effect.setCamera(this.m_camera);
            this.m_effect.activate(true);
            this.m_effect.setBoundingBox(new Box(-18.0f, 18.0f, -18.0f, 18.0f, -18.0f, 18.0f));
            EffectManager.getInstance().addWorldEffect(this.m_effect);
        }
    }
}
