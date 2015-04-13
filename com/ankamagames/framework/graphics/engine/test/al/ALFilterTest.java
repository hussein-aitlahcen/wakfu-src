package com.ankamagames.framework.graphics.engine.test.al;

import org.lwjgl.openal.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.graphics.engine.test.*;

public class ALFilterTest extends AbstractALFeatureTest
{
    private int[] m_filterId;
    
    public ALFilterTest() {
        super();
        this.m_filterId = new int[] { 0 };
    }
    
    @Override
    public boolean runTest() {
        if (!ALC10.alcIsExtensionPresent(ALC10.alcGetContextsDevice(ALC10.alcGetCurrentContext()), "ALC_EXT_EFX")) {
            return false;
        }
        try {
            this.m_filterId[0] = EFX10.alGenFilters();
            final SoundManager manager = this.m_manager;
            SoundManager.check();
            EFX10.alFilteri(this.m_filterId[0], 32769, 1);
            final SoundManager manager2 = this.m_manager;
            SoundManager.check();
        }
        catch (Exception e) {
            SoundManager.getInstance().setAreFiltersSupported(false);
            return false;
        }
        SoundManager.getInstance().setAreFiltersSupported(true);
        return true;
    }
    
    @Override
    public void cleanUp() {
        if (this.m_filterId[0] != 0) {
            final SoundManager manager = this.m_manager;
            SoundManager.check();
            EFX10.alDeleteFilters(this.m_filterId[0]);
            final SoundManager manager2 = this.m_manager;
            SoundManager.check();
            this.m_filterId[0] = 0;
        }
        this.m_filterId = null;
        super.cleanUp();
    }
    
    @Override
    public HardwareFeature getFeature() {
        return HardwareFeature.AL_FILTER;
    }
}
