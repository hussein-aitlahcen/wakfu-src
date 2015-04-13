package com.ankamagames.wakfu.client.core.game.item.referenceItem;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class ReferenceItemBuilder<T extends ReferenceItemBuilder, I extends ReferenceItem> extends AbstractReferenceItemBuilder<T, I>
{
    public ReferenceItemBuilder() {
        this(new ReferenceItem());
    }
    
    protected ReferenceItemBuilder(final I item) {
        super(item);
    }
    
    public T setActionVisual(final ItemActionVisual actionVisual) {
        this.m_item.setActionVisual(actionVisual);
        return (T)this;
    }
    
    public T setUsageTarget(final ItemWorldUsageTarget usageTarget) {
        this.m_item.setUsageTarget(usageTarget);
        return (T)this;
    }
}
