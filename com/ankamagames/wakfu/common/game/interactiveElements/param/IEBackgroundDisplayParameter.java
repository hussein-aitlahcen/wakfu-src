package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IEBackgroundDisplayParameter extends IEParameter
{
    private final int m_backgroundDisplayId;
    
    public IEBackgroundDisplayParameter(final int paramId, final int visualId, final int backgroundDisplayId, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_backgroundDisplayId = backgroundDisplayId;
    }
    
    public int getBackgroundDisplayId() {
        return this.m_backgroundDisplayId;
    }
}
