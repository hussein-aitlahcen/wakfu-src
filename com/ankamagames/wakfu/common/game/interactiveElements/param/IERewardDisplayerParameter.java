package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IERewardDisplayerParameter extends IEParameter
{
    private final int[] m_itemIds;
    
    public IERewardDisplayerParameter(final int paramId, final int visualId, final int[] itemIds, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_itemIds = itemIds;
    }
    
    public int[] getItemIds() {
        return this.m_itemIds;
    }
}
