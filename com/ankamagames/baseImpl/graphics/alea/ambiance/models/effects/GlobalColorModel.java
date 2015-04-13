package com.ankamagames.baseImpl.graphics.alea.ambiance.models.effects;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.graphics.engine.*;

public class GlobalColorModel extends AbstractEffectModel
{
    public GlobalColorModel(final int typeId) {
        super(typeId, new ModelVar[] { new ModelVar.MFloat("r"), new ModelVar.MFloat("g"), new ModelVar.MFloat("b") });
    }
    
    @Override
    public EffectApplyer createApplyer() {
        return new Apply(this);
    }
    
    @Override
    public void reset(final Var[] inputs) {
        for (int i = 0; i < inputs.length; ++i) {
            inputs[i].setFloat(1.0f);
        }
    }
    
    private static class Apply extends EffectApplyer implements LitSceneModifier
    {
        private Apply(final GlobalColorModel model) {
            super(model);
        }
        
        @Override
        public final void update(final int deltaTime) {
        }
        
        @Override
        public final void apply() {
        }
        
        @Override
        public final void stop() {
            IsoSceneLightManager.INSTANCE.removeLightingModifier(this);
        }
        
        @Override
        public final void start() {
            IsoSceneLightManager.INSTANCE.addLightingModifier(this);
        }
        
        @Override
        public boolean useless() {
            final float r = this.m_inputs[0].floatValue();
            final float g = this.m_inputs[1].floatValue();
            final float b = this.m_inputs[2].floatValue();
            return Engine.isEqualColor(r, 1.0f) && Engine.isEqualColor(g, 1.0f) && Engine.isEqualColor(b, 1.0f);
        }
        
        @Override
        public int getPriority() {
            return 0;
        }
        
        @Override
        public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
            final float r = this.m_inputs[0].floatValue();
            final float g = this.m_inputs[1].floatValue();
            final float b = this.m_inputs[2].floatValue();
            final int n = 0;
            colors[n] *= r;
            final int n2 = 1;
            colors[n2] *= g;
            final int n3 = 2;
            colors[n3] *= b;
        }
    }
}
