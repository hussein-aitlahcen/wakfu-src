package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.chaos.*;

public class IEDecorationParameter extends IEParameter
{
    private final GemType[] m_allowedGemTypes;
    
    public IEDecorationParameter(final int paramId, final GemType[] allowedGemTypes) {
        super(paramId, 0, ChaosInteractiveCategory.NO_CHAOS, 0);
        this.m_allowedGemTypes = allowedGemTypes;
    }
    
    public GemType[] getAllowedGemTypes() {
        return this.m_allowedGemTypes;
    }
}
