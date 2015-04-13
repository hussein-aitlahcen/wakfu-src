package com.ankamagames.baseImpl.graphics.sound;

import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public abstract class BaseSoundManager extends SoundManager
{
    protected int m_currentCameraMaskKey;
    
    public BaseSoundManager() {
        super();
        this.m_currentCameraMaskKey = 0;
    }
    
    protected void onCameraGroupChange(final int maskKey) {
        this.m_currentCameraMaskKey = maskKey;
        for (int i = this.m_lowPassFilters.size() - 1; i >= 0; --i) {
            final LowPassFilter filter = this.m_lowPassFilters.getQuickValue(i);
            filter.setIsVisibleFromCamera(this.isVisibleFromCamera(filter.getMaskKey()));
        }
    }
    
    @Override
    protected final boolean isVisibleFromCamera(final int maskKey) {
        return GroupLayerManager.getInstance().isVisible(this.m_currentCameraMaskKey, maskKey);
    }
}
