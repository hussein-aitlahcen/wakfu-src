package com.ankamagames.wakfu.common.game.interactiveElements.param;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.chaos.*;

public class IECraftParameter extends IEParameter
{
    private final int m_craftId;
    private final TIntHashSet m_allowedRecipes;
    
    public IECraftParameter(final int paramId, final int visualId, final int craftId, final int[] allowedRecipes, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_craftId = craftId;
        this.m_allowedRecipes = new TIntHashSet(allowedRecipes);
    }
    
    public int getCraftId() {
        return this.m_craftId;
    }
    
    public TIntHashSet getAllowedRecipes() {
        return this.m_allowedRecipes;
    }
}
