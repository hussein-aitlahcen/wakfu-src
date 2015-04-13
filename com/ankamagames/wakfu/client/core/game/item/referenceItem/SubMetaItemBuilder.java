package com.ankamagames.wakfu.client.core.game.item.referenceItem;

public class SubMetaItemBuilder extends ReferenceItemBuilder<SubMetaItemBuilder, SubMetaItem>
{
    public SubMetaItemBuilder() {
        super(new SubMetaItem());
    }
    
    public SubMetaItemBuilder setMetaParent(final MetaItem item) {
        ((SubMetaItem)this.m_item).setMetaParent(item);
        return this;
    }
    
    public SubMetaItemBuilder setMetaId(final int metaId) {
        ((SubMetaItem)this.m_item).setMetaId(metaId);
        return this;
    }
}
