package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.common.game.craft.reference.*;

public class UnknownCraftView extends AbstractCraftView
{
    private final boolean m_hasRecipes;
    private final boolean m_hasHarvests;
    
    public UnknownCraftView(final int refCraftId) {
        super(refCraftId);
        this.m_hasHarvests = CraftHarvestElementManager.INSTANCE.hasElements(refCraftId);
        this.m_hasRecipes = (CraftManager.INSTANCE.getCraft(this.m_refCraftId).getNumRecipes() != 0);
    }
    
    @Override
    public short getLevel() {
        return 0;
    }
    
    @Override
    public boolean isUnknown() {
        return true;
    }
    
    @Override
    public boolean hasRecipes() {
        return this.m_hasRecipes;
    }
    
    @Override
    public boolean hasHarvests() {
        return this.m_hasHarvests;
    }
}
