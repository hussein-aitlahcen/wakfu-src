package com.ankamagames.framework.graphics.engine.test.al;

import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.sound.openAL.*;

public abstract class AbstractALFeatureTest implements HardwareFeatureTest
{
    protected SoundManager m_manager;
    
    @Override
    public void initialize() {
        this.m_manager = SoundManager.getInstance();
    }
    
    @Override
    public void cleanUp() {
        this.m_manager = null;
    }
}
