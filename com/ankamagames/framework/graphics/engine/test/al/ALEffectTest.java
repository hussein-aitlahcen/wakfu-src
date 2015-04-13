package com.ankamagames.framework.graphics.engine.test.al;

import org.lwjgl.openal.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.graphics.engine.test.*;

public class ALEffectTest extends AbstractALFeatureTest
{
    private int[] m_effects;
    private int[] m_effectSlots;
    
    public ALEffectTest() {
        super();
        this.m_effects = new int[] { 0 };
        this.m_effectSlots = new int[] { 0 };
    }
    
    @Override
    public boolean runTest() {
        if (!ALC10.alcIsExtensionPresent(ALC10.alcGetContextsDevice(ALC10.alcGetCurrentContext()), "ALC_EXT_EFX")) {
            return false;
        }
        try {
            this.m_effectSlots[0] = EFX10.alGenAuxiliaryEffectSlots();
            final SoundManager manager = this.m_manager;
            SoundManager.check();
            this.m_effects[0] = EFX10.alGenEffects();
            final SoundManager manager2 = this.m_manager;
            SoundManager.check();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    @Override
    public void cleanUp() {
        if (this.m_effectSlots[0] != 0) {
            EFX10.alDeleteAuxiliaryEffectSlots(this.m_effectSlots[0]);
            final SoundManager manager = this.m_manager;
            SoundManager.check();
            this.m_effectSlots[0] = 0;
        }
        if (this.m_effects[0] != 0) {
            EFX10.alDeleteEffects(this.m_effects[0]);
            final SoundManager manager2 = this.m_manager;
            SoundManager.check();
            this.m_effects[0] = 0;
        }
        this.m_effects = null;
        this.m_effectSlots = null;
        super.cleanUp();
    }
    
    @Override
    public HardwareFeature getFeature() {
        return HardwareFeature.AL_EFFECT;
    }
}
