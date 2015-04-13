package com.ankamagames.baseImpl.graphics.alea.ambiance.models.effects;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;

public class MapLightingModifierModel extends AbstractEffectModel
{
    public MapLightingModifierModel(final int typeId) {
        super(typeId, new ModelVar[] { new ModelVar.MFloat("ombre"), new ModelVar.MFloat("lumi\u00e8re nocturne") });
    }
    
    @Override
    public EffectApplyer createApplyer() {
        return new Apply(this);
    }
    
    private static class Apply extends EffectApplyer
    {
        private Apply(final MapLightingModifierModel model) {
            super(model);
        }
        
        @Override
        public final void apply() {
            final float shadowIntensity = this.m_inputs[0].floatValue();
            final float nightLightIntensity = this.m_inputs[1].floatValue();
            IsoSceneLightManager.INSTANCE.setLightIntensityFactor(shadowIntensity, nightLightIntensity);
        }
        
        @Override
        public final void stop() {
            IsoSceneLightManager.INSTANCE.setLightIntensityFactor(1.0f, 0.0f);
        }
        
        @Override
        public final void start() {
        }
    }
}
