package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IEAudioMarkerParameter extends IEParameter
{
    private final int m_audioMarkerTypeId;
    private final boolean m_isLocalized;
    
    public IEAudioMarkerParameter(final int paramId, final int audioMarkerTypeId, final boolean isLocalized) {
        super(paramId, 0, ChaosInteractiveCategory.NO_CHAOS, 0);
        this.m_audioMarkerTypeId = audioMarkerTypeId;
        this.m_isLocalized = isLocalized;
    }
    
    public int getAudioMarkerTypeId() {
        return this.m_audioMarkerTypeId;
    }
    
    public boolean isLocalized() {
        return this.m_isLocalized;
    }
}
