package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IEMarketBoardParameter extends IEParameter
{
    private final int m_marketId;
    
    public IEMarketBoardParameter(final int paramId, final int visualId, final int marketId, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_marketId = marketId;
    }
    
    public int getMarketId() {
        return this.m_marketId;
    }
}
