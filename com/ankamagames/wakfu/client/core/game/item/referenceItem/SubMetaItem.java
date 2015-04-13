package com.ankamagames.wakfu.client.core.game.item.referenceItem;

import com.ankamagames.wakfu.common.game.item.*;

public class SubMetaItem extends ReferenceItem
{
    private MetaItem m_metaParent;
    
    void setMetaParent(final MetaItem metaParent) {
        this.m_metaParent = metaParent;
    }
    
    @Override
    public ItemMetaType getMetaType() {
        return ItemMetaType.SUB_META_ITEM;
    }
    
    @Override
    public String getName() {
        return this.m_metaParent.getName();
    }
    
    @Override
    public String getDescription() {
        return this.m_metaParent.getDescription();
    }
    
    public MetaItem getMetaParent() {
        return this.m_metaParent;
    }
    
    public boolean isPerfectItem() {
        return this.m_metaParent.getLastSubId() == this.getId();
    }
}
