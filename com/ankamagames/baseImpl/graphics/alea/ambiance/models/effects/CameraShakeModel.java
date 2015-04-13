package com.ankamagames.baseImpl.graphics.alea.ambiance.models.effects;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.effects.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public class CameraShakeModel extends AbstractEffectModel
{
    public CameraShakeModel(final int typeId) {
        super(typeId, new ModelVar[] { new ModelVar.MFloat("amplitude"), new ModelVar.MFloat("p\u00e9riode") });
    }
    
    @Override
    public EffectApplyer createApplyer() {
        return new Apply(this);
    }
    
    private static class Apply extends EffectApplyer
    {
        private CameraEffectShake m_effect;
        
        private Apply(final CameraShakeModel model) {
            super(model);
        }
        
        @Override
        public void combine(final ArrayList<EffectBlock> currents) {
            super.combine(currents);
        }
        
        @Override
        public final void apply() {
            final float amplitude = this.m_inputs[0].floatValue();
            final float period = this.m_inputs[1].floatValue();
            if (amplitude == 0.0f || period == 0.0f) {
                this.stop();
                return;
            }
            if (this.m_effect == null) {
                this.start();
            }
            this.m_effect.setCamera(this.m_camera);
            this.m_effect.setParams(period, amplitude);
        }
        
        @Override
        public final void stop() {
            if (this.m_effect == null) {
                return;
            }
            this.m_effect.reset();
            this.m_effect.clear();
            EffectManager.getInstance().removeWorldEffect(this.m_effect);
            this.m_effect = null;
        }
        
        @Override
        public final void start() {
            (this.m_effect = new CameraEffectShake()).start();
            EffectManager.getInstance().addWorldEffect(this.m_effect);
        }
    }
}
