package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;

final class CosmeticsItemModel implements CosmeticsItem
{
    private final int m_refItem;
    
    CosmeticsItemModel(final int refId) {
        super();
        this.m_refItem = refId;
    }
    
    @Override
    public int getRefId() {
        return this.m_refItem;
    }
    
    @Override
    public String toString() {
        return "CosmeticsItemModel{m_refItem=" + this.m_refItem + '}';
    }
}
