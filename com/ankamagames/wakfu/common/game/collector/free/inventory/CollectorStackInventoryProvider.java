package com.ankamagames.wakfu.common.game.collector.free.inventory;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class CollectorStackInventoryProvider implements InventoryContentProvider<Item, RawInventoryItem>
{
    public static final CollectorStackInventoryProvider INSTANCE;
    
    @Override
    public Item unSerializeContent(final RawInventoryItem rawItem) {
        return ReferenceItemManager.getInstance().unSerializeContent(rawItem);
    }
    
    public RawInventoryItem serializeContent(final Item item) {
        final RawInventoryItem raw = new RawInventoryItem();
        item.toRaw(raw);
        return raw;
    }
    
    static {
        INSTANCE = new CollectorStackInventoryProvider();
    }
}
