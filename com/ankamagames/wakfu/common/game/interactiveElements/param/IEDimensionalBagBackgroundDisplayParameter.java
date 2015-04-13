package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.chaos.*;

public class IEDimensionalBagBackgroundDisplayParameter extends IEParameter
{
    private final GemType[] m_allowedGemTypes;
    private final int m_backgroundDisplayId;
    
    public IEDimensionalBagBackgroundDisplayParameter(final int paramId, final GemType[] allowedGemTypes, final int backgroundDisplayId) {
        super(paramId, 0, ChaosInteractiveCategory.NO_CHAOS, 0);
        this.m_allowedGemTypes = allowedGemTypes;
        this.m_backgroundDisplayId = backgroundDisplayId;
    }
    
    public GemType[] getAllowedGemTypes() {
        return this.m_allowedGemTypes;
    }
    
    public int getBackgroundDisplayId() {
        return this.m_backgroundDisplayId;
    }
}
