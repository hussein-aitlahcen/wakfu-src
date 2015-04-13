package com.ankamagames.wakfu.client.core.game.interactiveElement.param;

import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.chaos.*;

public class ClientIECraftParameter extends IECraftParameter
{
    private final int m_apsId;
    
    public ClientIECraftParameter(final int paramId, final int visualId, final int craftId, final int[] allowedRecipes, final int apsId, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, craftId, allowedRecipes, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_apsId = apsId;
    }
    
    public int getApsId() {
        return this.m_apsId;
    }
}
