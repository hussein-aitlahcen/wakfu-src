package com.ankamagames.baseImpl.graphics.alea.ambiance.models.effects;

import com.ankamagames.baseImpl.graphics.alea.display.effects.cloud.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class CloudModel extends AbstractEffectModel
{
    public CloudModel(final int typeId) {
        super(typeId, new ModelVar[] { new ModelVar.MString("texture"), new ModelVar.MFloat("vitesse X"), new ModelVar.MFloat("mult"), new ModelVar.MFloat("add"), new ModelVar.MFloat("sharpness") });
    }
    
    @Override
    public EffectApplyer createApplyer() {
        return new Apply(this);
    }
    
    private static class Apply extends EffectApplyer
    {
        private CloudModifier m_effect;
        private String m_lastTexture;
        private IsoCamera m_camera;
        
        private Apply(final CloudModel effectBlock) {
            super(effectBlock);
        }
        
        @Override
        public final void apply() {
            final String textureName = this.m_inputs[0].stringValue();
            if (!textureName.equals(this.m_lastTexture)) {
                this.m_effect.setTexture(textureName);
                this.m_lastTexture = textureName;
            }
            final float velocityX = this.m_inputs[1].floatValue();
            final float velocityY = this.m_inputs[2].floatValue();
            final float noiseBias = this.m_inputs[3].floatValue();
            final float noiseScale = this.m_inputs[4].floatValue();
            final float sharpness = this.m_inputs[5].floatValue();
            this.m_effect.setDirection(velocityX, velocityY);
            this.m_effect.setNoiseBias(noiseBias);
            this.m_effect.setNoiseScale(noiseScale);
            this.m_effect.setSharpness(sharpness);
        }
        
        @Override
        public final void stop() {
            IsoSceneLightManager.INSTANCE.removeLightingModifier(this.m_effect);
            this.m_effect = null;
            this.m_lastTexture = null;
        }
        
        @Override
        public final void start() {
            (this.m_effect = new CloudModifier()).setCamera(this.m_camera);
            IsoSceneLightManager.INSTANCE.addLightingModifier(this.m_effect);
        }
        
        @Override
        public void init(final AleaWorldScene scene) {
            super.init(scene);
            this.m_camera = scene.getIsoCamera();
        }
    }
}
