package com.ankamagames.wakfu.client.core.game.item.referenceItem;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class MetaItemBuilder<T extends MetaItemBuilder, I extends MetaItem> extends AbstractReferenceItemBuilder<T, I>
{
    public MetaItemBuilder() {
        this(new MetaItem());
    }
    
    protected MetaItemBuilder(final I item) {
        super(item);
    }
    
    public MetaItemBuilder setActionVisual(final ItemActionVisual actionVisual) {
        this.m_item.setActionVisual(actionVisual);
        return this;
    }
    
    public MetaItemBuilder setUsageTarget(final ItemWorldUsageTarget usageTarget) {
        this.m_item.setUsageTarget(usageTarget);
        return this;
    }
    
    public MetaItemBuilder setSubs(final int[] subMetaIds) {
        this.m_item.setSubs(subMetaIds);
        return this;
    }
}
