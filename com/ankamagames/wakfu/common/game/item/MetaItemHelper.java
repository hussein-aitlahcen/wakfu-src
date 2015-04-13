package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class MetaItemHelper
{
    public static final MetaItemHelper INSTANCE;
    public final ReferenceItemManager REFERENCE_MANAGER;
    public final MetaItemManager META_MANAGER;
    
    public MetaItemHelper() {
        super();
        this.REFERENCE_MANAGER = ReferenceItemManager.getInstance();
        this.META_MANAGER = MetaItemManager.INSTANCE;
    }
    
    public void addToAppropriateManager(final AbstractReferenceItem item) {
        switch (item.getMetaType()) {
            case META_ITEM: {
                this.META_MANAGER.add((IMetaItem)item);
                break;
            }
            case REFERENCE_ITEM:
            case SUB_META_ITEM: {
                this.REFERENCE_MANAGER.addReferenceItem(item);
                break;
            }
        }
    }
    
    static {
        INSTANCE = new MetaItemHelper();
    }
}
